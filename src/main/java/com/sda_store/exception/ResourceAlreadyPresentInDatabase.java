package com.sda_store.exception;

public class ResourceAlreadyPresentInDatabase extends RuntimeException{

    public ResourceAlreadyPresentInDatabase(String message) {
        super(message);
    }
}
