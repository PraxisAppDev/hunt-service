package com.afterhoursdev.scavengerhunt.hunt.dto.response;

import java.io.Serializable;
import java.util.List;

import com.afterhoursdev.scavengerhunt.hunt.domain.TeamResult;

import lombok.Getter;
import lombok.Setter;

@Getter  
@Setter
public class LeaderboardResponse2 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String huntId;
	private String huntName;
	private String huntLogoUrl;
	private int    huntScore;
	private List<TeamResult> teamHuntResults;
}