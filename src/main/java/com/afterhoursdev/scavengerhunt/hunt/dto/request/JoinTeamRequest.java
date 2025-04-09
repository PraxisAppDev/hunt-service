package com.afterhoursdev.scavengerhunt.hunt.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * The joinTeamRequest class contains information about a Player. Instances
 * of this class are used to represent an individual Player's request to 
 * join a team.
 *     
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class JoinTeamRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	private String playerName;
}