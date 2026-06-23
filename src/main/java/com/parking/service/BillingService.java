package com.parking.service;

import com.parking.dto.BillingDTO;
import com.parking.entity.Billing;
import com.parking.entity.User;
import java.util.List;

/**
 * Service interface for managing billing operations.
 * <p>
 * Defines business logic for bill generation based on
 * parking transactions, and retrieving billing information
 * with full transaction details.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface BillingService {

    /**
     * Generates a billing record for a completed parking transaction.
     *
     * @param transactionId the transaction identifier
     * @return the billing DTO with computed charges
     * @throws com.parking.exception.ResourceNotFoundException if transaction not found
     */
    BillingDTO generateBill(Long transactionId);

    /**
     * Retrieves the raw billing entity by its identifier.
     *
     * @param billingId the billing identifier
     * @return the billing entity
     * @throws com.parking.exception.ResourceNotFoundException if billing not found
     */
    Billing getBillEntity(Long billingId);

    /**
     * Retrieves a billing record as a DTO with full details.
     *
     * @param billingId the billing identifier
     * @return the billing DTO
     * @throws com.parking.exception.ResourceNotFoundException if billing not found
     */
    BillingDTO getBill(Long billingId);

    /**
     * Retrieves all billing records as DTOs.
     *
     * @return list of billing DTOs
     */
    List<BillingDTO> getAllBills();

    /**
     * Retrieves all billing records for a specific user.
     *
     * @param user the user entity
     * @return list of billing DTOs for the user
     */
    List<BillingDTO> getBillsByUser(User user);
}