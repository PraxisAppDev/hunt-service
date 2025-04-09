package com.afterhoursdev.scavengerhunt.hunt.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Challenge class stores basic information about a Challenge. 
 * Instances of this class are used to represent individual Challenges
 * associated with a Hunt.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class Challenge implements Serializable {
    private static final long serialVersionUID = 1L;
    private String challengeId;
    private int sequence;
    private int difficulty;
    private String description;
    private String solutionType;
    private String solutionAnswer;
    private String clue;
    private List<Hint> hints;
}