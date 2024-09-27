package com.example.car.exceptions;

import com.example.car.messages.MessageCollector;

public class CarNotFoundException extends RuntimeException{

    public CarNotFoundException() {
        super(MessageCollector.CAR_NOT_FOUND);
    }
}
