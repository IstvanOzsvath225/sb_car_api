package com.example.car.service;

import com.example.car.entity.Car;
import com.example.car.entity.Dashboard;
import com.example.car.entity.Engine;
import com.example.car.entity.FuelTank;
import com.example.car.exceptions.CarNotFoundException;
import com.example.car.messages.MessageCollector;
import com.example.car.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CarServiceTest {

    @MockBean
    private CarRepository carRepository;

    @Mock
    private Logger logger = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private CarService carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void createCar_shouldSaveNewCar() {
        Car car = createTestCar();
        // Mock the saveAndFlush behavior to return the car with an ID
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);

        // Act
        Car result = carService.createCar();

        // Assert
        assertNotNull(result);  // Check that a car is returned
        assertEquals(1L, result.getId());  // Ensure the ID is correctly set

        // Verify that the logger logged the car creation message

        // Verify that the repository's saveAndFlush method was called
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    void startCar_whenCarExists_shouldStartCar() {
        // Arrange
        Long carId = 1L;
        Car car = createTestCar();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Act
        carService.startCar(carId);

        // Assert
        assertTrue(car.getEngine().isRunning());
        verify(carRepository, times(1)).findById(carId);
        verify(logger, never()).error(anyString());
        assertEquals(MessageCollector.CAR_STARTED, car.getDashboard().getCurrentMessage());
    }

    @Test
    void startCar_whenCarDoesNotExist_shouldLogAndThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CarNotFoundException.class, () -> carService.startCar(carId));
    }

    @Test
    void stopCar_whenCarExists_shouldStopCar() {
        // Arrange
        Long carId = 1L;
        Car car = createTestCar(); // Use a mock Car object
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Act
        carService.stopCar(carId);

        // Assert
        assertFalse(car.getEngine().isRunning());
        verify(carRepository, times(1)).findById(carId);
        verify(logger, never()).error(anyString());
        assertEquals(MessageCollector.CAR_STOPPED, car.getDashboard().getCurrentMessage());
    }

    @Test
    void stopCar_whenCarDoesNotExist_shouldLogAndThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CarNotFoundException.class, () -> carService.stopCar(carId));
    }

    @Test
    void refuelCar_whenCarExists_shouldRefuelCar() {
        // Arrange
        Long carId = 1L;
        int fuelAmount = 20;
        Car car = createTestCar(); // Use a mock Car object
        int startingFuelAmount = car.getFuelTank().getFuelLevel();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Act
        carService.refuelCar(carId, fuelAmount);

        // Assert
        assertEquals(car.getFuelTank().getFuelLevel() - fuelAmount, startingFuelAmount);
        verify(carRepository, times(1)).findById(carId);
        verify(logger, never()).error(anyString());
        assertEquals(MessageCollector.carRefueled(fuelAmount, car.getFuelTank().getFuelLevel()),car.getDashboard().getCurrentMessage());
    }

    @Test
    void refuelCar_whenCarDoesNotExist_shouldLogAndThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;
        int fuelAmount = 20;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CarNotFoundException.class, () -> carService.refuelCar(carId, fuelAmount));
    }

    @Test
    void checkIsEngineRunning_whenEngineIsRunning_shouldReturnTrueAndLog() {
        // Arrange
        Long carId = 1L;
        Car car = createTestCar();
        car.getEngine().setRunning(true);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Act
        boolean isEngineRunning = carService.checkIsEngineRunning(carId);

        // Assert
        assertTrue(isEngineRunning);
        verify(logger, never()).error(anyString());
        assertEquals(MessageCollector.ENGINE_IS_RUNNING,car.getDashboard().getCurrentMessage());
    }

    @Test
    void checkIsEngineRunning_whenEngineIsNotRunning_shouldReturnFalseAndLog() {
        // Arrange
        Long carId = 1L;
        Car car = createTestCar();
        car.getEngine().setRunning(false);


        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Act
        boolean isEngineRunning = carService.checkIsEngineRunning(carId);

        // Assert
        assertFalse(isEngineRunning);
        verify(logger, never()).error(anyString());
        assertEquals(MessageCollector.ENGINE_IS_NOT_RUNNING,car.getDashboard().getCurrentMessage());
    }

    @Test
    void checkIsEngineRunning_whenCarDoesNotExist_shouldThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CarNotFoundException.class, () -> carService.checkIsEngineRunning(carId));
}

    @Test
    void checkFuelLevel_whenCarExists_shouldReturnFuelLevelAndLog() {
        // Arrange
        Long carId = 1L;
        Car car = createTestCar();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Act
        int fuelLevel = carService.checkFuelLevel(carId);

        // Assert
        assertEquals(50, fuelLevel);
        assertEquals(MessageCollector.carFuelLevel(carId, 50), car.getDashboard().getCurrentMessage());
    }

    @Test
    void checkFuelLevel_whenCarDoesNotExist_shouldThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act & Assert
       assertThrows(CarNotFoundException.class, () -> carService.checkFuelLevel(carId));
    }

    private Car createTestCar(){
        Car car = new Car();
        car.setId(1L);
        car.setEngine(new Engine());
        car.setFuelTank(new FuelTank());
        car.setDashboard(new Dashboard());
        return car;
    }

}
