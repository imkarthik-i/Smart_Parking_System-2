package com.parking.service.impl;

import com.parking.entity.*;
import com.parking.enums.SlotStatus;
import com.parking.enums.TransactionStatus;
import com.parking.exception.ResourceNotFoundException;
import com.parking.exception.SlotNotAvailableException;
import com.parking.repository.*;
import com.parking.service.ParkingTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link ParkingTransactionService} for managing
 * vehicle entry and exit operations.
 * <p>
 * Handles the complete parking lifecycle: vehicle entry with slot
 * occupancy, exit processing with duration calculation, automatic
 * bill generation, and reservation cancellation upon entry.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ParkingTransactionServiceImpl implements ParkingTransactionService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository slotRepository;
    private final ParkingTransactionRepository transactionRepository;
    private final ReservationRepository reservationRepository;
    private final BillingRepository billingRepository;

    /**
     * Standard hourly parking rate in currency units.
     */
    private static final double RATE_PER_HOUR = 50.0;

    /**
     * {@inheritDoc}
     * <p>
     * Validates vehicle and slot existence, checks slot availability,
     * cancels any existing confirmed reservations for the slot, marks
     * the slot as OCCUPIED, and creates a new active transaction.
     * </p>
     */
    @Override
    @Transactional
    public ParkingTransaction vehicleEntry(String vehicleNumber, Long slotId) {

        Vehicle vehicle = vehicleRepository.findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (slot.getStatus() == SlotStatus.OCCUPIED) {
            throw new SlotNotAvailableException("Slot already occupied");
        }

        reservationRepository.findAll()
                .stream()
                .filter(r ->
                        r.getParkingSlot().getSlotId().equals(slotId)
                                && r.getStatus().name().equals("CONFIRMED"))
                .forEach(r -> r.setStatus(com.parking.enums.ReservationStatus.CANCELLED));

        slot.setStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);

        ParkingTransaction tx = ParkingTransaction.builder()
                .vehicle(vehicle)
                .parkingSlot(slot)
                .entryTime(LocalDateTime.now())
                .status(TransactionStatus.ACTIVE)
                .build();

        return transactionRepository.save(tx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Calculates parking duration from entry to exit time, sets
     * the transaction status to COMPLETED, releases the slot,
     * and auto-generates a billing record with the computed amount.
     * </p>
     */
    @Override
    @Transactional
    public ParkingTransaction vehicleExit(Long transactionId) {

        ParkingTransaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        tx.setExitTime(LocalDateTime.now());

        long minutes = Duration.between(
                tx.getEntryTime(),
                tx.getExitTime()
        ).toMinutes();

        tx.setDuration(Math.max(0.1, minutes / 60.0));
        tx.setStatus(TransactionStatus.COMPLETED);

        ParkingSlot slot = tx.getParkingSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);

        ParkingTransaction savedTx = transactionRepository.save(tx);

        // Auto-generate bill on exit
        double amount = savedTx.getDuration() * RATE_PER_HOUR;
        Billing bill = Billing.builder()
                .ratePerHour(RATE_PER_HOUR)
                .totalAmount(amount)
                .paymentStatus("PENDING")
                .transaction(savedTx)
                .build();
        billingRepository.save(bill);

        return savedTx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParkingTransaction getTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingTransaction> getTransactionsByUser(User user) {
        return transactionRepository.findByUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
