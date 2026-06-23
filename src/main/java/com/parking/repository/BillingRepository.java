package com.parking.repository;

import com.parking.dto.BillingDTO;
import com.parking.entity.Billing;
import com.parking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Billing} entities.
 * <p>
 * Provides custom JPQL queries that return enriched {@link BillingDTO}
 * objects with joined transaction, vehicle, slot, and lot information.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface BillingRepository extends JpaRepository<Billing, Long> {

    /**
     * Retrieves a complete billing DTO by billing ID, including transaction,
     * vehicle, slot, and lot details.
     *
     * @param id the billing identifier
     * @return an {@link Optional} containing the {@link BillingDTO} if found
     */
    @Query("SELECT new com.parking.dto.BillingDTO(" +
           "b.billingId, t.transactionId, " +
           "b.ratePerHour, b.totalAmount, b.paymentStatus, " +
           "t.entryTime, t.exitTime, t.duration, " +
           "v.vehicleNumber, v.vehicleType, " +
           "ps.slotNumber, pl.lotName) " +
           "FROM Billing b " +
           "LEFT JOIN b.transaction t " +
           "LEFT JOIN t.vehicle v " +
           "LEFT JOIN t.parkingSlot ps " +
           "LEFT JOIN ps.parkingLot pl " +
           "WHERE b.billingId = :id")
    Optional<BillingDTO> findBillingDTOById(@Param("id") Long id);

    /**
     * Retrieves all billing records as DTOs with complete details.
     *
     * @return list of {@link BillingDTO} objects
     */
    @Query("SELECT new com.parking.dto.BillingDTO(" +
           "b.billingId, t.transactionId, " +
           "b.ratePerHour, b.totalAmount, b.paymentStatus, " +
           "t.entryTime, t.exitTime, t.duration, " +
           "v.vehicleNumber, v.vehicleType, " +
           "ps.slotNumber, pl.lotName) " +
           "FROM Billing b " +
           "LEFT JOIN b.transaction t " +
           "LEFT JOIN t.vehicle v " +
           "LEFT JOIN t.parkingSlot ps " +
           "LEFT JOIN ps.parkingLot pl")
    List<BillingDTO> findAllBillingDTOs();

    /**
     * Retrieves all billing records for a specific user as DTOs.
     *
     * @param user the user entity
     * @return list of {@link BillingDTO} objects for the user
     */
    @Query("SELECT new com.parking.dto.BillingDTO(" +
           "b.billingId, t.transactionId, " +
           "b.ratePerHour, b.totalAmount, b.paymentStatus, " +
           "t.entryTime, t.exitTime, t.duration, " +
           "v.vehicleNumber, v.vehicleType, " +
           "ps.slotNumber, pl.lotName) " +
           "FROM Billing b " +
           "LEFT JOIN b.transaction t " +
           "LEFT JOIN t.vehicle v " +
           "LEFT JOIN t.parkingSlot ps " +
           "LEFT JOIN ps.parkingLot pl " +
           "WHERE v.user = :user")
    List<BillingDTO> findBillingDTOsByUser(@Param("user") User user);
}