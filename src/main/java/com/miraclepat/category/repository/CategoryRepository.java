package com.miraclepat.category.repository;

import com.miraclepat.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);

    @Query("select c.id from Category c where c.categoryName = :categoryName")
    Long findIdByCategoryName(@Param("categoryName") String categoryName);
}
