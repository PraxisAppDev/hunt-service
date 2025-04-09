package com.afterhoursdev.scavengerhunt.hunt.dto.response;

import java.io.Serializable;
import java.util.List;

import com.afterhoursdev.scavengerhunt.hunt.domain.TeamResult;

import lombok.Getter;
import lombok.Setter;

/**
 * The LeaderboardResponse class stores leaderboard information about a Hunt.
 *      
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Getter  
@Setter
public class LeaderboardResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String huntId;
	private String huntName;
	private String huntLogoUrl;
	private String startDate;
	private List<TeamResult> teamsResults;
}