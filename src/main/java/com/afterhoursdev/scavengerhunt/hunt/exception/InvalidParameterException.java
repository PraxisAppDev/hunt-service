package com.afterhoursdev.scavengerhunt.hunt.exception;

/**
 * The ResourceNotFound class is a wrapper class for all ResourceNotFound
 * exceptions.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/
public class InvalidParameterException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public InvalidParameterException(String errorMessage) {
	  super(errorMessage);
  }
}