package com.mjy.econometrics.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjy.econometrics.dto.CpiData;
import com.mjy.econometrics.model.CpiModel;
import com.mjy.econometrics.repository.CpiRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CpiScheduler {

    private final CpiRepository cpiDataRepository;
    private final WebClient webClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public CpiScheduler(CpiRepository cpiDataRepository, RedisTemplate<String, Object> redisTemplate) {
        this.cpiDataRepository = cpiDataRepository;
        this.webClient = WebClient.builder().baseUrl("https://api.stlouisfed.org/fred/series/").build();
        this.redisTemplate = redisTemplate;
    }

    @Value("${fred.api-key}")
    private String fredApiKey;

    //    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    @Scheduled(cron = "*/20 * * * * *") // 매 10초마다 실행
    public void saveCpiData() {
        String seriesId = "CPIAUCSL";

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("observations")
                        .queryParam("series_id", seriesId)
                        .queryParam("api_key", fredApiKey)
                        .queryParam("file_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .subscribe(response -> {
                    List<Map<String, Object>> observations = (List<Map<String, Object>>) response.get("observations");

                    // valueList를 사용하여 전체 데이터의 백분율 계산
                    List<BigDecimal> valueList = observations.stream()
                            .map(observation -> new BigDecimal(observation.get("value").toString()))
                            .collect(Collectors.toList());

                    BigDecimal max = Collections.max(valueList);
                    BigDecimal min = Collections.min(valueList);

                    List<BigDecimal> percentageList = valueList.stream()
                            .map(value -> {
                                if (value.equals(max)) {
                                    return BigDecimal.valueOf(100);
                                } else if (value.equals(min)) {
                                    return BigDecimal.ZERO;
                                } else {
                                    return value.subtract(min).divide(max.subtract(min), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                                }
                            })
                            .collect(Collectors.toList());

                    for (Map<String, Object> observation : observations) {
                        LocalDate date = LocalDate.parse(observation.get("date").toString());
                        BigDecimal value = new BigDecimal(observation.get("value").toString());

                        int index = valueList.indexOf(value);

                        if (index != -1) {
                            CpiData cpiData = new CpiData();
                            cpiData.setDate(date);
                            cpiData.setValue(value);
                            cpiData.setPercentage(percentageList.get(index));
                            redisTemplate.opsForList().rightPush("cpi", cpiData);
                        }

                        cpiDataRepository.save(new CpiModel(date, value));
                    }
                });
    }
}
