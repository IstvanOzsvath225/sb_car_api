package com.example.car.controller;

import com.example.car.entity.Car;
import com.example.car.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping("/createCar")
    public Car createCar() {
        return carService.createCar();
    }

    @PostMapping("/{carId}/start")
    public Car startCar(@PathVariable Long carId) {
        return carService.startCar(carId);
    }

    @PostMapping("/{carId}/stop")
    public Car stopCar(@PathVariable Long carId) {
        return carService.stopCar(carId);
    }

    @PostMapping("/{carId}/refuel")
    public Car refuelCar(@PathVariable Long carId, @RequestParam int amount) {
        return carService.refuelCar(carId, amount);
    }

    @GetMapping("/{carId}/checkFuelLevel")
    public int getFuelLevel(@PathVariable Long carId) {
        return carService.checkFuelLevel(carId);
    }

    @GetMapping("/{carId}/isEngineRunning")
    public boolean getIsEngineRunning(@PathVariable Long carId) {
        return carService.checkIsEngineRunning(carId);
    }
}
