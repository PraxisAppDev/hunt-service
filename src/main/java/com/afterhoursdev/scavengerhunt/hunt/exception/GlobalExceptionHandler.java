package com.afterhoursdev.scavengerhunt.hunt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The GlobalHuntException class defines all the exceptions thrown by
 * any controller in the application.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@ControllerAdvice
public class GlobalExceptionHandler extends Exception {
  private static final long serialVersionUID = 1L;
  
  @ExceptionHandler(HuntServiceException.class)
  public ResponseEntity<ErrorMessage> handleHuntServiceException(HuntServiceException e) {
	  ErrorMessage errorMessage = new ErrorMessage.Builder() 
    		  .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
              .error("Internal Server Error")
              .message(e.getMessage())
              .build();
      return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException e) {
      ErrorMessage errorMessage = new ErrorMessage.Builder() 
    		  .httpStatusCode(HttpStatus.NOT_FOUND.value())
              .error("Resource Not Found")
              .message(e.getMessage())
              .build();
      return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
  }
  
  @ExceptionHandler(ResourceExistsException.class)
  public ResponseEntity<ErrorMessage> handleResourceExistsException(ResourceExistsException e) {
	  ErrorMessage errorMessage = new ErrorMessage.Builder() 
    		  .httpStatusCode(HttpStatus.CONFLICT.value())
              .error("Resource Exists")
              .message(e.getMessage())
              .build();
      return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
  }
  
  @ExceptionHandler(InvalidParameterException.class)
  public ResponseEntity<ErrorMessage> handleInvalidParameterException(InvalidParameterException e) {
	  ErrorMessage errorMessage = new ErrorMessage.Builder() 
    		  .httpStatusCode(HttpStatus.BAD_REQUEST.value())
              .error("Bad Request")
              .message(e.getMessage())
              .build();
      return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
   }
}