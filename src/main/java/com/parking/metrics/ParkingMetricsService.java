package com.parking.metrics;

import com.parking.enums.SlotStatus;
import com.parking.enums.TransactionStatus;
import com.parking.repository.*;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingMetricsService {

    private final ParkingSlotRepository slotRepository;
    private final ParkingTransactionRepository transactionRepository;
    private final BillingRepository billingRepository;

    private final MeterRegistry meterRegistry;

    public void registerMetrics() {

        // 🅿️ Available slots
        Gauge.builder("parking.slots.available", slotRepository,
                repo -> repo.countByStatus(SlotStatus.AVAILABLE))
                .register(meterRegistry);

        // 🚗 Occupied slots (active parking)
        Gauge.builder("parking.slots.occupied", slotRepository,
                repo -> repo.countByStatus(SlotStatus.OCCUPIED))
                .register(meterRegistry);

        // 🚗 Active transactions
        Gauge.builder("parking.transactions.active", transactionRepository,
                repo -> repo.findByStatus(TransactionStatus.ACTIVE).size())
                .register(meterRegistry);

        // 💰 Total revenue
        Gauge.builder("parking.revenue.total", billingRepository,
                repo -> repo.findAll()
                        .stream()
                        .mapToDouble(b -> b.getTotalAmount() == null ? 0 : b.getTotalAmount())
                        .sum())
                .register(meterRegistry);
    }
}