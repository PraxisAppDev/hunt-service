package com.afterhoursdev.scavengerhunt.hunt.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Team class stores basic information about a Team. Instances
 * of this class are used to represent an individual Team participating
 * in a Hunt. 
 *     
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;
    private String  teamId;
    private String  name;
    private String  logoURL;
    private boolean lockStatus;
    private List<Player> players;
}