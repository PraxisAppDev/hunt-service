package com.afterhoursdev.scavengerhunt.hunt.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


/**
 * The TeamResult class stores information about a Team's Hunt 
 * results. Instances of this class are used to represent a team's
 * results associated with a Hunt.
 *     
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class TeamResult implements Serializable {
    private static final long serialVersionUID = 1L;
     
    private String teamId;
    private String name;
    private String logoURL;
    private int    score;
    private List<ChallengeResult> challengeResults;
}