package com.parking.service;

import com.parking.dto.PaymentDTO;
import com.parking.entity.Payment;
import com.parking.entity.User;
import java.util.List;

/**
 * Service interface for managing payment operations.
 * <p>
 * Defines business logic for processing payments against
 * billing records, and retrieving payment information
 * with full transaction details.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface PaymentService {

    /**
     * Processes a payment for the specified billing record.
     *
     * @param billingId the billing record identifier
     * @param method    the payment method (e.g., CASH, UPI, CARD)
     * @return the payment DTO with full details
     * @throws com.parking.exception.ResourceNotFoundException if billing record not found
     */
    PaymentDTO makePayment(Long billingId, String method);

    /**
     * Retrieves the raw payment entity by its identifier.
     *
     * @param paymentId the payment identifier
     * @return the payment entity
     * @throws com.parking.exception.ResourceNotFoundException if the payment is not found
     */
    Payment getPaymentEntity(Long paymentId);

    /**
     * Retrieves a payment as a DTO with full details.
     *
     * @param paymentId the payment identifier
     * @return the payment DTO
     * @throws com.parking.exception.ResourceNotFoundException if the payment is not found
     */
    PaymentDTO getPayment(Long paymentId);

    /**
     * Retrieves all payments as DTOs.
     *
     * @return list of payment DTOs
     */
    List<PaymentDTO> getAllPayments();

    /**
     * Retrieves all payments made by a specific user.
     *
     * @param user the user entity
     * @return list of payment DTOs for the user
     */
    List<PaymentDTO> getPaymentsByUser(User user);
}