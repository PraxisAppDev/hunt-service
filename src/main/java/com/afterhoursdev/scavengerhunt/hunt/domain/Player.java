package com.afterhoursdev.scavengerhunt.hunt.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Player class stores basic information about a player. Instances
 * of this class are used to represent individual players participating
 * in a Hunt.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/
@Getter  
@Setter
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private boolean teamLeader;
}