package com.parking.service;

import com.parking.entity.*;
import com.parking.enums.*;
import com.parking.exception.*;
import com.parking.repository.*;
import com.parking.service.impl.*;
import com.parking.utils.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingFlowServiceTest {

    @Mock private VehicleRepository vehicleRepo;
    @Mock private ParkingSlotRepository slotRepo;
    @Mock private ParkingTransactionRepository txRepo;
    @Mock private ReservationRepository reservationRepo;
    @Mock private BillingRepository billingRepo;
    @Mock private PaymentRepository paymentRepo;

    private ParkingTransactionServiceImpl parkingService;
    private PaymentServiceImpl paymentService;

    private User user;
    private Vehicle vehicle;
    private ParkingLot lot;
    private ParkingSlot slot;

    @BeforeEach
    void setUp() {
        parkingService = new ParkingTransactionServiceImpl(vehicleRepo, slotRepo, txRepo, reservationRepo, billingRepo);
        paymentService = new PaymentServiceImpl(billingRepo, paymentRepo);
        user = TestDataBuilder.createCustomerUser(2L);
        vehicle = TestDataBuilder.createVehicle(1L, "KA-01-1234", "CAR", user);
        lot = TestDataBuilder.createLot(1L, "Main Lot", 10, 5, 3, 2);
        slot = TestDataBuilder.createSlot(1L, "CAR-001", SlotType.CAR, SlotStatus.AVAILABLE, lot);
    }

    @Test
    void fullParkingFlow_ShouldSucceed() {
        when(vehicleRepo.findByVehicleNumber("KA-01-1234")).thenReturn(Optional.of(vehicle));
        when(slotRepo.findById(1L)).thenReturn(Optional.of(slot));
        when(slotRepo.save(any(ParkingSlot.class))).thenReturn(slot);
        when(reservationRepo.findAll()).thenReturn(Collections.emptyList());

        ParkingTransaction savedTx = ParkingTransaction.builder()
                .transactionId(1L)
                .vehicle(vehicle)
                .parkingSlot(slot)
                .entryTime(java.time.LocalDateTime.now())
                .status(TransactionStatus.ACTIVE)
                .build();
        when(txRepo.save(any(ParkingTransaction.class))).thenReturn(savedTx);

        ParkingTransaction tx = parkingService.vehicleEntry("KA-01-1234", 1L);

        assertThat(tx.getTransactionId()).isEqualTo(1L);
        assertThat(tx.getStatus()).isEqualTo(TransactionStatus.ACTIVE);
        assertThat(slot.getStatus()).isEqualTo(SlotStatus.OCCUPIED);
        verify(vehicleRepo).findByVehicleNumber("KA-01-1234");

        ParkingTransaction activeTx = TestDataBuilder.createTx(1L, vehicle, slot, TransactionStatus.ACTIVE);
        activeTx.setEntryTime(java.time.LocalDateTime.now().minusHours(3));

        when(txRepo.findById(1L)).thenReturn(Optional.of(activeTx));
        when(txRepo.save(any(ParkingTransaction.class))).thenAnswer(i -> i.getArgument(0));
        when(billingRepo.save(any(Billing.class))).thenAnswer(i -> i.getArgument(0));

        ParkingTransaction completed = parkingService.vehicleExit(1L);

        assertThat(completed.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(completed.getDuration()).isGreaterThan(0);
        assertThat(slot.getStatus()).isEqualTo(SlotStatus.AVAILABLE);

        ArgumentCaptor<Billing> billCaptor = ArgumentCaptor.forClass(Billing.class);
        verify(billingRepo).save(billCaptor.capture());
        Billing bill = billCaptor.getValue();
        assertThat(bill.getPaymentStatus()).isEqualTo("PENDING");
        assertThat(bill.getTotalAmount()).isGreaterThan(0);
        assertThat(bill.getRatePerHour()).isEqualTo(50.0);

        when(billingRepo.findById(1L)).thenReturn(Optional.of(bill));
        when(billingRepo.save(any(Billing.class))).thenReturn(bill);
        when(paymentRepo.save(any(Payment.class))).thenAnswer(i -> {
            Payment p = i.getArgument(0);
            p.setPaymentId(1L);
            return p;
        });

        com.parking.dto.PaymentDTO paymentDTO = new com.parking.dto.PaymentDTO();
        paymentDTO.setPaymentId(1L);
        paymentDTO.setPaymentMethod("UPI");
        paymentDTO.setStatus("SUCCESS");
        when(paymentRepo.findPaymentDTOById(anyLong())).thenReturn(Optional.of(paymentDTO));

        com.parking.dto.PaymentDTO result = paymentService.makePayment(1L, "UPI");

        assertThat(result.getPaymentMethod()).isEqualTo("UPI");
        assertThat(result.getStatus()).isEqualTo("SUCCESS");
        assertThat(bill.getPaymentStatus()).isEqualTo("PAID");
    }

    @Test
    void vehicleEntry_ShouldThrowWhenSlotOccupied() {
        slot.setStatus(SlotStatus.OCCUPIED);
        when(vehicleRepo.findByVehicleNumber("KA-01-1234")).thenReturn(Optional.of(vehicle));
        when(slotRepo.findById(1L)).thenReturn(Optional.of(slot));

        assertThatThrownBy(() -> parkingService.vehicleEntry("KA-01-1234", 1L))
                .isInstanceOf(SlotNotAvailableException.class);
    }

    @Test
    void vehicleEntry_ShouldThrowWhenVehicleNotFound() {
        when(vehicleRepo.findByVehicleNumber("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parkingService.vehicleEntry("UNKNOWN", 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void vehicleExit_ShouldThrowWhenTxNotFound() {
        when(txRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parkingService.vehicleExit(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void makePayment_ShouldThrowWhenAlreadyPaid() {
        Billing bill = TestDataBuilder.createBilling(1L, null, "PAID");
        when(billingRepo.findById(1L)).thenReturn(Optional.of(bill));

        assertThatThrownBy(() -> paymentService.makePayment(1L, "UPI"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Already paid");
    }
}
