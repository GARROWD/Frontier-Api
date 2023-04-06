package ru.frontierspb.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.frontierspb.util.messages.ExceptionsMessages;

import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> httpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return new ResponseEntity<>(Map.of("message.request.methodNotSupported", ExceptionsMessages.getMessage(
                "message.request.methodNotSupported") + " - " + exception.getMethod()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> missingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        return new ResponseEntity<>(Map.of("message.request.missingParameter", ExceptionsMessages.getMessage(
                "message.request.missingParameter") + " - " + exception.getParameterName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookingAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> bookingAlreadyExistsException(
            BookingAlreadyExistsException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerReferentException.class)
    public ResponseEntity<Map<String, String>> customerAlreadyHaveReferrerException(
            CustomerReferentException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AdminAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> adminAlreadyExistsException(
            AdminAlreadyExistsException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PointsInsufficientException.class)
    public ResponseEntity<Map<String, String>> pointsInsufficientException(
            PointsInsufficientException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionException.class)
    public ResponseEntity<Map<String, String>> sessionException(
            SessionException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PreviousPasswordsException.class)
    public ResponseEntity<Map<String, String>> previousPasswordsException(PreviousPasswordsException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> customerAlreadyExistsException(
            CustomerAlreadyExistsException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptionValidationException.class)
    public ResponseEntity<Map<String, String>> optionValidationException(OptionValidationException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerValidationException.class)
    public ResponseEntity<Map<String, String>> customerValidationException(
            CustomerValidationException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptionNotFoundException.class)
    public ResponseEntity<Map<String, String>> optionNotFoundException(OptionNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessages(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, String>> bookingNotFoundException(BookingNotFoundException exception) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, String>> customerNotFoundException(CustomerNotFoundException exception) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

