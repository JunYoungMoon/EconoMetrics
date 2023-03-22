package com.mjy.econometrics.repository;

import com.mjy.econometrics.model.CpiData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CpiDataRepository extends JpaRepository<CpiData, Long> {
    List<CpiData> findAllByOrderByDateAsc();
    List<CpiData> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate);
    List<CpiData> findByDateGreaterThanEqualOrderByDateAsc(LocalDate startDate);
    List<CpiData> findByDateLessThanEqualOrderByDateAsc(LocalDate endDate);
}
