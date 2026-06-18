package com.parking.metrics;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetricsInitializer {

    private final ParkingMetricsService metricsService;

    @PostConstruct
    public void init() {
        metricsService.registerMetrics();
    }
}