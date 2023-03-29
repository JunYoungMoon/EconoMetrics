package com.mjy.econometrics.scheduler;

import com.mjy.econometrics.model.CpiData;
import com.mjy.econometrics.repository.CpiDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class CpiDataScheduler {

    private final CpiDataRepository cpiDataRepository;
    private final WebClient webClient;

    public CpiDataScheduler(CpiDataRepository cpiDataRepository) {
        this.cpiDataRepository = cpiDataRepository;
        this.webClient = WebClient.builder().baseUrl("https://api.stlouisfed.org/fred/series/").build();
    }

    @Value("${fred.api-key}")
    private String fredApiKey;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void saveCpiData() {
        String seriesId = "CPIAUCSL";
        LocalDate today = LocalDate.now();

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
                    for (Map<String, Object> observation : observations) {
                        LocalDate date = LocalDate.parse(observation.get("date").toString());
                        if (date.isBefore(today.withDayOfMonth(1))) {
                            continue; // 2월 1일 이전 데이터는 저장하지 않음
                        }
                        BigDecimal value = new BigDecimal(observation.get("value").toString());

                        // 이미 저장된 데이터인지 확인하여, 중복 저장하지 않음
                        if (cpiDataRepository.findByDate(date).isEmpty()) {
                            cpiDataRepository.save(new CpiData(date, value));
                        }
                    }
                });

    }
}
