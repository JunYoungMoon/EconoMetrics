package com.mjy.econometrics.scheduler;

import com.mjy.econometrics.dto.PceData;
import com.mjy.econometrics.model.PceModel;
import com.mjy.econometrics.repository.PceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PceScheduler {

    private final PceRepository PceDataRepository;
    private final WebClient webClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public PceScheduler(PceRepository PceDataRepository, RedisTemplate<String, Object> redisTemplate) {
        this.PceDataRepository = PceDataRepository;
        this.redisTemplate = redisTemplate;
        this.webClient = WebClient.builder().baseUrl("https://api.stlouisfed.org/fred/series/").build();
    }

    @Value("${fred.api-key}")
    private String fredApiKey;

    @Scheduled(cron = "*/20 * * * * *") // 매 10초마다 실행
    public void savePceData() {
        String seriesId = "PCE";

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
                            PceData PceData = new PceData();
                            PceData.setDate(date);
                            PceData.setValue(value);
                            PceData.setPercentage(percentageList.get(index));
                            redisTemplate.opsForList().rightPush("Pce", PceData);
                        }

                        PceDataRepository.save(new PceModel(date, value));
                    }
                });
    }
}
