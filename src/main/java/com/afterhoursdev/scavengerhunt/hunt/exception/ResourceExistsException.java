package com.afterhoursdev.scavengerhunt.hunt.exception;

/**
 * The ResourceExists class is a wrapper class for all ResourceExists
 * exceptions.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/
public class ResourceExistsException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public ResourceExistsException(String errorMessage) {
	  super(errorMessage);
  }
}