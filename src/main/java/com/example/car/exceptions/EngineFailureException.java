package com.example.car.exceptions;

import com.example.car.messages.MessageCollector;

public class EngineFailureException extends RuntimeException{

    public EngineFailureException() {
        super(MessageCollector.ENGINE_FAILURE);
    }
}
