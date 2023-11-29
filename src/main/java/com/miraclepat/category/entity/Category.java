package com.miraclepat.category.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter //필수
@NoArgsConstructor//빈생성자
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", unique = true)
    private String categoryName;
}
