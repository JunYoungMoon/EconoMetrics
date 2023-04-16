package com.mjy.econometrics.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chart-data")
public class ChartController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping
    public ResponseEntity<List<String>> getChartData() {
        List<String> cpiData = redisTemplate.opsForList().range("cpi", 0, -1);
        return ResponseEntity.ok(cpiData);
    }
}

