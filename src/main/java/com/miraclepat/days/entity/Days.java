package com.miraclepat.days.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
public class Days {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql
    @Column(name = "days_id")
    private Long id;

    @Column(name = "day_name")
    private String dayName;
}
