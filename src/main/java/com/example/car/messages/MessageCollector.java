package com.example.car.messages;

import com.example.car.exceptions.CarNotFoundException;
import com.example.car.exceptions.EngineFailureException;
import com.example.car.exceptions.FuelEmptyException;

import java.text.MessageFormat;

public class MessageCollector {

    public static final String CAR_STARTED = "Car started successfully.";
    public static final String CAR_STOPPED = "Car stopped.";
    public static final String ENGINE_FAILURE = "Failed to start the engine. Please check the engine.";
    public static final String FUEL_EMPTY = "Not enough fuel to start the car.";
    public static final String ENGINE_IS_RUNNING = "Engine is running.";
    public static final String ENGINE_IS_NOT_RUNNING = "Engine is not running.";
    public static final String CAR_NOT_FOUND = "Car not found";

    // Method for dynamic messages (using String.format)
    public static String carRefueled(int amount, int currentFuel) {
        return MessageFormat.format("Car has been refueled with {0} liters of fuel. Current fuel level is {1} liters", amount, currentFuel);
    }

    public static String carRefueledDetailed(Long carId, Long fuelTankId,int amount, int currentFuel) {
        return MessageFormat.format("Fueltank with id {0} from car with {1} ID has been refueled with {2} liters of fuel. Current fuel level is {3} liters",fuelTankId, carId, amount, currentFuel);
    }

    public static String carCreated(Long carId) {
        return String.format("Car with " + carId + " as ID created successfully.");
    }

    public static String carStarted(Long carId, int fuelLevel) {
        return MessageFormat.format("Car with the id {0} started successfully. Remaining fuel: {1} liters.", carId, fuelLevel);
    }

    public static String carStopped(Long carId, int fuelLevel) {
        return MessageFormat.format("Car with the id {0} stopped. Remaining fuel: {1} liters.", carId, fuelLevel);
    }

    public static String carFuelLevel(Long carId, int fuelLevel) {
        return MessageFormat.format("The car {0} has {1} liters of fuel remaining.", carId, fuelLevel);
    }
    public static String carFuelLevelDetailed(Long carId, int fuelLevel, Long fuelTankId) {
        return MessageFormat.format("The car {0} has {1} liters of fuel remaining in fuelTank with id of {2}.", carId, fuelLevel, fuelTankId);
    }

    public static String isEngineRunning(Long carId, Long engineId, boolean isRunning){
        String engineStatus = isRunning? "is running." : "is not running.";
        return MessageFormat.format("Engine with {0} ID from Car with {1} ID {2}",engineId, carId, engineStatus);
    }

    public static String engineFailureDetails(Long carId, Long engineId) {
        String errorDetails = MessageFormat.format("Error with engine with ID of {0} from car with id of {1}.", carId, engineId);
        return errorMessage(EngineFailureException.class.getName(), errorDetails);
    }

    public static String fuelFailureDetails(Long carId, Long fuelTankId, int fuelLevel) {
        String errorDetails = MessageFormat.format("Error with fuelTank with ID of {0} from car with id of {1}.\n" +
                "Insufficient fuel: Current fuel level is {2} liters", fuelTankId, carId, fuelLevel);
        return errorMessage(FuelEmptyException.class.getName(), errorDetails);
    }

    public static final String carNotFoundDetailed(Long carId){
        String errorDetails = MessageFormat.format("Car with {0} not found.", carId);
        return errorMessage(CarNotFoundException.class.getName(), errorDetails);
    }

    // Utility method to handle exceptions and format error messages
    public static String errorMessage(String errorClass, String errorDetail) {
        return MessageFormat.format("Error: {0}. Details: {1}.", errorClass, errorDetail);
    }
}