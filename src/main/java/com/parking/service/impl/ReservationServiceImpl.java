package com.parking.service.impl;

import com.parking.entity.*;
import com.parking.enums.ReservationStatus;
import com.parking.enums.SlotStatus;
import com.parking.enums.SlotType;
import com.parking.exception.ResourceNotFoundException;
import com.parking.exception.SlotNotAvailableException;
import com.parking.repository.*;
import com.parking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link ReservationService} for managing parking reservations.
 * <p>
 * Handles reservation creation with slot availability validation,
 * vehicle type compatibility checking, and reservation cancellation
 * that releases the associated slot.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository slotRepository;

    /**
     * {@inheritDoc}
     * <p>
     * Creates an immediate reservation (start time defaults to now, no end time).
     * </p>
     */
    @Override
    public Reservation createReservation(Long vehicleId, Long slotId) {
        return createReservation(vehicleId, slotId, null, null);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Validates slot availability, verifies vehicle-to-slot type
     * compatibility, marks the slot as RESERVED, and persists
     * the reservation.
     * </p>
     */
    @Override
    public Reservation createReservation(Long vehicleId, Long slotId, LocalDateTime startTime, LocalDateTime endTime) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new SlotNotAvailableException("Slot not available");
        }

        String vehicleType = vehicle.getVehicleType();
        SlotType requiredSlotType;
        try {
            requiredSlotType = SlotType.valueOf(vehicleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid vehicle type: " + vehicleType);
        }

        if (slot.getSlotType() != requiredSlotType) {
            throw new RuntimeException("Slot type " + slot.getSlotType() + " does not match vehicle type " + vehicleType);
        }

        Reservation reservation = Reservation.builder()
                .vehicle(vehicle)
                .parkingSlot(slot)
                .reservationTime(LocalDateTime.now())
                .startTime(startTime != null ? startTime : LocalDateTime.now())
                .endTime(endTime)
                .status(ReservationStatus.CONFIRMED)
                .build();

        slot.setStatus(SlotStatus.RESERVED);
        slotRepository.save(slot);

        return reservationRepository.save(reservation);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sets the reservation status to CANCELLED and releases
     * the associated parking slot back to AVAILABLE.
     * </p>
     */
    @Override
    public Reservation cancelReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        reservation.setStatus(ReservationStatus.CANCELLED);

        ParkingSlot slot = reservation.getParkingSlot();
        slot.setStatus(SlotStatus.AVAILABLE);

        slotRepository.save(slot);

        return reservationRepository.save(reservation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reservation> getReservationsByUser(User user) {
        return reservationRepository.findByUser(user);
    }
}