# Parking Lot Management Application

# 1. Overview
This project implements **Parking Lot Management Application**.

It models a small parking lot with 'N slots' - (N provided by the user at runtime), divides the slots into sizes (**SMALL, LARGE, OVERSIZE**), and supports:
- Vehicle **entry** (park)
- Vehicle **exit** (remove)
- Parking lot **status display** (summary + detailed slots)

The application is implemented as a **command-line (CLI) Java application**.  

For storage, the application uses a **CSV file** (`data/parking_state.csv`) to persist state.  

# 2. Requirements Covered
Functional Requirements
- User provides total slots (N)  
Slots divided into:
   - SMALL (small/compact cars)
   - LARGE (full-size cars)
   - OVERSIZE (SUV/truck)

- Manage available and filled slots  
Support:
   - Vehicle entry
   - Vehicle exit
   - Status display

# 3. Technical Requirements
Language: Java 

Interface: Command line (CLI) 

Data storage: File-based (CSV)

# 4. Slot Distribution Strategy
The parking lot is divided using this ratio:
- **40%** SMALL
- **40%** LARGE
- **20%** OVERSIZE

Example:
If N = 10 → SMALL=4, LARGE=4, OVERSIZE=2 

Vehicles are parked only in a slot of the matching type.

# 5. Project Structure

```
parking-lot/
├── pom.xml
├── data/
│   └── parking_state.csv (created after running)
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── parkinglot/
│                   ├── Main.java
│                   ├── model/
│                   │   ├── ParkingSlot.java
│                   │   ├── SlotType.java
│                   │   ├── Vehicle.java
│                   │   └── VehicleType.java
│                   ├── service/
│                   │   └── ParkingLotService.java
│                   └── storage/
│                       └── ParkingLotStorage.java
└── README.md
```

# 6. Key Files and Responsibilities
- **Main.java**
  - CLI menu and user input handling
- **model/**
  - `Vehicle`, `ParkingSlot`, and enums `VehicleType`, `SlotType`
- **service/ParkingLotService.java**
  - Core business logic: initialize lot, park/remove vehicles, status display
- **storage/ParkingLotStorage.java**
  - CSV save/load functionality for persistence
 
 # Run the Application using any IDE 
  STS / IntelliJ / VS Code
