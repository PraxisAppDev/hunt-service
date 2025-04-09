package com.afterhoursdev.scavengerhunt.hunt.exception;

/**
 * The HuntRepositoryException class is a wrapper class for all Hunt Repository exceptions.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/
public class HuntRepositoryException extends RuntimeException {
   private static final long serialVersionUID = 1L;
  
   public HuntRepositoryException(String errorMessage, Throwable e) {
		  super(errorMessage);
		  this.initCause(e);
	  }
}