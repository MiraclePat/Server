package com.miraclepat.utils.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) // Auditing을 적용
@MappedSuperclass //공통 매핑 정보가 필요할 때 사용하는 어노테이션. 부모 클래스를 상속받는 자식 클래스에 매핑 정보만 제공
@Getter
@Setter
public abstract class BaseTimeEntity { //객체를 만들 일은 없음. 등록일, 수정일만 있는 경우.

    @CreatedDate //엔티티가 생성되어 저장될 때 시간을 자동으로 저장
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate //엔티티의 값을 변경할 때 시간을 자동으로 저장
    private LocalDateTime updateTime;
}
