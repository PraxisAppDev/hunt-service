package com.afterhoursdev.scavengerhunt.hunt.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter  
@Setter
public class DeleteTeamResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String huntId;
	private String teamId;
 }