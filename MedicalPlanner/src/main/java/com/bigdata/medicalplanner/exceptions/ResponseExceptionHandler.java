package com.bigdata.medicalplanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ValueNotFoundExceptions.class)
    public ResponseEntity<String> medicalPlanNotFoundException(ValueNotFoundExceptions exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(InvalidPayloadException.class)
    public ResponseEntity<String> invalidPayloadException(InvalidPayloadException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(JsonValidationFailureException.class)
    public ResponseEntity<String> jsonValidationFailureException(JsonValidationFailureException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(KeyAlreadyExistsException.class)
    public ResponseEntity<String> medicalExistsException(KeyAlreadyExistsException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(InvalidEtagException.class)
    public ResponseEntity<String> invalidEtagException(InvalidEtagException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(message);
    }
}
