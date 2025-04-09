package com.afterhoursdev.scavengerhunt.hunt.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;


/**
 * The ChallengeResult class stores information about a Challenge
 * result. Instances of this class are used to represent
 * the results for a challenge associated with a Team.
 *     
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class ChallengeResult implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String challengeId;    
    private int    answerAttempts; 
    private int    hintsViewed;
    private int    timeToComplete;
	private int    score;
}