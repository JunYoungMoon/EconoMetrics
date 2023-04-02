package com.mjy.econometrics.controller;

import com.mjy.econometrics.model.CpiModel;
import com.mjy.econometrics.model.PceModel;
import com.mjy.econometrics.repository.CpiRepository;
import com.mjy.econometrics.repository.PceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chart-data")
public class ChartController {

    private final CpiRepository cpiDataRepository;
    private final PceRepository pceDataRepository;

    public ChartController(CpiRepository cpiDataRepository, PceRepository pceDataRepository) {
        this.cpiDataRepository = cpiDataRepository;
        this.pceDataRepository = pceDataRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Double>>> getChartData() {
        Map<String, List<Double>> chartData = new HashMap<>();

        return ResponseEntity.ok(chartData);
    }
}

