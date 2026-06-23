package com.parking.repository;

import com.parking.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link ParkingLot} entities.
 * <p>
 * Provides standard CRUD operations for parking lot records.
 * Additional query methods can be added for lot-specific lookups.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface ParkingLotRepository
        extends JpaRepository<ParkingLot, Long> {
}