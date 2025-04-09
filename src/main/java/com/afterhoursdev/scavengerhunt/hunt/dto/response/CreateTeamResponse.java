package com.afterhoursdev.scavengerhunt.hunt.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter  
@Setter
public class CreateTeamResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String teamId;
	private String teamName;
 }