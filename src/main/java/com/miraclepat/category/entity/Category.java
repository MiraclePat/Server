package com.miraclepat.category.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter //필수
@Setter //선택
@ToString //나중에 나오는 필드들에 대해 문자열을 자동으로 만들어줌
@NoArgsConstructor//빈생성자
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", unique = true)
    private String categoryName;
}
