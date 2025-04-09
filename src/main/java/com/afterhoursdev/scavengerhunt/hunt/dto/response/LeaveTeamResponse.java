package com.afterhoursdev.scavengerhunt.hunt.dto.response;

import java.io.Serializable;
import java.util.List;

import com.afterhoursdev.scavengerhunt.hunt.domain.Player;

import lombok.Getter;
import lombok.Setter;

@Getter  
@Setter
public class LeaveTeamResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String huntId;
	private String teamId;
	private String teamName;
	private List<Player> players;
 }