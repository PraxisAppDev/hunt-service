package com.afterhoursdev.scavengerhunt.hunt.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter  
@Setter
public class UpdateTeamResponse implements Serializable {
	private static final long serialVersionUID = 1L;
		
	private String  huntId;
	private String  teamId;
	private String  teamName;
	private String  teamLogoUrl;
	private boolean lockStatus;
}