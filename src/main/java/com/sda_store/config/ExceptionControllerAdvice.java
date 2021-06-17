package com.sda_store.config;

import com.sda_store.exception.ResourceAlreadyPresentInDatabase;
import com.sda_store.exception.ResourceNotFoundInDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = {ResourceNotFoundInDatabase.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundInDatabase ex){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(value = {ResourceAlreadyPresentInDatabase.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage databaseException(ResourceAlreadyPresentInDatabase ex){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        return errorMessage;
    }
}
