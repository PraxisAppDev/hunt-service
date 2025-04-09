package com.afterhoursdev.scavengerhunt.hunt.dto.response;

import java.io.Serializable;
import java.util.List;

import com.afterhoursdev.scavengerhunt.hunt.domain.Player;

import lombok.Getter;
import lombok.Setter;

/**
 * The JoinTeamResponse class contains the serialized JSON fields 
 * returned to clients after a Player has successfully joined a Team.
 * 
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class JoinTeamResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String huntId;
	private String teamId;
	private String teamName;
	private List<Player> players;
 }