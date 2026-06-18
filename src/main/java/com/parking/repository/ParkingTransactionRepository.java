package com.parking.repository;

import com.parking.entity.ParkingSlot;
import com.parking.entity.ParkingTransaction;
import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingTransactionRepository
        extends JpaRepository<ParkingTransaction, Long> {

    List<ParkingTransaction> findByVehicle(Vehicle vehicle);

    List<ParkingTransaction> findByStatus(TransactionStatus status);

    Optional<ParkingTransaction> findByVehicleAndStatus(
            Vehicle vehicle,
            TransactionStatus status
    );

    boolean existsByParkingSlotAndStatus(
            ParkingSlot slot,
            TransactionStatus status
    );

    @Query("SELECT t FROM ParkingTransaction t WHERE t.vehicle.user = :user ORDER BY t.entryTime DESC")
    List<ParkingTransaction> findByUser(@Param("user") User user);

    @Query("SELECT t FROM ParkingTransaction t WHERE t.vehicle.user = :user AND t.status = :status ORDER BY t.entryTime DESC")
    List<ParkingTransaction> findByUserAndStatus(@Param("user") User user, @Param("status") TransactionStatus status);
}