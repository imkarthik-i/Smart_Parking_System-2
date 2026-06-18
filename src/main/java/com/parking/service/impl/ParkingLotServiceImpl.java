package com.parking.service.impl;

import com.parking.entity.ParkingLot;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.ParkingLotRepository;
import com.parking.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository lotRepository;

    @Override
    public ParkingLot createLot(ParkingLot lot) {
        return lotRepository.save(lot);
    }

    @Override
    public ParkingLot getLot(Long id) {
        return lotRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot not found: " + id));
    }

    @Override
    public List<ParkingLot> getAllLots() {
        return lotRepository.findAll();
    }

    @Override
    public void deleteLot(Long id) {
        ParkingLot lot = getLot(id);
        lotRepository.delete(lot);
    }
}