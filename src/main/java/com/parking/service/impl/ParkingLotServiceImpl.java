package com.parking.service.impl;

import com.parking.entity.ParkingLot;
import com.parking.entity.ParkingSlot;
import com.parking.entity.Reservation;
import com.parking.enums.ReservationStatus;
import com.parking.enums.SlotStatus;
import com.parking.enums.SlotType;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.ParkingSlotRepository;
import com.parking.repository.ReservationRepository;
import com.parking.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ParkingLotService} for managing parking lots.
 * <p>
 * Handles lot creation with automatic slot generation, capacity
 * updates with dynamic slot addition/removal, and lot deletion
 * with safety checks for active slots.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository lotRepository;
    private final ParkingSlotRepository slotRepository;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public ParkingLot createLot(ParkingLot lot) {
        ParkingLot saved = lotRepository.save(lot);
        generateSlots(saved,
                lot.getCarSlots() != null ? lot.getCarSlots() : 0,
                lot.getBikeSlots() != null ? lot.getBikeSlots() : 0,
                lot.getEvSlots() != null ? lot.getEvSlots() : 0);
        return saved;
    }

    @Override
    public ParkingLot getLot(Long id) {
        return lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot not found: " + id));
    }

    @Override
    public List<ParkingLot> getAllLots() {
        return lotRepository.findAll();
    }

    @Override
    @Transactional
    public ParkingLot updateLot(Long id, ParkingLot updated) {
        ParkingLot lot = getLot(id);
        lot.setLotName(updated.getLotName());
        lot.setLocation(updated.getLocation());
        lot.setTotalSlots(updated.getTotalSlots());
        lot.setCarSlots(updated.getCarSlots());
        lot.setBikeSlots(updated.getBikeSlots());
        lot.setEvSlots(updated.getEvSlots());
        addAdditionalSlots(lot);
        return lotRepository.save(lot);
    }

    @Override
    @Transactional
    public void deleteLot(Long id) {
        ParkingLot lot = getLot(id);

        List<ParkingSlot> slots = slotRepository.findByParkingLot(lot);
        boolean hasActiveSlots = slots.stream()
                .anyMatch(s -> s.getStatus() == SlotStatus.OCCUPIED || s.getStatus() == SlotStatus.RESERVED);
        if (hasActiveSlots) {
            throw new RuntimeException("Cannot delete lot with active slots");
        }

        for (ParkingSlot s : slots) {
            List<Reservation> reservations = reservationRepository.findByParkingSlot(s);
            if (!reservations.isEmpty()) {
                reservationRepository.deleteAll(reservations);
            }
        }
        reservationRepository.flush();

        slotRepository.deleteAll(slots);
        lotRepository.delete(lot);
    }

    /**
     * Generates parking slots for a lot based on configured capacities.
     * Creates sequentially numbered slots for each vehicle type (CAR, BIKE, EV).
     *
     * @param lot      the parking lot to generate slots for
     * @param carSlots number of car slots to create
     * @param bikeSlots number of bike slots to create
     * @param evSlots   number of EV slots to create
     */
    private void generateSlots(ParkingLot lot, int carSlots, int bikeSlots, int evSlots) {
        List<ParkingSlot> slots = new ArrayList<>();

        for (int i = 1; i <= carSlots; i++) {
            slots.add(ParkingSlot.builder()
                    .slotNumber(String.format("CAR-%03d", i))
                    .slotType(SlotType.CAR)
                    .status(SlotStatus.AVAILABLE)
                    .parkingLot(lot)
                    .build());
        }

        for (int i = 1; i <= bikeSlots; i++) {
            slots.add(ParkingSlot.builder()
                    .slotNumber(String.format("BIKE-%03d", i))
                    .slotType(SlotType.BIKE)
                    .status(SlotStatus.AVAILABLE)
                    .parkingLot(lot)
                    .build());
        }

        for (int i = 1; i <= evSlots; i++) {
            slots.add(ParkingSlot.builder()
                    .slotNumber(String.format("EV-%03d", i))
                    .slotType(SlotType.EV)
                    .status(SlotStatus.AVAILABLE)
                    .parkingLot(lot)
                    .build());
        }

        if (!slots.isEmpty()) {
            slotRepository.saveAll(slots);
        }
    }

    /**
     * Adjusts the number of slots per type when a lot's capacity is updated.
     * Removes excess slots (if reducing capacity) or creates new slots
     * (if expanding), ensuring no active slots are removed.
     *
     * @param lot the parking lot to adjust slots for
     * @throws RuntimeException if attempting to reduce capacity while slots are occupied or reserved
     */
    private void addAdditionalSlots(ParkingLot lot) {
        List<ParkingSlot> existing = slotRepository.findByParkingLot(lot);
        long existingCar = existing.stream().filter(s -> s.getSlotType() == SlotType.CAR).count();
        long existingBike = existing.stream().filter(s -> s.getSlotType() == SlotType.BIKE).count();
        long existingEv = existing.stream().filter(s -> s.getSlotType() == SlotType.EV).count();

        int targetCar = lot.getCarSlots() != null ? lot.getCarSlots() : 0;
        int targetBike = lot.getBikeSlots() != null ? lot.getBikeSlots() : 0;
        int targetEv = lot.getEvSlots() != null ? lot.getEvSlots() : 0;

        if (targetCar < existingCar || targetBike < existingBike || targetEv < existingEv) {
            boolean hasActive = existing.stream()
                    .anyMatch(s -> s.getStatus() == SlotStatus.OCCUPIED || s.getStatus() == SlotStatus.RESERVED);
            if (hasActive) {
                throw new RuntimeException("Cannot reduce capacity while slots are occupied or reserved");
            }
        }

        List<ParkingSlot> toRemove = new ArrayList<>();
        List<ParkingSlot> newSlots = new ArrayList<>();

        if (targetCar < existingCar) {
            existing.stream()
                    .filter(s -> s.getSlotType() == SlotType.CAR)
                    .skip(targetCar)
                    .forEach(toRemove::add);
        }
        if (targetBike < existingBike) {
            existing.stream()
                    .filter(s -> s.getSlotType() == SlotType.BIKE)
                    .skip(targetBike)
                    .forEach(toRemove::add);
        }
        if (targetEv < existingEv) {
            existing.stream()
                    .filter(s -> s.getSlotType() == SlotType.EV)
                    .skip(targetEv)
                    .forEach(toRemove::add);
        }

        if (!toRemove.isEmpty()) {
            for (ParkingSlot s : toRemove) {
                List<Reservation> reservations = reservationRepository.findByParkingSlot(s);
                if (!reservations.isEmpty()) {
                    reservationRepository.deleteAll(reservations);
                }
            }
            reservationRepository.flush();
            slotRepository.deleteAll(toRemove);
        }

        for (int i = (int) existingCar + 1; i <= targetCar; i++) {
            newSlots.add(ParkingSlot.builder()
                    .slotNumber(String.format("CAR-%03d", i))
                    .slotType(SlotType.CAR)
                    .status(SlotStatus.AVAILABLE)
                    .parkingLot(lot)
                    .build());
        }

        for (int i = (int) existingBike + 1; i <= targetBike; i++) {
            newSlots.add(ParkingSlot.builder()
                    .slotNumber(String.format("BIKE-%03d", i))
                    .slotType(SlotType.BIKE)
                    .status(SlotStatus.AVAILABLE)
                    .parkingLot(lot)
                    .build());
        }

        for (int i = (int) existingEv + 1; i <= targetEv; i++) {
            newSlots.add(ParkingSlot.builder()
                    .slotNumber(String.format("EV-%03d", i))
                    .slotType(SlotType.EV)
                    .status(SlotStatus.AVAILABLE)
                    .parkingLot(lot)
                    .build());
        }

        if (!newSlots.isEmpty()) {
            slotRepository.saveAll(newSlots);
        }
    }
}
