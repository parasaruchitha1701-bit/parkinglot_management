package com.parkinglot;

import com.parkinglot.model.Vehicle;
import com.parkinglot.model.VehicleType;
import com.parkinglot.service.ParkingLotService;
import com.parkinglot.storage.ParkingLotStorage;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n;
        while (true) {
            System.out.print("Enter total number of parking slots (N): ");
            String input = scanner.nextLine().trim();
            try {
                n = Integer.parseInt(input);
                if (n <= 0) {
                    System.out.println("N must be a positive integer.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }

        ParkingLotStorage storage = new ParkingLotStorage("data/parking_state.csv");
        ParkingLotService service = new ParkingLotService(n, storage);

        while (true) {
            System.out.println("\n========= MENU =========");
            System.out.println("1) Park Vehicle");
            System.out.println("2) Remove Vehicle");
            System.out.println("3) Display Status Summary");
            System.out.println("4) Display Detailed Slots");
            System.out.println("5) Exit");
            System.out.println("========================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter Vehicle ID (ex: KA01AB1234): ");
                    String vehicleId = scanner.nextLine().trim();
                    if (vehicleId.isEmpty()) {
                        System.out.println("Vehicle ID cannot be empty.");
                        break;
                    }
                    if (!vehicleId.matches("[A-Za-z0-9-]+")) {
                        System.out.println("Vehicle ID must be alphanumeric.");
                        break;
                    }
                    System.out.print("Enter Vehicle Type (SMALL/LARGE/OVERSIZE): ");
                    String typeInput = scanner.nextLine().trim().toUpperCase();

                    try {
                        VehicleType vehicleType = VehicleType.valueOf(typeInput);
                        service.parkVehicle(new Vehicle(vehicleId, vehicleType));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid Vehicle type. Use SMALL, LARGE, or OVERSIZE.");
                    }
                    
                    break;

                case "2":
                    System.out.print("Enter Vehicle ID to Remove: ");
                    String removeId = scanner.nextLine().trim();
                    service.removeVehicle(removeId);
                    break;

                case "3":
                    service.displayStatus();
                    break;

                case "4":
                    service.displayDetailedSlots();
                    break;

                case "5":
                    System.out.println("Exiting... state saved to data/parking_state.csv");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please choose 1-5.");
            }
        }
    }
}
