package com.parking.service;

import com.parking.entity.Reservation;
import com.parking.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    Reservation createReservation(Long vehicleId, Long slotId);

    Reservation createReservation(Long vehicleId, Long slotId, LocalDateTime startTime, LocalDateTime endTime);

    Reservation cancelReservation(Long reservationId);

    Reservation getReservationById(Long id);

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByUser(User user);
}