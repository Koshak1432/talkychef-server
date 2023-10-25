package voicerecipeserver.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.persistence.TransactionRequiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import voicerecipeserver.model.dto.Error;
import voicerecipeserver.model.exceptions.*;

import jakarta.validation.ConstraintViolationException;


@RestControllerAdvice
@CrossOrigin(maxAge = 1440)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new Error().code(400).message("Validation failed, argument not valid"), HttpStatus.BAD_REQUEST);
    }

    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new Error().code(400).message("Validation failed, http message not readable"), HttpStatus.BAD_REQUEST);
    }

    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Error().code(400).message("Validation failed, type mismatch"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(Exception e) {
        return new ResponseEntity<>(new Error().code(404).message(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ClassCastException.class})
    protected ResponseEntity<Object> handleClassCastException(Exception e) {
        return new ResponseEntity<>(new Error().code(404).message(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class, InvalidMediaTypeException.class})
    protected ResponseEntity<Object> handleBadRequest(Exception e) {
        return new ResponseEntity<>(new Error().code(400).message(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleValidationFailed(ConstraintViolationException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(new Error().code(400).message("Validation failed: " + message.substring(message.indexOf(":"))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthException.class})
    protected ResponseEntity<Object> handleAuthException(Exception e) {
        String message = e.getMessage();
        return new ResponseEntity<>(new Error().code(400).message("Auth failed: " + message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(new Error().code(400).message("Saving to db failed: " + message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TransactionRequiredException.class})
    protected ResponseEntity<Object> handleTransactionRequiredException(TransactionRequiredException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(new Error().code(400).message("Deleting from db failed: " + message), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler({ExpiredJwtException.class})
    protected ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e) {
        String message = e.getMessage();
        logger.error("Invalid token: {}", message, e);
        return new ResponseEntity<>(new Error().code(400).message("Invalid token: " + message), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({MalformedJwtException.class})
    protected ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException e) {
        String message = e.getMessage();
        logger.error("Invalid token: {}", message, e);
        return new ResponseEntity<>(new Error().code(400).message("Invalid token: " + message), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({UnsupportedJwtException.class})
    protected ResponseEntity<Object> handleUnsupportedJwtException(UnsupportedJwtException e) {
        String message = e.getMessage();
        logger.error("Invalid token: {}", message, e);
        return new ResponseEntity<>(new Error().code(400).message("Invalid token: " + message), HttpStatus.UNAUTHORIZED);
    }




}