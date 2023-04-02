package com.mjy.econometrics.repository;

import com.mjy.econometrics.model.CpiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CpiRepository extends JpaRepository<CpiModel, Long> {
    List<CpiModel> findAllByOrderByDateAsc();
    List<CpiModel> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate);
    List<CpiModel> findByDateGreaterThanEqualOrderByDateAsc(LocalDate startDate);
    List<CpiModel> findByDateLessThanEqualOrderByDateAsc(LocalDate endDate);
    Optional<CpiModel> findByDate(LocalDate date);
    Optional<CpiModel> findTopByOrderByDateDesc();
}
