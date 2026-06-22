package com.parking.utils;

import com.parking.entity.*;
import com.parking.enums.*;
import java.time.LocalDateTime;

public class TestDataBuilder {

    public static User createCustomerUser(Long id) {
        return User.builder().userId(id).username("customer" + id).email("c" + id + "@test.com")
                .password("pass").role(Role.ROLE_CUSTOMER).status("ACTIVE")
                .createdDate(LocalDateTime.now()).build();
    }

    public static User createAdminUser() {
        return User.builder().userId(1L).username("admin").email("admin@test.com")
                .password("pass").role(Role.ROLE_ADMIN).status("ACTIVE")
                .createdDate(LocalDateTime.now()).build();
    }

    public static Vehicle createVehicle(Long id, String number, String type, User user) {
        return Vehicle.builder().vehicleId(id).vehicleNumber(number).vehicleType(type)
                .ownerName("Owner").mobileNumber("9999999999").user(user).build();
    }

    public static ParkingLot createLot(Long id, String name, int total, int car, int bike, int ev) {
        return ParkingLot.builder().lotId(id).lotName(name).location("Loc")
                .totalSlots(total).carSlots(car).bikeSlots(bike).evSlots(ev).build();
    }

    public static ParkingSlot createSlot(Long id, String number, SlotType type, SlotStatus status, ParkingLot lot) {
        return ParkingSlot.builder().slotId(id).slotNumber(number).slotType(type)
                .status(status).floorNumber(1).parkingLot(lot).build();
    }

    public static ParkingTransaction createTx(Long id, Vehicle v, ParkingSlot s, TransactionStatus st) {
        return ParkingTransaction.builder().transactionId(id).vehicle(v).parkingSlot(s)
                .entryTime(LocalDateTime.now().minusHours(2))
                .exitTime(st == TransactionStatus.COMPLETED ? LocalDateTime.now() : null)
                .duration(st == TransactionStatus.COMPLETED ? 2.0 : null).status(st).build();
    }

    public static Billing createBilling(Long id, ParkingTransaction tx, String paymentStatus) {
        return Billing.builder().billingId(id).transaction(tx).ratePerHour(50.0)
                .totalAmount(tx != null && tx.getDuration() != null ? tx.getDuration() * 50.0 : 0.0)
                .paymentStatus(paymentStatus).build();
    }

    public static Payment createPayment(Long id, Billing b, String method) {
        return Payment.builder().paymentId(id).billing(b).amount(b != null ? b.getTotalAmount() : 0.0)
                .paymentMethod(method).status(PaymentStatus.SUCCESS)
                .paymentTime(LocalDateTime.now()).build();
    }
}
