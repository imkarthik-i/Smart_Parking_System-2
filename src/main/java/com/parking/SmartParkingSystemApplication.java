package com.parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Smart Parking Management System.
 * <p>
 * Bootstraps the Spring Boot application, initializing all
 * components including controllers, services, repositories,
 * security configuration, and data initializers.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@SpringBootApplication
public class SmartParkingSystemApplication {

	/**
	 * Launches the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(SmartParkingSystemApplication.class, args);
	}
}
