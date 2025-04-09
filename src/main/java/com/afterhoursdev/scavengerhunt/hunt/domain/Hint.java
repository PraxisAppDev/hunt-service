package com.afterhoursdev.scavengerhunt.hunt.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Hint class stores basic information about a Hint. Instances
 * of this class are used to represent an individual Hint associated
 * with a Challenge. 
 *     
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class Hint implements Serializable {
  private static final long serialVersionUID = 1L;
  private String hintId;
  private String description;
  private String hint;
}