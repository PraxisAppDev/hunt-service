package com.afterhoursdev.scavengerhunt.hunt.websocket.messages;

/**
 * The ChallengeResponseMessage class represents the outcome of a 
 * Solve Challenge request. 
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

public class ChallengeResponseMessage extends BaseHuntMessage {
	private String  challengeId ;
	private boolean challengeSolved;
	private int     score;
      
    public static final String CHALLENGE_RESPONSE_MESSAGE_VERSION = "1.0";
    public static final String CHALLENGE_RESPONSE_MESSAGE = "CHALLENGE_RESPONSE";

    private ChallengeResponseMessage(Builder builder) {
        super(builder);
        this.challengeId = builder.challengeId;
        this.challengeSolved = builder.challengeSolved;
        this.score = builder.score;
    }

    public static class Builder extends BaseHuntMessage.Builder<Builder> {
        private String challengeId;
        private boolean challengeSolved;
        private int score;
  
        public Builder challengeId(String challengeId) {
            this.challengeId = challengeId;
            return this;
        }

        public Builder challengeSolved(boolean challengeSolved) {
            this.challengeSolved = challengeSolved;
            return this;
        }
        
        public Builder score(int score) {
            this.score = score;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ChallengeResponseMessage build() {
            return new ChallengeResponseMessage(this);
        }
    }

    public String getChallengeId() {
        return challengeId;
    }

    public boolean getChallengeSolved() {
        return challengeSolved;
    }
    
    public int getScore() {
        return score;
    }
}