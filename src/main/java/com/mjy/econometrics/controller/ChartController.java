package com.mjy.econometrics.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjy.econometrics.model.CpiModel;
import com.mjy.econometrics.model.PceModel;
import com.mjy.econometrics.repository.CpiRepository;
import com.mjy.econometrics.repository.PceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chart-data")
public class ChartController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping
    public ResponseEntity<Map<String, List<Double>>> getChartData() {
        Map<String, List<Double>> chartData = new HashMap<>();


        List<String> cpiData = redisTemplate.opsForList().range("cpi", 0, -1);

//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            List<Map<String, Object>> dataList = objectMapper.readValue(data, new TypeReference<>() {});
//            for (Map<String, Object> dataMap : dataList) {
//                Double cpiValue = Double.parseDouble(dataMap.get("value").toString());
//                chartData.computeIfAbsent("cpi", k -> new ArrayList<>()).add(cpiValue);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return ResponseEntity.ok(chartData);
    }
}

