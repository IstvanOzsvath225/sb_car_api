package com.example.car.service;

import com.example.car.entity.Car;
import com.example.car.entity.Dashboard;
import com.example.car.entity.Engine;
import com.example.car.entity.FuelTank;
import com.example.car.exceptions.CarNotFoundException;
import com.example.car.exceptions.EngineFailureException;
import com.example.car.exceptions.FuelEmptyException;
import com.example.car.messages.MessageCollector;
import com.example.car.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    private final CarRepository carRepository;

    @Transactional
    public Car createCar() {
        Car car = new Car();
        car.setEngine(new Engine());
        car.setFuelTank(new FuelTank());
        car.setDashboard(new Dashboard());
        Car result= carRepository.saveAndFlush(car);
        logger.info(MessageCollector.carCreated(car.getId()));
        return result;
    }

    /**
     * Starts the car by checking engine and fuel tank availability.
     * If conditions are met, the car starts, fuel is consumed, and status is displayed.
     */
    @Transactional
    public Car startCar(Long carId) throws EngineFailureException, FuelEmptyException {
        Car car = getCarById(carId);

        // Handle the business logic for starting the car
        Engine engine = car.getEngine();
        FuelTank fuelTank = car.getFuelTank();

        if (!canStartEngine()) {
            logger.error(MessageCollector.engineFailureDetails(carId, engine.getId()));
            throw new EngineFailureException();
        }

        if (!hasSufficientFuel(fuelTank)) {
            logger.error(MessageCollector.fuelFailureDetails(carId, fuelTank.getId(), fuelTank.getFuelLevel()));
            throw new FuelEmptyException();
        }

        // Mark engine as running and consume fuel
        engine.setRunning(true);
        fuelTank.setFuelLevel(fuelTank.getFuelLevel() - 5);  // Consumes 5 liters per start

        Dashboard dashboard = car.getDashboard();
        dashboard.setCurrentMessage(MessageCollector.CAR_STARTED);
        dashboard.displayMessage();
        logger.info(MessageCollector.carStarted(carId, fuelTank.getFuelLevel()));
        return car;
    }

    /**
     * Stops the car by turning off the engine.
     */
    @Transactional
    public Car stopCar(Long carId) {
        Car car = getCarById(carId);

        Engine engine = car.getEngine();
        engine.setRunning(false);  // Stop the engine
        Dashboard dashboard = car.getDashboard();
        dashboard.setCurrentMessage(MessageCollector.CAR_STOPPED);
        dashboard.displayMessage();
        logger.info(MessageCollector.carStopped(carId, car.getFuelTank().getFuelLevel()));

        return car;
    }

    /**
     * Refuels the car's fuel tank by adding a specified amount of fuel.
     */
    @Transactional
    public Car refuelCar(Long carId, int amount) {
        Car car = getCarById(carId);

        FuelTank fuelTank = car.getFuelTank();
        fuelTank.setFuelLevel(fuelTank.getFuelLevel() + amount);  // Increase fuel level
        Dashboard dashboard = car.getDashboard();
        int currentFuelLevel = fuelTank.getFuelLevel();
        dashboard.setCurrentMessage(MessageCollector.carRefueled(amount, currentFuelLevel));
        dashboard.displayMessage();
        logger.info(MessageCollector.carRefueledDetailed(carId, fuelTank.getId(), amount, currentFuelLevel));
        return car;
    }

    /**
     * Displays the current fuel level of the car.
     */
    @Transactional
    public int checkFuelLevel(Long carId) {
        Car car = getCarById(carId);
        int fuelLevel = car.getFuelTank().getFuelLevel();
        Dashboard dashboard = car.getDashboard();
        dashboard.setCurrentMessage(MessageCollector.carFuelLevel(carId, fuelLevel));
        dashboard.displayMessage();
        logger.info(MessageCollector.carFuelLevelDetailed(carId, fuelLevel, car.getFuelTank().getId()));
        return fuelLevel;
    }

    @Transactional
    public boolean checkIsEngineRunning(Long carId) {
        Car car = getCarById(carId);
        boolean isEngineRunning = car.getEngine().isRunning();
        Dashboard dashboard = car.getDashboard();
        dashboard.setCurrentMessage(isEngineRunning ? MessageCollector.ENGINE_IS_RUNNING : MessageCollector.ENGINE_IS_NOT_RUNNING);
        dashboard.displayMessage();
        logger.info(MessageCollector.isEngineRunning(carId, car.getEngine().getId(), isEngineRunning));
        return isEngineRunning;
    }

    // Helper method to check if the engine can start
    private boolean canStartEngine() {
        return Math.random() <= 0.9;  // 90% chance the engine will start successfully
    }

    // Helper method to check if there is sufficient fuel
    private boolean hasSufficientFuel(FuelTank fuelTank) {
        return fuelTank.getFuelLevel() >= 5;  // Minimum 5 liters of fuel required to start the car
    }

    protected Car getCarById(Long carId) {

        return carRepository.findById(carId)
                .orElseThrow(() -> {
                    logger.error(MessageCollector.carNotFoundDetailed(carId));
                    throw new CarNotFoundException();
                });
    }
}