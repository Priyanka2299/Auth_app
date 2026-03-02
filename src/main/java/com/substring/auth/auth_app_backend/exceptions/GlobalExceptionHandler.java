package com.substring.auth.auth_app_backend.exceptions;


import com.substring.auth.auth_app_backend.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice           //we can handle global exception, exceptions arising inside project
public class GlobalExceptionHandler {

    //resource not found exception :: method
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
//        exception.getCause();
//        exception.getMessage();
//        exception.getLocalizedMessage();
//        exception.printStackTrace();
        ErrorResponse internalServerError = new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception){
        ErrorResponse internalServerError = new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(internalServerError);
    }



}




//✅ 200 – OK
//Request successful
//
//✅ 201 – Created
//Used when creating a resource
//
//✅ 400 – Bad Request
//Client sent invalid data.
//
// ✅ 401 – Unauthorized
//User not authenticated.
//
// ✅ 403 – Forbidden
//User authenticated but not allowed.
// ✅ 404 – Not Found
//Resource does not exist.
//
// ✅ 500 – Internal Server Error
//Something broke in backend.
