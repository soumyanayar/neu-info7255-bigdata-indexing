package com.bigdata.medicalplanner.filters;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@ResponseStatus
public class ExceptionHandlingFilters extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        String detail= ex.getLocalizedMessage();
        return new ResponseEntity<Object>(new JSONObject().put("Server Error", detail).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<Object> handleNullPointerExceptions(Exception ex, WebRequest request) {
        String detail= ex.getLocalizedMessage();
        return new ResponseEntity<Object>(new JSONObject().put("Null Pointer Exception", detail).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        return new ResponseEntity<Object>(new JSONObject().put("Validation Error", details).toString(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail= ex.getLocalizedMessage();
        return new ResponseEntity<Object>(new JSONObject().put("Validation Error", detail).toString(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(org.json.JSONException.class)
    public final ResponseEntity<Object> handleJSONExceptions(Exception ex, WebRequest request) {
        String detail= ex.getLocalizedMessage();
        return new ResponseEntity<Object>(new JSONObject().put("JSON Error", detail).toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException.class)
    public final ResponseEntity<Object> handleEmptyResultDataAccessExceptions(Exception ex, WebRequest request) {
        String detail= ex.getLocalizedMessage();
        return new ResponseEntity<Object>(new JSONObject().put("Object Not Found", detail).toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(Exception ex, WebRequest request) {
        String detail= ex.getLocalizedMessage();
        return new ResponseEntity<Object>(new JSONObject().put("Path Variable Not Found", detail).toString(), HttpStatus.NOT_FOUND);
    }

    public final ResponseEntity<Object> handleInvalidJsonpParameterException(Exception ex, WebRequest request) {
        String detail= ex.getLocalizedMessage();
        return new ResponseEntity<Object>(new JSONObject().put("Invalid Etag", detail).toString(), HttpStatus.BAD_REQUEST);
    }
}
