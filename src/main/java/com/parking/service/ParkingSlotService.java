package com.parking.service;

import com.parking.entity.ParkingSlot;
import com.parking.enums.SlotType;

import java.util.List;

public interface ParkingSlotService {

    ParkingSlot addSlot(ParkingSlot slot);

    List<ParkingSlot> getAllSlots();

    List<ParkingSlot> getAvailableSlots();

    List<ParkingSlot> getAvailableSlotsByType(
            SlotType slotType);

    ParkingSlot getSlot(Long id);

    void deleteSlot(Long id);
}