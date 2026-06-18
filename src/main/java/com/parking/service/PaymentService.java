package com.parking.service;

import com.parking.entity.Payment;
import com.parking.entity.User;
import java.util.List;

public interface PaymentService {

    Payment makePayment(Long billingId, String method);

    Payment getPayment(Long paymentId);

    List<Payment> getAllPayments();

    List<Payment> getPaymentsByUser(User user);
}