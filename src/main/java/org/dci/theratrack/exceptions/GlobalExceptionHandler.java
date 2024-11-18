package org.dci.theratrack.exceptions;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  // Handle specific exceptions like EntityNotFoundException
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", "Entity not found");
    body.put("details", ex.getMessage());
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  // Handle database-related exceptions (e.g., from PostgreSQL)
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", "Database error");
    body.put("details", ex.getMostSpecificCause().getMessage());
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Handle validation errors from @Valid annotation
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  // Handle generic exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGlobalException(Exception ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", "Unexpected error");
    body.put("details", ex.getMessage());
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
