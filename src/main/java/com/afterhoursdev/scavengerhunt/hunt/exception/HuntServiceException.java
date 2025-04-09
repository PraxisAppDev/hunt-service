package com.afterhoursdev.scavengerhunt.hunt.exception;

/**
 * The HuntServiceException class is a wrapper class for all Hunt Service exceptions.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/
public class HuntServiceException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public HuntServiceException(String errorMessage, Throwable e) {
	  super(errorMessage);
	  this.initCause(e);
  }
}