package com.sda_store.exception;

public class ResourceNotFoundInDatabase extends RuntimeException{

    public ResourceNotFoundInDatabase(String message) {
        super(message);
    }
}
