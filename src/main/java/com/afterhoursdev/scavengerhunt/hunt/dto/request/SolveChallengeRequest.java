package com.afterhoursdev.scavengerhunt.hunt.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * The SolveChallengeRequest class contains information about a solution to
 * a Challenge. Instances of this class are used to represent a Player's 
 * attempt to solve a Challenge.
 *     
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class SolveChallengeRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	private int attemptNumber;
	
	@NotNull
	private int timeSinceChallengeStarted;  // ms
	
	@NotNull
	private int numberOfHintsUsed;
	
	@NotNull
	private String answer;
}