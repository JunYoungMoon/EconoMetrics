package com.mjy.econometrics.controller;

import com.mjy.econometrics.model.CpiData;
import com.mjy.econometrics.repository.CpiDataRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/cpi")
public class CpiDataController {
    private final CpiDataRepository cpiDataRepository;

    public CpiDataController(CpiDataRepository cpiDataRepository) {
        this.cpiDataRepository = cpiDataRepository;
    }

    @PostMapping
    public List<CpiData> getCpiData(@RequestParam(value = "start_date", defaultValue = "", required = false) String startDate,
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
            return cpiDataRepository.findByDateBetweenOrderByDateAsc(startLocalDate, endLocalDate);
        } else if (startLocalDate != null) {
            return cpiDataRepository.findByDateGreaterThanEqualOrderByDateAsc(startLocalDate);
        } else if (endLocalDate != null) {
            return cpiDataRepository.findByDateLessThanEqualOrderByDateAsc(endLocalDate);
        } else {
            return cpiDataRepository.findAllByOrderByDateAsc();
        }
    }
}
