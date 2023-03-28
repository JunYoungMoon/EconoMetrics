package com.mjy.econometrics.repository;

import com.mjy.econometrics.model.CpiData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CpiDataRepository extends JpaRepository<CpiData, Long> {
    List<CpiData> findAllByOrderByDateAsc();
    List<CpiData> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate);
    List<CpiData> findByDateGreaterThanEqualOrderByDateAsc(LocalDate startDate);
    List<CpiData> findByDateLessThanEqualOrderByDateAsc(LocalDate endDate);
    Optional<CpiData> findByDate(LocalDate date);
}
