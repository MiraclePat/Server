package com.miraclepat.utils.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public abstract class BaseModifiableEntity extends BaseTimeEntity{ //시간과 작성자 모두 갖는 엔티티는 BaseEntity 상속

    @LastModifiedDate //엔티티의 값을 변경할 때 시간을 자동으로 저장
    @Column(nullable = false)
    private LocalDateTime updateTime;
}
