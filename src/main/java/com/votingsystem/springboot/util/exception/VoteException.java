package com.votingsystem.springboot.util.exception;

public class VoteException extends RuntimeException {
    public VoteException(String message) {
        super(message);
    }
}