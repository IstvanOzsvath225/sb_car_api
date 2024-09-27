package com.example.car.exceptions;

import com.example.car.messages.MessageCollector;

public class FuelEmptyException extends RuntimeException{

    public FuelEmptyException() {
        super(MessageCollector.FUEL_EMPTY);
    }
}
