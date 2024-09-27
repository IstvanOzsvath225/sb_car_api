package com.example.car.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class FuelTank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fuel_level")
    private int fuelLevel;

    private static final int DEFAULT_FUEL_LEVEL = 50;

    public FuelTank(){
        this.fuelLevel = DEFAULT_FUEL_LEVEL;
    }
}
