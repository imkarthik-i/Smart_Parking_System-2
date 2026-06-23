package com.parking.repository;

import com.parking.dto.PaymentDTO;
import com.parking.entity.Payment;
import com.parking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Payment} entities.
 * <p>
 * Provides standard CRUD operations and custom JPQL queries that
 * return enriched {@link PaymentDTO} objects with joined vehicle,
 * slot, and lot information.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Finds a payment by its associated billing record.
     *
     * @param billing the billing entity
     * @return an {@link Optional} containing the payment if found, or empty
     */
    Optional<Payment> findByBilling(com.parking.entity.Billing billing);

    /**
     * Retrieves a complete payment DTO by payment ID, including vehicle,
     * slot, and lot details.
     *
     * @param id the payment identifier
     * @return an {@link Optional} containing the {@link PaymentDTO} if found
     */
    @Query("SELECT new com.parking.dto.PaymentDTO(" +
           "p.paymentId, b.billingId, t.transactionId, " +
           "p.amount, p.paymentMethod, CAST(p.status AS string), p.paymentTime, " +
           "v.vehicleNumber, v.vehicleType, " +
           "ps.slotNumber, pl.lotName, " +
           "t.entryTime, t.exitTime, t.duration) " +
           "FROM Payment p " +
           "LEFT JOIN p.billing b " +
           "LEFT JOIN b.transaction t " +
           "LEFT JOIN t.vehicle v " +
           "LEFT JOIN t.parkingSlot ps " +
           "LEFT JOIN ps.parkingLot pl " +
           "WHERE p.paymentId = :id")
    Optional<PaymentDTO> findPaymentDTOById(@Param("id") Long id);

    /**
     * Retrieves all payments as DTOs with complete details.
     *
     * @return list of {@link PaymentDTO} objects
     */
    @Query("SELECT new com.parking.dto.PaymentDTO(" +
           "p.paymentId, b.billingId, t.transactionId, " +
           "p.amount, p.paymentMethod, CAST(p.status AS string), p.paymentTime, " +
           "v.vehicleNumber, v.vehicleType, " +
           "ps.slotNumber, pl.lotName, " +
           "t.entryTime, t.exitTime, t.duration) " +
           "FROM Payment p " +
           "LEFT JOIN p.billing b " +
           "LEFT JOIN b.transaction t " +
           "LEFT JOIN t.vehicle v " +
           "LEFT JOIN t.parkingSlot ps " +
           "LEFT JOIN ps.parkingLot pl")
    List<PaymentDTO> findAllPaymentDTOs();

    /**
     * Retrieves all payments made by a specific user as DTOs.
     *
     * @param user the user entity
     * @return list of {@link PaymentDTO} objects for the user
     */
    @Query("SELECT new com.parking.dto.PaymentDTO(" +
           "p.paymentId, b.billingId, t.transactionId, " +
           "p.amount, p.paymentMethod, CAST(p.status AS string), p.paymentTime, " +
           "v.vehicleNumber, v.vehicleType, " +
           "ps.slotNumber, pl.lotName, " +
           "t.entryTime, t.exitTime, t.duration) " +
           "FROM Payment p " +
           "LEFT JOIN p.billing b " +
           "LEFT JOIN b.transaction t " +
           "LEFT JOIN t.vehicle v " +
           "LEFT JOIN t.parkingSlot ps " +
           "LEFT JOIN ps.parkingLot pl " +
           "WHERE v.user = :user")
    List<PaymentDTO> findPaymentDTOsByUser(@Param("user") User user);
}