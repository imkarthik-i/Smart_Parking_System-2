package com.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingDTO {

    private Long billId;

    private Long transactionId;

    private Double amount;

    private Double tax;

    private Double totalAmount;

    private String paymentStatus;
}