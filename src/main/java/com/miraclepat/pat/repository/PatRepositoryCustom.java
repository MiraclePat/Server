package com.miraclepat.pat.repository;

import com.miraclepat.member.dto.MyPatListDto;
import com.miraclepat.pat.constant.SortType;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.dto.HomeBannerDto;
import com.miraclepat.pat.dto.HomePatListDto;
import com.miraclepat.pat.dto.PatListDto;

import java.util.List;

public interface PatRepositoryCustom {

    List<HomeBannerDto> getTodayProofPatList(List<Long> patIds, Long todayId);

    HomePatListDto getHomePatList(Long lastId, int size, SortType sort, String query,
                                  Long categoryId, boolean showFull, State state);

    PatListDto getMapPatList(int size, String query, Long categoryId, State state, boolean showFull,
                             Double leftLongitude, Double rightLongitude, Double bottomLatitude, Double topLatitude);

    MyPatListDto getJoinPatList(List<Long> ids, Long lastId, int size, State state);

    MyPatListDto getOpenPatList(List<Long> ids, Long lastId, int size, State state);

    Long updatePatStateCompleteMidnight();

    Long updatePatStateComplete();

    Long updatePatStateInProgress();
}
