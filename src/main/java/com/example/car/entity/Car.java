package com.example.car.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engine", referencedColumnName = "id")
    private Engine engine;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fuel_tank", referencedColumnName = "id")
    private FuelTank fuelTank;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dashboard", referencedColumnName = "id")
    private Dashboard dashboard;
}
