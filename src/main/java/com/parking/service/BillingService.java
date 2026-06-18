package com.parking.service;

import com.parking.entity.Billing;
import com.parking.entity.User;
import java.util.List;

public interface BillingService {

    Billing generateBill(Long transactionId);

    Billing getBill(Long billingId);

    List<Billing> getAllBills();

    List<Billing> getBillsByUser(User user);
}