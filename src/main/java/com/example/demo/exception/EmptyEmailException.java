package com.example.demo.exception;

public class EmptyEmailException extends Exception {
    public EmptyEmailException(String message) {
        super(message);
    }
}
