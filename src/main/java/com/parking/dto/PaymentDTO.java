package com.parking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long billingId;
    private String paymentMethod; // UPI / CASH / CARD
}