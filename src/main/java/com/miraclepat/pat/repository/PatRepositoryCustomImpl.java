package com.miraclepat.pat.repository;

import com.miraclepat.member.dto.MyPatDto;
import com.miraclepat.member.dto.MyPatListDto;
import com.miraclepat.pat.constant.SortType;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.dto.HomePatDto;
import com.miraclepat.pat.dto.HomePatListDto;
import com.miraclepat.pat.dto.PatDto;
import com.miraclepat.pat.dto.PatListDto;
import com.miraclepat.utils.helper.GeometryHelper;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static com.miraclepat.pat.entity.QPat.pat;

@Slf4j
public class PatRepositoryCustomImpl implements PatRepositoryCustom {

    private JPAQueryFactory queryFactory;
    private GeometryHelper geometryHelper;

    public PatRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
        geometryHelper = new GeometryHelper();
    }

    @Override
    public HomePatListDto getHomePatList(Long lastId, int size, SortType sort, String query,
                                         Long categoryId, boolean showFull, State state) {

        List<HomePatDto> homePatList = queryFactory
                .select(Projections.constructor(HomePatDto.class, pat.id, pat.repImg, pat.patName, pat.startDate,
                        pat.category.categoryName, pat.location, pat.nowPerson, pat.maxPerson))
                .from(pat)
                .where(searchStateEq(state),
                        searchCategory(categoryId),
                        filterFullPersonPat(showFull),
                        searchQuery(query),
                        dynamicPredicateByLastId(lastId, sort))
                .orderBy(sort(sort).toArray(OrderSpecifier[]::new))
                .limit(size + 1)
                .fetch();

        boolean hasNext = homePatList.size() > size;
        if (hasNext) {
            homePatList.remove(homePatList.size() - 1);
        }

        System.out.println("homePatList: " + homePatList);

        HomePatListDto response = new HomePatListDto(homePatList, hasNext);
        return response;
    }

    @Override
    public PatListDto getMapPatList(int size, String query, Long categoryId, State state, boolean showFull,
                                    Double leftLongitude, Double rightLongitude, Double bottomLatitude, Double topLatitude) {

        Polygon polygon = geometryHelper.createPolygon(leftLongitude, rightLongitude, bottomLatitude, topLatitude);

        List<PatDto> patDtoList = queryFactory
                .select(Projections.constructor(PatDto.class, pat.id, pat.repImg, pat.patName, pat.startDate,
                        pat.category.categoryName, pat.longLat))
                .from(pat)
                .where(searchQuery(query),
                        searchCategory(categoryId),
                        searchStateEq(state),
                        filterFullPersonPat(showFull),
                        pat.longLat.isNotNull(),
                        pat.longLat.within(polygon))
                .orderBy(pat.id.desc())
                .limit(size)
                .fetch();

        PatListDto response = new PatListDto(patDtoList);

        return response;
    }

    @Override
    public MyPatListDto getJoinPatList(List<Long> ids, Long lastId, int size, State state) {

        List<MyPatDto> myPatDtoList = queryFactory
                .select(Projections.constructor(MyPatDto.class,
                        pat.id, pat.repImg, pat.patName, pat.startDate,
                        pat.category.categoryName, pat.nowPerson,
                        pat.maxPerson, pat.location, pat.days,
                        pat.state, Expressions.constant(false)))
                .from(pat)
                .where(pat.id.in(ids),
                        searchStateEq(state),
                        dynamicPredicateByLastId(lastId, null))
                .orderBy(pat.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = myPatDtoList.size() > size;
        if (hasNext) {
            myPatDtoList.remove(myPatDtoList.size() - 1);
        }

        MyPatListDto response = new MyPatListDto(myPatDtoList, hasNext);
        return response;
    }

    @Override
    public MyPatListDto getOpenPatList(List<Long> ids, Long lastId, int size, State state) {

        List<MyPatDto> myPatDtoList = queryFactory
                .select(Projections.constructor(MyPatDto.class,
                        pat.id, pat.repImg, pat.patName, pat.startDate,
                        pat.category.categoryName, pat.nowPerson,
                        pat.maxPerson, pat.location, pat.days,
                        pat.state, Expressions.constant(false)))
                .from(pat)
                .where(pat.id.in(ids),
                        searchStateEq(state),
                        dynamicPredicateByLastId(lastId, null))
                .orderBy(pat.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = myPatDtoList.size() > size;
        if (hasNext) {
            myPatDtoList.remove(myPatDtoList.size() - 1);
        }

        MyPatListDto response = new MyPatListDto(myPatDtoList, hasNext);
        return response;
    }

    @Override
    public Long updatePatStateComplete() {
        //LocalDate, LocalTime을 기준으로 상태를 update 한다.
        //State가 진행중: 오늘이 endDate 이고, endTime이 지났다면 진행중 -> 완료
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        return queryFactory.update(pat)
                .set(pat.state, State.COMPLETED)
                .where(pat.state.eq(State.IN_PROGRESS),
                        pat.endDate.eq(today).and(pat.endTime.lt(nowTime)))
                .execute();
    }

    @Override
    public Long updatePatStateInProgress() {
        //LocalDate, LocalTime을 기준으로 상태를 update 한다.
        //State가 진행 예정: 오늘이 startDate이고 startTime이 지났다면 진행 예정 -> 진행중
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        return queryFactory.update(pat)
                .set(pat.state, State.IN_PROGRESS)
                .where(pat.state.eq(State.SCHEDULED),
                        pat.startDate.eq(today).and(pat.startTime.lt(nowTime)))
                .execute();
    }

    //방 진행 상태
    private BooleanExpression searchStateEq(State state) {
        if (state == null) {
            return null;
        }
        return pat.state.eq(state);
    }

    //카테고리
    private BooleanExpression searchCategory(Long categoryId) {
        return categoryId == null ? null : pat.category.id.eq(categoryId);
    }

    //인원 마감인 방 보여줄 것인가?
    private BooleanExpression filterFullPersonPat(boolean showFull) {
        return showFull ? null : pat.nowPerson.lt(pat.maxPerson);
    }

    //검색어
    private BooleanExpression searchQuery(String query) {
        return query == null ? null : pat.patName.like("%" + query + "%");
    }

    //정렬
    private List<OrderSpecifier> sort(SortType sortType) {
        if (sortType == null) {
            return Arrays.asList(pat.id.desc());
        }
        switch (sortType) {
            case LATEST:
                // 가장 최신 것부터 정렬
                return Arrays.asList(pat.id.desc());
            case HOT:
                // 참여자가 많은 것부터 정렬
                return Arrays.asList(pat.nowPerson.desc(), pat.id.desc());
            default:
                return Arrays.asList(pat.startDate.asc(), pat.id.desc());
        }
    }

    //id
    private BooleanExpression dynamicPredicateByLastId(Long lastId, SortType sortType) {
        if (lastId == null) {
            return null;
        }
        if (sortType == null) {
            return pat.id.lt(lastId);
        }
        switch (sortType) {
            case HOT:
                Integer lastNowPerson = nowPersonByLastIdSubQuery(lastId);
                // 참여자가 많은 것부터 정렬
                return ((pat.nowPerson.eq(lastNowPerson).and(pat.id.lt(lastId)))
                        .or(pat.nowPerson.lt(lastNowPerson)));
            default:
                return pat.id.lt(lastId);
        }
    }

    //nowPerson 수를 가져오는 서브쿼리
    private Integer nowPersonByLastIdSubQuery(Long lastId) {
        return queryFactory.select(pat.nowPerson)
                .from(pat)
                .where(pat.id.eq(lastId))
                .fetchOne();
    }

    private BooleanExpression positionWithinPolygon(Double leftLongitude, Double rightLongitude, Double bottomLatitude, Double topLatitude){
        String polygonText = String.format(
                "POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
                leftLongitude, topLatitude, rightLongitude, topLatitude, rightLongitude, bottomLatitude, leftLongitude, bottomLatitude, leftLongitude, topLatitude
        );
        log.info("********************getMap-polygonText: "+polygonText);
        BooleanExpression condition = Expressions
                .booleanTemplate("ST_Within(pat.longLat, ST_GeomFromText('{0}', 4326))", polygonText);

        return condition;
    }
}