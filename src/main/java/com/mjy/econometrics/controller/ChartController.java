package com.mjy.econometrics.controller;

import com.mjy.econometrics.model.CpiModel;
import com.mjy.econometrics.model.PceModel;
import com.mjy.econometrics.repository.CpiRepository;
import com.mjy.econometrics.repository.PceRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chart-data")
public class ChartController {

    private final CpiRepository cpiDataRepository;
    private final PceRepository pceDataRepository;
    private final RedisTemplate<String, Double> redisTemplate;

    public ChartController(CpiRepository cpiDataRepository, PceRepository pceDataRepository, RedisTemplate<String, Double> redisTemplate) {
        this.cpiDataRepository = cpiDataRepository;
        this.pceDataRepository = pceDataRepository;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Double>>> getChartData() {
        Map<String, List<Double>> chartData = new HashMap<>();

        List<Double> cpiData = redisTemplate.opsForValue().get("chart-data:cpi");
        if (cpiData == null) {
            cpiData = getCpiPercentageData();
            redisTemplate.opsForValue().set("chart-data:cpi", cpiData);
        }

        List<Double> pceData = redisTemplate.opsForValue().get("chart-data:pce");
        if (pceData == null) {
            pceData = getPcePercentageData();
            redisTemplate.opsForValue().set("chart-data:pce", pceData);
        }

        chartData.put("cpi", cpiData);
        chartData.put("pce", pceData);

        return ResponseEntity.ok(chartData);
    }

    private List<Double> getCpiPercentageData() {
        List<CpiModel> cpiData = cpiDataRepository.findAllByOrderByDateAsc();
        double firstValue = cpiData.get(0).getValue();
        return cpiData.stream()
                .map(cpiModel -> cpiModel.getValue() / firstValue * 100)
                .collect(Collectors.toList());
    }

    private List<Double> getPcePercentageData() {
        List<PceModel> pceData = pceDataRepository.findAllByOrderByDateAsc();
        double firstValue = pceData.get(0).getValue();
        return pceData.stream()
                .map(pceModel -> pceModel.getValue() / firstValue * 100)
                .collect(Collectors.toList());
    }
}

