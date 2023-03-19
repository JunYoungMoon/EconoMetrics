package com.mjy.econometrics.controller;

import com.mjy.econometrics.model.CpiData;
import com.mjy.econometrics.repository.CpiDataRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cpi")
public class CpiDataController {
    private final CpiDataRepository cpiDataRepository;

    public CpiDataController(CpiDataRepository cpiDataRepository) {
        this.cpiDataRepository = cpiDataRepository;
    }


    @GetMapping
    public List<CpiData> getCpiData(@RequestParam(value = "start_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                    @RequestParam(value = "end_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return getCpiDataByDateRange(startDate, endDate);
        } else {
            return cpiDataRepository.findAllByOrderByDateAsc();
        }
    }

    @GetMapping
    public List<CpiData> getCpiDataByDateRange(@RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                               @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return cpiDataRepository.findByDateBetweenOrderByDateAsc(startDate, endDate);
    }

}
