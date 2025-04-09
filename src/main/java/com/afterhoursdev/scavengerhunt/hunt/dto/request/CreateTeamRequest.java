package com.afterhoursdev.scavengerhunt.hunt.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * The CreateTeamRequest class contains information about a team. Instances
 * of this class are used to represent a Player's request to create a team.
 *     
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class CreateTeamRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	private String teamName;
	
	@NotNull
	private String playerName;
			 
	@NotNull
	private boolean huntAlone;
 }