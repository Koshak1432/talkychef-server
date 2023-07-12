package voicerecipeserver.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import voicerecipeserver.model.dto.Error;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.InvalidMediaTypeException;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.security.SignatureException;




@RestControllerAdvice
@CrossOrigin(maxAge = 1440)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new Error().code(400).message("Validation Failed"), HttpStatus.BAD_REQUEST);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new Error().code(400).message("Validation Failed"), HttpStatus.BAD_REQUEST);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Error().code(400).message("Validation Failed"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(Exception e) {
        return new ResponseEntity<>(new Error().code(404).message(e.getMessage()), HttpStatus.NOT_FOUND);
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
        return new ResponseEntity<>(new Error().code(400).message("Authorization failed: " + message), HttpStatus.BAD_REQUEST);
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


    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);
}