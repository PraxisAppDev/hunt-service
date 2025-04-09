package com.afterhoursdev.scavengerhunt.hunt.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.afterhoursdev.scavengerhunt.hunt.domain.Challenge;
import com.afterhoursdev.scavengerhunt.hunt.domain.Hint;
import com.afterhoursdev.scavengerhunt.hunt.domain.Hunt;
import com.afterhoursdev.scavengerhunt.hunt.domain.Team;
import com.afterhoursdev.scavengerhunt.hunt.dto.request.CreateTeamRequest;
import com.afterhoursdev.scavengerhunt.hunt.dto.request.JoinTeamRequest;
import com.afterhoursdev.scavengerhunt.hunt.dto.request.LeaveTeamRequest;
import com.afterhoursdev.scavengerhunt.hunt.dto.request.SolveChallengeRequest;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.CreateTeamResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.DeleteTeamResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.EndHuntResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.HuntHistoryResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.JoinTeamResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.LeaderboardResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.LeaveTeamResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.SolveChallengeResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.StartChallengeResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.StartHuntResponse;
import com.afterhoursdev.scavengerhunt.hunt.dto.response.UpdateTeamResponse;
import com.afterhoursdev.scavengerhunt.hunt.service.HuntService;

@RestController
@RequestMapping("/api")
public class HuntController {
	
	@Autowired
    HuntService huntService;
	
	@GetMapping("/v1/hunts/{huntId}")
    public ResponseEntity<Hunt> getHunt(@PathVariable String huntId) {
		Hunt hunt = huntService.getHunt(huntId);
		return new ResponseEntity<Hunt>(hunt, HttpStatus.OK);
	}
	
	@GetMapping("/v1/hunts")
	public ResponseEntity<List<Hunt>> getHunts(
			              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                 		  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
 	                      @RequestParam(required = false, defaultValue="0") int limit) {
		//System.out.println("toDate: " + fromDate.toString());
		//System.out.println("fromDate: " + toDate.toString());
		List<Hunt> hunts = huntService.getHunts(fromDate, toDate, limit);
        return new ResponseEntity<List<Hunt>>(hunts, HttpStatus.OK);
    }
	
	@GetMapping("/v1/hunts/{huntId}/teams")
    public ResponseEntity<List<Team>> getTeams(@PathVariable String huntId) {
	    List<Team> teams = huntService.getTeams(huntId);
	    return new ResponseEntity<List<Team>>(teams, HttpStatus.OK);
    }
		
	@GetMapping("/v1/hunts/{huntId}/teams/{teamId}")
    public ResponseEntity<Team> getTeam(@PathVariable String huntId, @PathVariable String teamId) { 
	    Team team = huntService.getTeam(huntId, teamId);
	    return new ResponseEntity<Team>(team, HttpStatus.OK);
    }
	
	@GetMapping("/v1/hunts/{huntId}/challenges")
    public ResponseEntity<List<Challenge>> getChallenges(@PathVariable String huntId) {
  		List<Challenge> challenges = huntService.getChallenges(huntId);
        return new ResponseEntity<List<Challenge>>(challenges, HttpStatus.OK);
    }
	
	@GetMapping("/v1/hunts/{huntId}/challenges/{challengeId}")
    public ResponseEntity<Challenge> getChallenge(@PathVariable String huntId, 
    		                                      @PathVariable String challengeId) {
 		Challenge challenge = huntService.getChallenge(huntId, challengeId);
		return new ResponseEntity<Challenge>(challenge, HttpStatus.OK);
    }
	 
	@GetMapping("/v1/hunts/{huntId}/challenges/{challengeId}/hints/{hintId}")
	public ResponseEntity<Hint> getHint(@PathVariable String huntId, 
	   		                            @PathVariable String challengeId,
	   		                            @PathVariable String hintId) {
	    Hint hint = huntService.getHint(huntId, challengeId, hintId);
		return new ResponseEntity<Hint>(hint, HttpStatus.OK);
	}

	@PostMapping("/v1/hunts/{huntId}/teams/{teamId}/challenges/{challengeId}/hints/{hintId}/show")
	public ResponseEntity<Void> showChallengeHint(
			                             @PathVariable String huntId, 
			                             @PathVariable String teamId,
	   		                             @PathVariable String challengeId,
	   		                             @PathVariable String hintId) {
		huntService.showChallengeHint(huntId, teamId, challengeId, hintId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/v1/hunts/{huntId}/teams")
	public ResponseEntity<CreateTeamResponse> createTeam(@PathVariable String huntId,
			   @Valid @RequestBody CreateTeamRequest createTeamRequest) {
	    CreateTeamResponse createTeamResponse = huntService.createTeam(huntId, createTeamRequest);
        return new ResponseEntity<CreateTeamResponse>(createTeamResponse, HttpStatus.CREATED);
	}
	
	@PutMapping("/v1/hunts/{huntId}/teams/{teamId}/start")
	public ResponseEntity<StartHuntResponse> startHunt(@PathVariable String huntId, 
			                                           @PathVariable String teamId) {
	    StartHuntResponse startHuntResponse = huntService.startHunt(huntId, teamId);
		return new ResponseEntity<StartHuntResponse>(startHuntResponse, HttpStatus.OK);
	}
	
	@PutMapping("/v1/hunts/{huntId}/teams/{teamId}/end")
	public ResponseEntity<EndHuntResponse> endHunt(@PathVariable String huntId, 
			                                       @PathVariable String teamId) {
		EndHuntResponse endHuntResponse = huntService.endHunt(huntId, teamId);
	    return new ResponseEntity<EndHuntResponse>(endHuntResponse, HttpStatus.OK);
	}
	
	@PostMapping("/v1/hunts/{huntId}/teams/{teamId}/join")
	public ResponseEntity<JoinTeamResponse> joinTeam(@PathVariable String huntId,
			                                         @PathVariable String teamId,
			                                         @RequestBody JoinTeamRequest joinTeamRequest) {
	   	JoinTeamResponse joinTeamResponse = huntService.joinTeam(huntId, teamId, joinTeamRequest);
		return new ResponseEntity<JoinTeamResponse>(joinTeamResponse, HttpStatus.OK);
	}
	
	@PostMapping("/v1/hunts/{huntId}/teams/{teamId}/leave")
	public ResponseEntity<LeaveTeamResponse> leaveTeam(@PathVariable String huntId,
			                                           @PathVariable String teamId,
			                                           @RequestBody LeaveTeamRequest leaveTeamRequest) {
		LeaveTeamResponse leaveTeamResponse = huntService.leaveTeam(huntId, teamId, leaveTeamRequest);
	    return new ResponseEntity<LeaveTeamResponse>(leaveTeamResponse, HttpStatus.OK);
	}
	
	@DeleteMapping("/v1/hunts/{huntId}/teams/{teamId}")
	public ResponseEntity<DeleteTeamResponse> deleteTeam(@PathVariable String huntId,
			                                             @PathVariable String teamId) {
	   	DeleteTeamResponse deleteTeamResponse = huntService.deleteTeam(huntId, teamId);
	   	return new ResponseEntity<DeleteTeamResponse>(deleteTeamResponse, HttpStatus.OK);
	}
	
	@GetMapping("/v1/hunts/{huntId}/leaderboard")
	public ResponseEntity<LeaderboardResponse> getLeaderboard(@PathVariable String huntId) {
		LeaderboardResponse leaderboardResponse = huntService.getLeaderboard(huntId);
	    return new ResponseEntity<LeaderboardResponse>(leaderboardResponse, HttpStatus.OK);
	}
	
	@GetMapping("/v1/hunts/history/user/{userId}")
	public ResponseEntity<HuntHistoryResponse> getHuntHistory(@PathVariable String userId) {
	    HuntHistoryResponse huntHistoryResponse = huntService.getHuntHistory(userId);
	    return new ResponseEntity<HuntHistoryResponse>(huntHistoryResponse, HttpStatus.OK);
	}
	
	@PostMapping("/v1/hunts/{huntId}/teams/{teamId}/challenges/{challengeId}/solve")
	public ResponseEntity<SolveChallengeResponse> solveChallenge(@PathVariable String huntId,
		                                                         @PathVariable String teamId,
		                                                         @PathVariable String challengeId,
		                                                         @RequestBody SolveChallengeRequest solveChallengeRequest) { 
	    SolveChallengeResponse solveChallengeResponse = huntService.solveChallenge(huntId, teamId, challengeId, solveChallengeRequest);
	    return new ResponseEntity<SolveChallengeResponse>(solveChallengeResponse, HttpStatus.OK);
	}

	@PatchMapping("/v1/hunts/{huntId}/teams/{teamId}/update")
	public ResponseEntity<UpdateTeamResponse>updateTeam(@PathVariable String huntId,
	                                                    @PathVariable String teamId,
	                                                    @RequestBody Map<String, Object> updateTeamRequest) {
	    UpdateTeamResponse updateTeamResponse = huntService.updateTeam(huntId, teamId, updateTeamRequest);
        return new ResponseEntity<UpdateTeamResponse>(updateTeamResponse, HttpStatus.OK);
	}
	 
	@PostMapping("/v1/hunts/{huntId}/teams/{teamId}/challenges/{challengeId}/start")
	public ResponseEntity<StartChallengeResponse> startChallenge(@PathVariable String huntId,
			                                     @PathVariable String teamId,
			                                     @PathVariable String challengeId) {
		StartChallengeResponse startChallengeResponse = huntService.startChallenge(huntId, teamId, challengeId);
	    return new ResponseEntity<StartChallengeResponse>(startChallengeResponse, HttpStatus.OK);
	}
}