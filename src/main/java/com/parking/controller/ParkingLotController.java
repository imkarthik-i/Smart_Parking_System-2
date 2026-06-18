package com.parking.controller;

import com.parking.dto.ParkingLotDTO;
import com.parking.entity.ParkingLot;
import com.parking.security.SecurityHelper;
import com.parking.service.ParkingLotService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "BearerAuth")
@Tag(
	    name = "Parking Lot Management APIs",
	    description = "APIs for managing parking lots, locations and capacity"
	)
@RestController
@RequestMapping("/api/lots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService lotService;
    private final SecurityHelper securityHelper;

    @PostMapping
    public ParkingLotDTO create(@Valid @RequestBody ParkingLotDTO dto) {
        if (!securityHelper.isAdmin()) {
            throw new RuntimeException("Only admins can create parking lots");
        }

        ParkingLot lot = ParkingLot.builder()
                .lotName(dto.getLotName())
                .location(dto.getLocation())
                .totalSlots(dto.getTotalSlots())
                .build();

        ParkingLot saved = lotService.createLot(lot);

        return convert(saved);
    }

    @GetMapping
    public List<ParkingLotDTO> getAll() {
        return lotService.getAllLots()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ParkingLotDTO get(@PathVariable Long id) {
        return convert(lotService.getLot(id));
    }

    private ParkingLotDTO convert(ParkingLot lot) {
        return new ParkingLotDTO(
                lot.getLotId(),
                lot.getLotName(),
                lot.getLocation(),
                lot.getTotalSlots()
        );
    }
}