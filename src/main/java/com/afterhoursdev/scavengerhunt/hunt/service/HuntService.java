package com.afterhoursdev.scavengerhunt.hunt.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.afterhoursdev.scavengerhunt.hunt.domain.Challenge;
import com.afterhoursdev.scavengerhunt.hunt.domain.ChallengeResult;
import com.afterhoursdev.scavengerhunt.hunt.domain.Hint;
import com.afterhoursdev.scavengerhunt.hunt.domain.Hunt;
import com.afterhoursdev.scavengerhunt.hunt.domain.HuntHistory;
import com.afterhoursdev.scavengerhunt.hunt.domain.Player;
import com.afterhoursdev.scavengerhunt.hunt.domain.Team;
import com.afterhoursdev.scavengerhunt.hunt.domain.TeamResult;
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
import com.afterhoursdev.scavengerhunt.hunt.exception.HuntRepositoryException;
import com.afterhoursdev.scavengerhunt.hunt.exception.HuntServiceException;
import com.afterhoursdev.scavengerhunt.hunt.exception.InvalidParameterException;
import com.afterhoursdev.scavengerhunt.hunt.exception.ResourceExistsException;
import com.afterhoursdev.scavengerhunt.hunt.exception.ResourceNotFoundException;
import com.afterhoursdev.scavengerhunt.hunt.repository.HuntRepository;
import com.afterhoursdev.scavengerhunt.hunt.websocket.messages.ChallengeResponseMessage;
import com.afterhoursdev.scavengerhunt.hunt.websocket.messages.EndHuntMessage;
import com.afterhoursdev.scavengerhunt.hunt.websocket.messages.PlayerJoinedTeamMessage;
import com.afterhoursdev.scavengerhunt.hunt.websocket.messages.PlayerLeftTeamMessage;
import com.afterhoursdev.scavengerhunt.hunt.websocket.messages.ShowChallengeHintMessage;
import com.afterhoursdev.scavengerhunt.hunt.websocket.messages.StartChallengeMessage;
import com.afterhoursdev.scavengerhunt.hunt.websocket.messages.StartHuntMessage;
import com.afterhoursdev.scavengerhunt.hunt.websocket.messages.TeamUpdatedMessage;


@Service
public class HuntService {
    //private static final Gson gson = new Gson();
    //private static final Logger logger = LoggerFactory.getLogger(HuntService.class);
	 
    private HashMap<String, Challenge> challengeSolutions = new HashMap<>();
	public static final int SALT_STRING_LENGTH = 16;
	
    @Autowired
    WebSocketHandler webSocketHandler;
    	
	@Autowired
	HuntRepository huntRepository;
	
		
	@Cacheable(value = "hunt", key = "huntId")
	public Hunt getHunt(String huntId) throws HuntServiceException, ResourceNotFoundException {
		try {
    		Optional<Hunt> optionalHunt = huntRepository.getHunt(huntId);
    		optionalHunt.orElseThrow(() -> new ResourceNotFoundException("Hunt not found for ID: " + huntId));
    		return optionalHunt.get();
     	} catch (HuntRepositoryException e) {    
			throw new HuntServiceException(e.getMessage(), e);
    	}	
    }
	
	public List<Hunt> getHunts(LocalDate fromDate, LocalDate toDate, int limit) throws HuntServiceException {
	    // Validate date input. Both fromDate and toDate must be provided or both must be null.
	    if ((fromDate != null && toDate == null) || (fromDate == null && toDate != null)) {
	       throw new InvalidParameterException("Both fromDate and toDate must be provided or both must be null.");
	    }

	    String isoFromDate = null;
	    String isoToDate = null;
	    try {
	        // Convert dates to ISO format if they are provided
	        if (fromDate != null && toDate != null) {
	            isoFromDate = convertDateToISOFormat(fromDate);
	            isoToDate = convertDateToISOFormat(toDate);
	        }
	        return huntRepository.getHunts(isoFromDate, isoToDate, limit);
	    } catch (DateTimeParseException | HuntRepositoryException e) {
	        throw new HuntServiceException("An error occurred while retrieving hunts: " + e.getMessage(), e);
	    }
	}

    public List<Team> getTeams(String huntId) throws HuntServiceException {
     	try {
       		return huntRepository.getTeams(huntId);	
        } catch (HuntRepositoryException e) {    
		    throw new HuntServiceException(e.getMessage(), e);
	    }
    }
 
    public Team getTeam(String huntId, String teamId) throws HuntServiceException, ResourceNotFoundException {
       	try {
       		Optional<Team> optionalTeam = huntRepository.getTeam(huntId, teamId);
    	    optionalTeam.orElseThrow(() -> new ResourceNotFoundException("Team not found for ID: " + teamId));
    	    return optionalTeam.get();
        } catch (HuntRepositoryException e) {    
		    throw new HuntServiceException(e.getMessage(), e);
	    }
    }
    
    public List<Challenge> getChallenges(String huntId) throws HuntServiceException {
    	try {
    		return huntRepository.getChallenges(huntId);
      	} catch (HuntRepositoryException e) {    
			throw new HuntServiceException(e.getMessage(), e);
    	}
    }
    
    public Challenge getChallenge(String huntId, String challengeId) throws HuntServiceException {
    	try {
    		Optional<Challenge> optionalChallenge = huntRepository.getChallenge(huntId, challengeId);
    		optionalChallenge.orElseThrow(() -> new ResourceNotFoundException("Challenge not found for ID: " + challengeId));
    		return optionalChallenge.get();
     	} catch (HuntRepositoryException e) {    
			throw new HuntServiceException(e.getMessage(), e);
    	}	
    }
     
    public CreateTeamResponse createTeam(String huntId, CreateTeamRequest createTeamRequest) 
		          throws ResourceExistsException, HuntServiceException {
	    if (teamExists(huntId, createTeamRequest.getTeamName())) {
   		   throw new ResourceExistsException("Team " + createTeamRequest.getTeamName() + " already exists.");
   	    } else {
   	        Team newTeam = new Team();
   	    
   	        newTeam.setTeamId(getSaltString());
   	        newTeam.setName(createTeamRequest.getTeamName());
   	        // TODO: remove hard coding of value
   	        newTeam.setLogoURL("https://afterhours-content.s3.us-east1.amazonaws.com/team-logo.png");
   	        newTeam.setLockStatus(false);
   	    
   	        ArrayList<Player> players = new ArrayList<>();
   	        Player player = new Player();
   	        player.setName(createTeamRequest.getPlayerName());
   	        
   	        System.out.println("Team Leader: " + createTeamRequest.isHuntAlone());
   	        
   	        player.setTeamLeader(createTeamRequest.isHuntAlone());
   	        players.add(player);
   	        newTeam.setPlayers(players);
   	      	 	
   	        if (huntRepository.createTeam(huntId, newTeam)) {
   	           CreateTeamResponse createTeamResponse = new CreateTeamResponse();
    	       createTeamResponse.setTeamId(newTeam.getTeamId());
    	       createTeamResponse.setTeamName(newTeam.getName());
       	       return createTeamResponse;
   	        } else {
   	        	throw new HuntServiceException("Unable to create team", new Throwable());
   	        }
       }
   }
 
   public DeleteTeamResponse deleteTeam(String huntId, String teamId)
	                   throws ResourceNotFoundException, HuntServiceException {
		
	   /*
	    Guard Clause pattern
	    
	    if (hunt does not exist) {
		    throw ResourceNotFoundException("Hunt does not exist")
		    return
		}

		if (team does not exist) {
		    throw ResourceNotFoundException("Team does not exist")
		    return
		}

		if (team size is not 1) {
		    throw HuntServiceException("Cannot delete team; Players on team")
		    return
		}

		if (delete team fails) {
		    throw HuntServiceException("Cannot delete team; Deletion failed")
		    return
		}

		// Build response message
	   */
	   
	   
	   // if (hunt exists) {
	   //    if (team exists) {
	   //        if team size = 1 {
	   //            if (delete team) {
	   //                build response message
	   //            else {
	   //                cannot delete team; Players on team
	   //                throw HuntServiceException    
	   //        else {
	   //            cannot delete team; Players on team
	   //            throw HuntServiceException
       //        }
	   //    else {
	   //        cannot delete team; team does not exist
	   //        throw ResourceNotFoundException
	   //    }
	   // else {
	   //    cannot delete team; hunt does not exist
	   //    throw ResourceNotFoundException
	   // }
	   
	   	/*   
	    Optional<Team> optionalTeam = huntRepository.getTeam(huntId, teamId);
	    optionalTeam.orElseThrow(() -> new ResourceNotFoundException("Team not found for ID: " + teamId));
	    Team team = optionalTeam.get();
	    if (team.getPlayers().size() > 1) {
	       	System.out.println("Cannot delete Team, size > 1");
	    	throw new HuntServiceException("Unable to delete team", new Throwable());
	    }
	    */
	    
    	if (huntRepository.deleteTeam(huntId, teamId)) {   	
    	    DeleteTeamResponse deleteTeamResponse = new DeleteTeamResponse();
      	    deleteTeamResponse.setHuntId(huntId);
    	    deleteTeamResponse.setTeamId(teamId);
       	    return deleteTeamResponse;
    	} else {
	        throw new HuntServiceException("Unable to delete team", new Throwable());
	   	}
    }
    
    public StartHuntResponse startHunt(String huntId, String teamId) throws HuntServiceException {
      	
    	// TODO: Do we want to update the hunt start time and hunt status in mongo
    	// TODO: Make sure Hunt and Team exist
    	// Build and send the HUNT_STARTED web socket message
        StartHuntMessage startHuntMessage = new StartHuntMessage.Builder()
                .version(StartHuntMessage.START_HUNT_MESSAGE_VERSION)
                .messageType(StartHuntMessage.START_HUNT_MESSAGE)
                .huntId(huntId)
                .teamId(teamId)
                .build();
         webSocketHandler.sendWebSocketMessage(startHuntMessage);
   	    
        // Build HTTP response message
      	StartHuntResponse startHuntResponse = new StartHuntResponse();
       	startHuntResponse.setHuntId(huntId);
      	startHuntResponse.setTeamId(teamId);
                	
    	return startHuntResponse;
    }
    
    public EndHuntResponse endHunt(String huntId, String teamId) throws HuntServiceException {
		
    	// TODO: Do we want to update the hunt end time and hunt status in mongo
    	// TODO: Make sure Hunt and Team exist
    	// Build and send the HUNT_END web socket message
        EndHuntMessage huntEndMessage = new EndHuntMessage.Builder()
                .version(EndHuntMessage.END_HUNT_MESSAGE_VERSION)
                .messageType(EndHuntMessage.END_HUNT_MESSAGE)
                .huntId(huntId)
                .teamId(teamId)
                .build();
         webSocketHandler.sendWebSocketMessage(huntEndMessage);
   	    
        // Build HTTP response message
      	EndHuntResponse endHuntResponse = new EndHuntResponse();
       	endHuntResponse.setHuntId(huntId);
      	endHuntResponse.setTeamId(teamId);
                	
    	return endHuntResponse;
	}
    
    public StartChallengeResponse startChallenge(String huntId, String teamId, String challengeId) {
        StartChallengeMessage startChallengeMessage = new StartChallengeMessage.Builder()
                .version(StartChallengeMessage.START_CHALLENGE_MESSAGE_VERSION)
                .messageType(StartChallengeMessage.START_CHALLENGE_MESSAGE)
                .huntId(huntId)
                .teamName(teamId)
                .challengeId(challengeId)
                .build();
        webSocketHandler.sendWebSocketMessage(startChallengeMessage);
        
        // Build HTTP response message
      	StartChallengeResponse startChallengeResponse = new StartChallengeResponse();
      	startChallengeResponse.setHuntId(huntId);
      	startChallengeResponse.setTeamId(teamId);
      	startChallengeResponse.setChallengeId(challengeId);
    	return startChallengeResponse;
    }
    
    public void showChallengeHint(String huntId, String teamId, String challengeId, String hintId) {
       ShowChallengeHintMessage showHintMessage = new ShowChallengeHintMessage.Builder()
              .version(ShowChallengeHintMessage.SHOW_HINT_MESSAGE_VERSION)
              .messageType(ShowChallengeHintMessage.SHOW_HINT_MESSAGE)
              .huntId(huntId)
              .teamId(teamId)
              .challengeId(challengeId)
              .hintId(hintId)
              .build();
        webSocketHandler.sendWebSocketMessage(showHintMessage);
    }
     
    public JoinTeamResponse joinTeam(String huntId, String teamId, JoinTeamRequest joinTeamRequest) 
    		          throws HuntServiceException, ResourceNotFoundException {
   	    // Make sure Team exists
    	if (!huntRepository.teamExistsById(huntId, teamId)) {
	    	throw new ResourceNotFoundException("Team not found for ID: " + teamId);	
	    }
	    
   	    // Join Player to Team
	    Player player = new Player();
	    player.setName(joinTeamRequest.getPlayerName());
	    player.setTeamLeader(false);
	    if (!huntRepository.joinTeam(huntId, teamId, player)) {
	    	throw new HuntServiceException("Unable to add Player to team: " + teamId, new Throwable());
	    }
	    
	    // Get Updated Team info
    	Optional<Team> optionalTeam = huntRepository.getTeam(huntId, teamId);
    	optionalTeam.orElseThrow(() -> new ResourceNotFoundException("Updated Team not found for ID: " + teamId));
               		    		
       	// Build HTTP Join Team response message
    	Team updatedTeam = optionalTeam.get();
    	JoinTeamResponse joinTeamResponse = new JoinTeamResponse();
        joinTeamResponse.setHuntId(huntId);
        joinTeamResponse.setTeamId(updatedTeam.getTeamId());
        joinTeamResponse.setTeamName(updatedTeam.getName());
        joinTeamResponse.setPlayers(updatedTeam.getPlayers());	
          	
        // Build and send Join Team Web Socket message
        PlayerJoinedTeamMessage playerJoinedTeamMessage = new PlayerJoinedTeamMessage.Builder()
                    .version(PlayerJoinedTeamMessage.PLAYER_JOINED_TEAM_MESSAGE_VERSION)
                    .huntId(huntId)
                    .teamId(teamId)
                    .messageType(PlayerJoinedTeamMessage.PLAYER_JOINED_TEAM_MESSAGE)
                    .teamName(updatedTeam.getName())
                    .playerName(player.getName())
                    .build();
        webSocketHandler.sendWebSocketMessage(playerJoinedTeamMessage);
       	return joinTeamResponse;
    }
      
    public LeaveTeamResponse leaveTeam(String huntId, String teamId, LeaveTeamRequest leaveTeamRequest) 
    		      throws HuntServiceException, ResourceNotFoundException {
     	// Make sure Team exists
    	if (!huntRepository.teamExistsById(huntId, teamId)) {
	    	throw new ResourceNotFoundException("Team not found for ID: " + teamId);
	    }
     	
    	// Remove Player from Team
     	if (!huntRepository.leaveTeam(huntId, teamId, leaveTeamRequest.getPlayerName())) {
     		throw new HuntServiceException("Unable to remove Player from team: " + teamId, new Throwable());
     	}
      	
     	// Get Updated Team info
   		Optional<Team> optionalTeam = huntRepository.getTeam(huntId, teamId);
    	optionalTeam.orElseThrow(() -> new ResourceNotFoundException("Updated Team not found for ID: " + teamId));
      	
    	// Build HTTP Leave Team response 
    	Team updatedTeam = optionalTeam.get();
    	LeaveTeamResponse leaveTeamResponse = new LeaveTeamResponse();
        leaveTeamResponse.setHuntId(huntId);
        leaveTeamResponse.setTeamId(updatedTeam.getTeamId());
        leaveTeamResponse.setTeamName(updatedTeam.getName());
        leaveTeamResponse.setPlayers(updatedTeam.getPlayers());	
          	
       	// Build and send Leave Team Web Socket message
        PlayerLeftTeamMessage playerLeftTeamMessage = new PlayerLeftTeamMessage.Builder()
                    .version(PlayerLeftTeamMessage.PLAYER_LEFT_TEAM_MESSAGE_VERSION)
                    .messageType(PlayerLeftTeamMessage.PLAYER_LEFT_TEAM_MESSAGE)
                    .huntId(huntId)
                    .teamId(teamId)
                    .teamName(updatedTeam.getName())
                    .playerName(leaveTeamRequest.getPlayerName())
                    .build();
       webSocketHandler.sendWebSocketMessage(playerLeftTeamMessage);
       return leaveTeamResponse;
    }
    
    public LeaderboardResponse getLeaderboard(String huntId) {
      	Hunt hunt = getHunt(huntId);
      	List<TeamResult> teamResults = huntRepository.getLeaderboard(huntId);
    	
       	for (TeamResult teamResult : teamResults) {
       		teamResult.setScore(calculateTotalScore(teamResult.getChallengeResults()));	
       	}
       	
       	LeaderboardResponse leaderBoardResponse = new LeaderboardResponse();
    	leaderBoardResponse.setHuntId(huntId);
    	leaderBoardResponse.setHuntName(hunt.getName());
    	leaderBoardResponse.setHuntLogoUrl(hunt.getLogoURL());
    	leaderBoardResponse.setStartDate(hunt.getStartDate());
    	leaderBoardResponse.setTeamsResults(teamResults);
    	
    	return leaderBoardResponse;
    }
    
        
    // HARD CODED HUNT HISTORY
    public HuntHistoryResponse getHuntHistory(String userId) throws HuntServiceException {
    	HuntHistoryResponse huntHistoryResponse = new HuntHistoryResponse();
    	    	    	  	
    	// Get current date and time with timezone
	    ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
				
	    // Convert to ISODate format (as String)
  	    String isoZonedDateTime = currentZonedDateTime.toString();
			
  	    ArrayList<HuntHistory> hunts = new ArrayList<>();
  	    
  	    HuntHistory huntHistory1 = new HuntHistory();
 	    huntHistory1.setId("265c4ge");
    	huntHistory1.setName("Explore Praxis");
    	huntHistory1.setVenue("Green Turtle");
    	huntHistory1.setLogoURL("https://afterhours-content.s3.amazonaws.com/hunt-logo.png");
    	huntHistory1.setStartDate(isoZonedDateTime);
    	huntHistory1.setPlace(1);
       	hunts.add(huntHistory1);
    	
       	HuntHistory huntHistory2 = new HuntHistory();
 	    huntHistory2.setId("o986fg43es");
    	huntHistory2.setName("Intern Meetup");
    	huntHistory2.setVenue("Praxis HQ");
    	huntHistory2.setLogoURL("https://afterhours-content.s3.amazonaws.com/hunt-logo.png");
    	huntHistory2.setStartDate(isoZonedDateTime);
    	huntHistory2.setPlace(6);
       	hunts.add(huntHistory2);
       	
       	HuntHistory huntHistory3 = new HuntHistory();
 	    huntHistory3.setId("876fghrd43");
    	huntHistory3.setName("Praxis Happy Hour");
    	huntHistory3.setVenue("Iron Bridge");
    	huntHistory3.setLogoURL("https://afterhours-content.s3.amazonaws.com/hunt-logo.png");
    	huntHistory3.setStartDate(isoZonedDateTime);
    	huntHistory3.setPlace(4);
       	hunts.add(huntHistory3);
       	
    	// Return HuntHistory Response
    	huntHistoryResponse.setUserId(userId);
    	huntHistoryResponse.setHunts(hunts);
    	
    	return huntHistoryResponse;
     }
    
    // Solve the Challenge
    public SolveChallengeResponse solveChallenge(String huntId, String teamId, String challengeId,
    		        SolveChallengeRequest solveChallengeRequest) throws HuntServiceException {
        Random rand = new Random();
      	int score = 0;
       	getChallengeSolutions(huntId);
      		
      	boolean challengeSolved = isAnswerCorrect(challengeId, solveChallengeRequest.getAnswer());
    	if (challengeSolved) {
    	    ChallengeResult challengeResult = new ChallengeResult();
     	    challengeResult.setChallengeId(challengeId);
    	    challengeResult.setTimeToComplete(solveChallengeRequest.getTimeSinceChallengeStarted());
    	    challengeResult.setAnswerAttempts(solveChallengeRequest.getAttemptNumber());
    	    challengeResult.setHintsViewed(solveChallengeRequest.getNumberOfHintsUsed());
    	    score = rand.nextInt(1000);
    	    challengeResult.setScore(score);
    	    huntRepository.updateChallengeResult(huntId, teamId, challengeResult);	 
      	}
  
    	// Build the web socket message
       	ChallengeResponseMessage challengeResponseMessage = new ChallengeResponseMessage.Builder()
                .version(ChallengeResponseMessage.CHALLENGE_RESPONSE_MESSAGE_VERSION)
                .messageType(ChallengeResponseMessage.CHALLENGE_RESPONSE_MESSAGE)
                .huntId(huntId)
                .teamId(teamId)
                .challengeId(challengeId)
                .challengeSolved(challengeSolved) 
                .score(score)
                .build();
     	webSocketHandler.sendWebSocketMessage(challengeResponseMessage);
                
	    // Build HTTP Response
     	SolveChallengeResponse solveChallengeResponse = new SolveChallengeResponse();
    	solveChallengeResponse.setHuntId(huntId);  
        solveChallengeResponse.setTeamId(teamId);  
        solveChallengeResponse.setChallengeId(challengeId);
        solveChallengeResponse.setChallengeSolved(challengeSolved);
        solveChallengeResponse.setScore(score);
        return solveChallengeResponse;
    }
     
    public UpdateTeamResponse updateTeam(String huntId, String teamId, 
    		     Map<String, Object>updateTeamRequest) 
    		     throws InvalidParameterException, ResourceNotFoundException, HuntServiceException {
        	
    	if (!updateTeamRequest.containsKey("newTeamName")) {
     	    throw new InvalidParameterException("Missing parameter: newTeamName.");
    	}
      	
    	String newTeamName = ((String)updateTeamRequest.get("newTeamName"));	
     	try {
      	    if (!huntRepository.updateTeamName(huntId, teamId, newTeamName)) {
      		    throw new ResourceNotFoundException("Resource not found for Team ID: " + teamId);
      	    }
       	} catch (HuntRepositoryException e) {
       	    throw new HuntServiceException(e.getMessage(), e);
       	}
       	   
      	// Build the web socket message
       	TeamUpdatedMessage teamUpdatedMessage = new TeamUpdatedMessage.Builder()
                  .version(TeamUpdatedMessage.TEAM_UPDATED_MESSAGE_VERSION)
                  .messageType(TeamUpdatedMessage.TEAM_UPDATED_MESSAGE)
                  .huntId(huntId)
                  .teamId(teamId)
                  .teamName(newTeamName)
                  .build();
       	webSocketHandler.sendWebSocketMessage(teamUpdatedMessage);
       	
       	// Build HTTP Team Updated Response
       	UpdateTeamResponse updateTeamResponse = new UpdateTeamResponse();
    	updateTeamResponse.setHuntId(huntId);
    	updateTeamResponse.setTeamId(teamId);
    	updateTeamResponse.setTeamName(newTeamName);
    	updateTeamResponse.setTeamLogoUrl("https://afterhours-content.s3.amazonaws.com/team-logo.png");
    	updateTeamResponse.setLockStatus(false);
      	return updateTeamResponse;
    }
 	
	public Hint getHint(String huntId, String challengeId, String hintId) throws HuntServiceException {
		try {
    		Optional<Hint> optionalHint = huntRepository.getHint(huntId, challengeId, hintId);
    		optionalHint.orElseThrow(() -> new ResourceNotFoundException("Hint not found for ID: " + hintId));
    		return optionalHint.get();
     	} catch (HuntRepositoryException e) {    
			throw new HuntServiceException(e.getMessage(), e);
    	}	
	}
		
	private String convertDateToISOFormat(LocalDate localDate) throws HuntServiceException {
        try {
            ZonedDateTime utcDateTime = localDate.atStartOfDay(ZoneId.of("UTC"));
            return utcDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        } catch (DateTimeParseException e) {
        	throw new HuntServiceException(e.getMessage(), e);
        }
    }
	
	private String getSaltString() {
	    String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < SALT_STRING_LENGTH) { // length of the random string.
	        int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    String saltStr = salt.toString();
	    return saltStr;
    }
		
	private boolean isAnswerCorrect(String challengeId, String answer) {
		System.out.println("ChallengeID: " + challengeId);
		Challenge challenge = challengeSolutions.get(challengeId);
		String solution = challenge.getSolutionAnswer();
		//List<String> solutionList = Arrays.asList(solution.split(","));
		List<String> solutionList = Arrays.asList(solution.split(";"));
		return solutionList.contains(answer.toUpperCase());
	}
	
	@Cacheable("challengeSolutions")
    private void getChallengeSolutions(String huntId) {
		System.out.println("First time call, caching going forwars");
        challengeSolutions.clear(); // Optional: Clear previous data to avoid stale cache
        List<Challenge> challenges = huntRepository.getChallengeSolutions(huntId);
        for (Challenge challenge : challenges) {
            challengeSolutions.put(challenge.getChallengeId(), challenge);
        }
    }
		
	// TODO: need to handle exception
	private boolean teamExists(String huntId, String teamId) {
		return huntRepository.teamExists(huntId, teamId);
	}
	
	private int calculateTotalScore(List<ChallengeResult> challengeResults) {
        int totalScore = 0;
        if (challengeResults != null) {
            for (ChallengeResult challengeResult : challengeResults) {
       	        totalScore += challengeResult.getScore();	
       	    }
        }
        return totalScore;
    }
}