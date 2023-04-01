package com.mjy.econometrics.repository;

import com.mjy.econometrics.model.PceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PceRepository extends JpaRepository<PceModel, Long> {
    List<PceModel> findAllByOrderByDateAsc();
    List<PceModel> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate);
    List<PceModel> findByDateGreaterThanEqualOrderByDateAsc(LocalDate startDate);
    List<PceModel> findByDateLessThanEqualOrderByDateAsc(LocalDate endDate);
    Optional<PceModel> findByDate(LocalDate date);
}
