package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.RaportEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RaportRepository extends CrudRepository<RaportEntity, Long> {
    @Query(value = "SELECT * FROM raport WHERE MONTH (DATE_) = date_month AND YEAR(DATE_) = date_year", nativeQuery = true)
    RaportEntity getByDate_MonthAndDate_Year(@Param("date_month") int date_month, @Param("date_year") int date_year);

}
