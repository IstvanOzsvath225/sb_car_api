package com.example.car.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_message")
    private String currentMessage;

    public void displayMessage() {
        System.out.println(currentMessage);
    }
}
