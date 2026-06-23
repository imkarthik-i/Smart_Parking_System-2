package com.parking.service.impl;

import com.parking.entity.ParkingLot;
import com.parking.entity.ParkingSlot;
import com.parking.entity.Reservation;
import com.parking.enums.ReservationStatus;
import com.parking.enums.SlotStatus;
import com.parking.enums.SlotType;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.ParkingSlotRepository;
import com.parking.repository.ReservationRepository;
import com.parking.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link ParkingSlotService} for managing parking slots.
 * <p>
 * Handles slot CRUD with duplicate slot number validation within a lot,
 * availability queries filtered by status and type, and safe deletion
 * that prevents removal of occupied slots.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ParkingSlotServiceImpl implements ParkingSlotService {

    private final ParkingSlotRepository slotRepository;
    private final ReservationRepository reservationRepository;

    /**
     * {@inheritDoc}
     * <p>
     * Defaults the slot status to AVAILABLE and validates that
     * the slot number is unique within its parking lot.
     * </p>
     */
    @Override
    public ParkingSlot addSlot(ParkingSlot slot) {
        if (slot.getStatus() == null) {
            slot.setStatus(SlotStatus.AVAILABLE);
        }

        if (slot.getParkingLot() != null &&
            slotRepository.existsBySlotNumberAndParkingLot(slot.getSlotNumber(), slot.getParkingLot())) {
            throw new RuntimeException("Slot number '" + slot.getSlotNumber() + "' already exists in this parking lot");
        }

        return slotRepository.save(slot);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingSlot> getAllSlots() {
        return slotRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingSlot> getAvailableSlots() {
        return slotRepository.findByStatus(SlotStatus.AVAILABLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingSlot> getAvailableSlotsByType(SlotType slotType) {
        return slotRepository.findByStatusAndSlotType(SlotStatus.AVAILABLE, slotType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParkingSlot getSlot(Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking Slot not found with id : " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParkingSlot updateSlot(Long id, ParkingSlot updated) {
        ParkingSlot slot = getSlot(id);
        slot.setSlotNumber(updated.getSlotNumber());
        slot.setSlotType(updated.getSlotType());
        slot.setFloorNumber(updated.getFloorNumber());
        if (updated.getParkingLot() != null) {
            slot.setParkingLot(updated.getParkingLot());
        }
        if (updated.getStatus() != null) {
            slot.setStatus(updated.getStatus());
        }
        return slotRepository.save(slot);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingSlot> getAvailableSlotsByLotAndType(ParkingLot lot, SlotType slotType) {
        return slotRepository.findByParkingLotAndSlotTypeAndStatus(lot, slotType, SlotStatus.AVAILABLE);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Prevents deletion of occupied slots and cleans up associated
     * reservations before removal.
     * </p>
     */
    @Override
    public void deleteSlot(Long id) {
        ParkingSlot slot = slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking Slot not found with id : " + id));

        if (slot.getStatus() == SlotStatus.OCCUPIED) {
            throw new RuntimeException("Cannot delete occupied slot");
        }

        List<Reservation> reservations = reservationRepository.findByParkingSlot(slot);
        if (!reservations.isEmpty()) {
            reservationRepository.deleteAll(reservations);
        }
        reservationRepository.flush();

        slotRepository.delete(slot);
    }
}
