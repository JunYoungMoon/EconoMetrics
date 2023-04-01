package com.mjy.econometrics.controller;

import com.mjy.econometrics.model.PceModel;
import com.mjy.econometrics.repository.PceRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/pce")
public class PceController {
    private final PceRepository pceDataRepository;

    public PceController(PceRepository PceDataRepository) {
        this.pceDataRepository = PceDataRepository;
    }

    @PostMapping
    public List<PceModel> getPceData(@RequestParam(value = "start_date", defaultValue = "", required = false) String startDate,
                                     @RequestParam(value = "end_date", defaultValue = "", required = false) String endDate) {
        LocalDate startLocalDate = null;
        LocalDate endLocalDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (!startDate.isEmpty()) {
            startLocalDate = LocalDate.parse(startDate, formatter);
        }
        if (!endDate.isEmpty()) {
            endLocalDate = LocalDate.parse(endDate, formatter);
        }

        if (startLocalDate != null && endLocalDate != null) {
            return pceDataRepository.findByDateBetweenOrderByDateAsc(startLocalDate, endLocalDate);
        } else if (startLocalDate != null) {
            return pceDataRepository.findByDateGreaterThanEqualOrderByDateAsc(startLocalDate);
        } else if (endLocalDate != null) {
            return pceDataRepository.findByDateLessThanEqualOrderByDateAsc(endLocalDate);
        } else {
            return pceDataRepository.findAllByOrderByDateAsc();
        }
    }
}
