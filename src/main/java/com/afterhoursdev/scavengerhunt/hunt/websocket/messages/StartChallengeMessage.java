package com.afterhoursdev.scavengerhunt.hunt.websocket.messages;

public class StartChallengeMessage extends BaseHuntMessage {
    private final String teamName;
    private final String challengeId;

    public static final String START_CHALLENGE_MESSAGE_VERSION = "1.0";
    public static final String START_CHALLENGE_MESSAGE = "CHALLENGE_STARTED";
    
    private StartChallengeMessage(Builder builder) {
        super(builder);
        this.teamName = builder.teamName;
        this.challengeId = builder.challengeId;
    }

    public static class Builder extends BaseHuntMessage.Builder<Builder> {
        private String teamName;
        private String challengeId;

        public Builder teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder challengeId(String challengeId) {
            this.challengeId = challengeId;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public StartChallengeMessage build() {
            return new StartChallengeMessage(this);
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public String getChallengeId() {
        return challengeId;
    }
}