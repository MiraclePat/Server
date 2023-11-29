package com.miraclepat.days.repository;

import com.miraclepat.days.entity.Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DaysRepository extends JpaRepository<Days, Long> {
    Days findByDayName(String dayName);


    @Query("SELECT d.id FROM Days d WHERE d.dayName = :dayName")
    Long findIdByDayName(@Param("dayName") String dayName);
}
