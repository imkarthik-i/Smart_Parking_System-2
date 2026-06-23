package com.parking.service.impl;

import com.parking.entity.*;
import com.parking.enums.Role;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.*;
import com.parking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link UserService} for managing user operations.
 * <p>
 * Handles user CRUD with cascading cleanup of associated vehicles,
 * reservations, transactions, billing, and payments when a user
 * is deleted. Prevents deletion of the default admin account.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ReservationRepository reservationRepository;
    private final ParkingTransactionRepository transactionRepository;
    private final BillingRepository billingRepository;
    private final PaymentRepository paymentRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Cascades deletion to all associated entities: reservations,
     * transactions, billing records, and payments. Admin accounts
     * are protected from deletion.
     * </p>
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + id));

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new RuntimeException("Default admin account cannot be deleted");
        }

        if (user.getVehicles() != null) {
            for (Vehicle vehicle : user.getVehicles()) {
                if (vehicle.getReservations() != null) {
                    reservationRepository.deleteAll(vehicle.getReservations());
                }
                if (vehicle.getTransactions() != null) {
                    for (ParkingTransaction tx : vehicle.getTransactions()) {
                        if (tx.getBilling() != null) {
                            Billing bill = tx.getBilling();
                            paymentRepository.findByBilling(bill).ifPresent(paymentRepository::delete);
                            tx.setBilling(null);
                            billingRepository.delete(bill);
                        }
                    }
                    transactionRepository.deleteAll(vehicle.getTransactions());
                }
            }
        }

        userRepository.delete(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + id));
        user.setStatus("ACTIVE");
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + id));
        user.setStatus("INACTIVE");
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates the username and email fields only. Password changes
     * are handled through a separate authentication flow.
     * </p>
     */
    @Override
    public User updateUser(Long id, User updated) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + id));
        user.setUsername(updated.getUsername());
        user.setEmail(updated.getEmail());
        return userRepository.save(user);
    }
}
