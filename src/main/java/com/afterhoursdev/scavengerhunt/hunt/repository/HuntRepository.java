package com.afterhoursdev.scavengerhunt.hunt.repository;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.afterhoursdev.scavengerhunt.hunt.domain.Challenge;
import com.afterhoursdev.scavengerhunt.hunt.domain.ChallengeResult;
import com.afterhoursdev.scavengerhunt.hunt.domain.Hint;
import com.afterhoursdev.scavengerhunt.hunt.domain.Hunt;
import com.afterhoursdev.scavengerhunt.hunt.domain.Player;
import com.afterhoursdev.scavengerhunt.hunt.domain.Team;
import com.afterhoursdev.scavengerhunt.hunt.domain.TeamResult;
import com.afterhoursdev.scavengerhunt.hunt.exception.HuntRepositoryException;
import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;

/**
 * The HuntRepository class serves as an abstraction layer between the 
 * application's business logic and the underlying MongoDB database operations.
 * HuntRepository class encapsulates the data access logic, providing a clean
 * and consistent interface for performing CRUD (Create, Read, Update, Delete) 
 * operations on the Hunt domain entities.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Service
public class HuntRepository {
    //private static final Logger logger = LoggerFactory.getLogger(HuntRepository.class);
    
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Value("${spring.data.mongodb.collection}") 
	private String collection; 
	
	
	public Optional<Hunt> getHunt(String huntId) throws HuntRepositoryException {
		Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(huntId));
		try {
            Hunt hunt = mongoTemplate.findOne(query, Hunt.class, collection);
            return Optional.ofNullable(hunt);
         } catch (DataAccessException e) {
	        throw new HuntRepositoryException("Database operation failed.", e);
       }
    }

	public List<Hunt> getHunts(String fromDate, String toDate, int limit) throws HuntRepositoryException {
	    
		// Filter the query based on the from date, to date, and limit 
	    Query query = new Query();
	    
	    // Build criteria based on the presence of fromDate and toDate
	    if (fromDate != null && toDate != null) {
	        query.addCriteria(Criteria.where("startDate").gte(fromDate).lte(toDate));
	    }

	    // Apply limit if greater than 0
	    if (limit > 0) {
	        query.limit(limit);
	    }

	    // Execute the query
	    try {
		    return mongoTemplate.find(query, Hunt.class, collection);
	    } catch (DataAccessException e) {
	        System.err.println("DataAccessException: " + e.getMessage());
	        throw new HuntRepositoryException("Database operation failed.", e);
	    } catch (MongoException e) {
	        System.err.println("MongoException: " + e.getMessage());
	        throw new HuntRepositoryException("MongoDB operation failed.", e);
	    } catch (MappingException e) {
	        System.err.println("MappingException: " + e.getMessage());
	        throw new HuntRepositoryException("Result mapping failed.", e);
	    } catch (IllegalArgumentException e) {
	        System.err.println("IllegalArgumentException: " + e.getMessage());
	        throw new HuntRepositoryException("Invalid aggregation pipeline.", e);
	    }
	}
	
	public boolean teamExists(String huntId, String teamName) throws HuntRepositoryException {
	    try {
	    	Query query = new Query(Criteria.where("_id").is(huntId).and("teams.name").is(teamName));
	        return mongoTemplate.exists(query, collection);
	    } catch (DataAccessException e) {
	        System.err.println("DataAccessException: " + e.getMessage());
	        throw new HuntRepositoryException("Database operation failed.", e);
        } 
	}
	
	public boolean teamExistsById(String huntId, String teamId) throws HuntRepositoryException {
	    try {
	    	Query query = new Query(Criteria.where("_id").is(huntId).and("teams.teamId").is(teamId));
	        return mongoTemplate.exists(query, collection);
	    } catch (DataAccessException e) {
	        System.err.println("DataAccessException: " + e.getMessage());
	        throw new HuntRepositoryException("Database operation failed.", e);
        } 
	}
	
    public Optional<Team> getTeam(String huntId, String teamId) throws HuntRepositoryException {
      	// Define the aggregation pipeline operations
        MatchOperation matchHunt = Aggregation.match(Criteria.where("_id").is(huntId));
        UnwindOperation unwindTeams = Aggregation.unwind("teams");
        MatchOperation matchTeam = Aggregation.match(Criteria.where("teams.teamId").is(teamId));
        ReplaceRootOperation replaceRoot = Aggregation.replaceRoot("teams");

        // Build and execute the aggregation pipeline
        try {
        	Aggregation aggregation = Aggregation.newAggregation(matchHunt, unwindTeams, matchTeam, replaceRoot);
            AggregationResults<Team> results = mongoTemplate.aggregate(aggregation, collection, Team.class);
            return Optional.ofNullable(results.getUniqueMappedResult());
        } catch (DataAccessException e) {
	        throw new HuntRepositoryException("Database operation failed.", e);
        }
    }
    
    public List<Team> getTeams(String huntId) throws HuntRepositoryException {
    	// Define the aggregation operations
        MatchOperation matchHunt = Aggregation.match(Criteria.where("_id").is(huntId));
        UnwindOperation unwindTeams = Aggregation.unwind("teams");
        ReplaceRootOperation replaceRoot = Aggregation.replaceRoot("teams");

        // Build and execute the aggregation pipeline
        try {
            Aggregation aggregation = Aggregation.newAggregation(matchHunt, unwindTeams, replaceRoot);
            AggregationResults<Team> results = mongoTemplate.aggregate(aggregation, collection, Team.class);
            return results.getMappedResults();  
        } catch (DataAccessException e) {
       	    throw new HuntRepositoryException("Database operation failed.", e);
        }
    }
    
    public boolean createTeam(String huntId, Team newTeam) throws HuntRepositoryException {
     	Query query = new Query(Criteria.where("_id").is(huntId));
        Update update = new Update().addToSet("teams", newTeam);
        try {
          	UpdateResult result = mongoTemplate.updateFirst(query, update, collection);  
           	return result.getMatchedCount() > 0 && result.getModifiedCount() > 0;
        } catch (DataAccessException e) {
	        throw new HuntRepositoryException("Database operation failed.", e);
       }
    }
    
    public boolean joinTeam(String huntId, String teamId, Player player) throws HuntRepositoryException {
    	boolean result = false;  
       	try {
      	    Query query = new Query(Criteria.where("_id").is(huntId).and("teams.teamId").is(teamId));
      		Update update = new Update().addToSet("teams.$.players", player);
        	UpdateResult updateResult = mongoTemplate.updateFirst(query, update, collection);   
       		result = updateResult.getMatchedCount() > 0 && updateResult.getModifiedCount() > 0;
       	} catch (DataAccessException e) {
	        throw new HuntRepositoryException("Database operation failed.", e);
        }
       	return result;
    }
      
    public boolean leaveTeam(String huntId, String teamId, String playerName) throws HuntRepositoryException {
 	   	boolean result = false;  
   	    try {
       	    Query query = new Query(Criteria.where("_id").is(huntId).and("teams.teamId").is(teamId));
      	    Update update = new Update().pull("teams.$.players", Query.query(Criteria.where("name").is(playerName)));
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, collection); 
       	    result = updateResult.getMatchedCount() > 0 && updateResult.getModifiedCount() > 0;
        } catch (DataAccessException e) {
	        throw new HuntRepositoryException("Database operation failed.", e);
        }
      	return result;
    }
    
    public Optional<Hint> getHint(String huntId, String challengeId, String hintId) throws HuntRepositoryException {
		// Define the aggregation operations
    	MatchOperation matchHunt = Aggregation.match(Criteria.where("_id").is(huntId));
        UnwindOperation unwindChallenges = Aggregation.unwind("challenges");
        MatchOperation matchChallenge = Aggregation.match(Criteria.where("challenges.challengeId").is(challengeId));
        UnwindOperation unwindHints = Aggregation.unwind("challenges.hints");
        MatchOperation matchHint = Aggregation.match(Criteria.where("challenges.hints.hintId").is(hintId));
        ReplaceRootOperation replaceRoot = Aggregation.replaceRoot("challenges.hints");
          
        // Build the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(matchHunt,
        		                                             unwindChallenges,
        		                                             matchChallenge,
        		                                             unwindHints,
        		                                             matchHint,
        		                                             replaceRoot);
         
        // Execute the aggregation pipeline
        try {
           AggregationResults<Hint> results = mongoTemplate.aggregate(aggregation, collection, Hint.class);
           return Optional.ofNullable(results.getUniqueMappedResult());
        } catch (DataAccessException e) {
        	System.out.println("MongoDB Error: " + e.getMessage());
 	        throw new HuntRepositoryException("Database operation failed.", e);
        }
	}
 
	public Optional<Challenge> getChallenge(String huntId, String challengeId) throws HuntRepositoryException {
	    // Define the aggregation operations
        MatchOperation matchHunt = Aggregation.match(Criteria.where("_id").is(huntId));
        UnwindOperation unwindChallenges = Aggregation.unwind("challenges");
        MatchOperation matchChallenge = Aggregation.match(Criteria.where("challenges.challengeId").is(challengeId));
        ReplaceRootOperation replaceRoot = Aggregation.replaceRoot("challenges");
        ProjectionOperation excludeSolutionAnswer = Aggregation.project().andExclude("solutionAnswer");

        // Build the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(matchHunt, unwindChallenges, matchChallenge,
        		                                             replaceRoot, excludeSolutionAnswer);

        // Execute the aggregation pipeline
        try {
            AggregationResults<Challenge> results = mongoTemplate.aggregate(aggregation, collection, Challenge.class);
            return Optional.ofNullable(results.getUniqueMappedResult());
        } catch (DataAccessException e) {
	        throw new HuntRepositoryException("Database operation failed.", e);
        }
 	}
	
	public List<Challenge> getChallenges(String huntId) throws HuntRepositoryException {
		// Define the aggregation operations
		MatchOperation matchHunt = Aggregation.match(Criteria.where("_id").is(new ObjectId(huntId)));
	    UnwindOperation unwindChallenges = Aggregation.unwind("challenges");
	    ReplaceRootOperation replaceRoot = Aggregation.replaceRoot("challenges");
	    ProjectionOperation excludeSolutionAnswer = Aggregation.project().andExclude("solutionAnswer");
	    
	    // Build the aggregation pipeline
	    Aggregation aggregation = Aggregation.newAggregation(matchHunt, unwindChallenges,
	    		                                             replaceRoot, excludeSolutionAnswer);
       	    
	    // Execute the aggregation pipeline
	    try {
	        AggregationResults<Challenge> results = mongoTemplate.aggregate(aggregation, collection, Challenge.class);
	        return results.getMappedResults();  
        } catch (DataAccessException e) {
       	    throw new HuntRepositoryException("Database operation failed.", e);
        }
	}
	
	public List<Challenge> getChallengeSolutions(String huntId) throws HuntRepositoryException {
		// Define the aggregation operations
		MatchOperation matchHunt = Aggregation.match(Criteria.where("_id").is(new ObjectId(huntId)));
	    UnwindOperation unwindChallenges = Aggregation.unwind("challenges");
	    ReplaceRootOperation replaceRoot = Aggregation.replaceRoot("challenges");
	    //ProjectionOperation excludeSolutionAnswer = Aggregation.project().andExclude("solutionAnswer");
	    
	    // Build the aggregation pipeline
	    Aggregation aggregation = Aggregation.newAggregation(matchHunt, unwindChallenges, replaceRoot);
                                         //replaceRoot, excludeSolutionAnswer);
       	    
	    // Execute the aggregation pipeline
	    try {
	        AggregationResults<Challenge> results = mongoTemplate.aggregate(aggregation, collection, 
	        		                                             Challenge.class);
	        return results.getMappedResults();  
        } catch (DataAccessException e) {
       	    throw new HuntRepositoryException("Database operation failed.", e);
        }
	}
	
	public boolean updateTeamName(String huntId, String teamId, String newTeamName) throws HuntRepositoryException {
        Query query = new Query(Criteria.where("_id").is(huntId).and("teams.teamId").is(teamId));
  	    Update update = new Update().set("teams.$.name", newTeamName);
  	    try {
   	        //UpdateResult result = mongoTemplate.updateFirst(query, update, Hunt.class);
   	        UpdateResult result = mongoTemplate.updateFirst(query, update, collection);
   	        return result.getMatchedCount() > 0 && result.getModifiedCount() > 0;
  	    } catch (DataAccessException e) {
       	    throw new HuntRepositoryException("Database operation failed.", e);
        }
 	}
	
	public boolean updateChallengeResult(String huntId, String teamId, ChallengeResult challengeResult) {
	    Query query = new Query(Criteria.where("_id").is(huntId).and("teams.teamId").is(teamId));
        Update update = new Update().addToSet("teams.$.challengeResults", challengeResult);
        UpdateResult result = mongoTemplate.updateFirst(query, update, collection);  //Hunt.class
        return result.getMatchedCount() > 0 && result.getModifiedCount() > 0;
    }
	
	public boolean deleteTeam(String huntId, String teamId)	throws HuntRepositoryException {
       	Query query = new Query(Criteria.where("_id").is(huntId));
       	Update update = new Update().pull("teams", new Document("teamId", teamId));
    	UpdateResult result = mongoTemplate.updateFirst(query, update, collection);
    	return result.getMatchedCount() > 0 && result.getModifiedCount() > 0;
     	
		/*
		Query query = new Query(Criteria.where("_id").is(huntId)
               	.and("teams").elemMatch(Criteria.where("_id").is(teamId)
                .and("players").size(1)));

        // Define the update operation to pull (remove) the team from the teams array
		// check if below line works
        //Update update = new Update().pull("teams", Query.query(Criteria.where("teams._id").is(teamId)));
        Update update = new Update().pull("teams", new BasicDBObject("_id", teamId));
               
        // Execute the update operation
        UpdateResult result = mongoTemplate.updateFirst(query, update, collection);
        System.out.println("Matched Count: " + result.getMatchedCount());
        System.out.println("Modified Count: " + result.getModifiedCount());
        return result.getMatchedCount() > 0 && result.getModifiedCount() > 0;
        */
    }
	
	public List<TeamResult> getLeaderboard(String huntId) throws HuntRepositoryException {
		MatchOperation matchHunt = Aggregation.match(Criteria.where("_id").is(new ObjectId(huntId)));
		UnwindOperation unwindTeams = Aggregation.unwind("teams");
	    ReplaceRootOperation replaceRoot = Aggregation.replaceRoot("teams");
	    ProjectionOperation excludeFields = Aggregation.project().andExclude("lockStatus").andExclude("players");
	   	    
	    //SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.DESC, "teamResults.score"));
	    
	    // Build the aggregation pipeline
	    Aggregation aggregation = Aggregation.newAggregation(matchHunt, unwindTeams, replaceRoot, excludeFields);
                                                	    
	    // Execute the aggregation pipeline
	    try {
	        AggregationResults<TeamResult> results = mongoTemplate.aggregate(aggregation,collection,TeamResult.class);
	        return results.getMappedResults();  
        } catch (DataAccessException e) {
       	    throw new HuntRepositoryException("Database operation failed.", e);
        }
	}
}