package com.parking.service.impl;

import com.parking.entity.*;
import com.parking.enums.TransactionStatus;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.*;
import com.parking.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final ParkingTransactionRepository transactionRepository;
    private final BillingRepository billingRepository;
    private final VehicleRepository vehicleRepository;

    private static final double RATE_PER_HOUR = 50.0;

    @Override
    public Billing generateBill(Long transactionId) {

        ParkingTransaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (tx.getStatus() != TransactionStatus.COMPLETED) {
            throw new RuntimeException("Cannot bill active transaction");
        }

        double amount = tx.getDuration() * RATE_PER_HOUR;

        Billing bill = Billing.builder()
                .ratePerHour(RATE_PER_HOUR)
                .totalAmount(amount)
                .paymentStatus("PENDING")
                .transaction(tx)
                .build();

        return billingRepository.save(bill);
    }

    @Override
    public Billing getBill(Long billingId) {
        return billingRepository.findById(billingId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
    }

    @Override
    public List<Billing> getAllBills() {
        return billingRepository.findAll();
    }

    @Override
    public List<Billing> getBillsByUser(User user) {
        return billingRepository.findAll().stream()
                .filter(b -> b.getTransaction() != null
                        && b.getTransaction().getVehicle() != null
                        && b.getTransaction().getVehicle().getUser() != null
                        && b.getTransaction().getVehicle().getUser().getUserId().equals(user.getUserId()))
                .collect(Collectors.toList());
    }
}
