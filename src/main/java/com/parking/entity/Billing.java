package com.parking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "billing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billingId;

    private Double ratePerHour;

    private Double totalAmount;

    private String paymentStatus; // PENDING / PAID

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private ParkingTransaction transaction;
}