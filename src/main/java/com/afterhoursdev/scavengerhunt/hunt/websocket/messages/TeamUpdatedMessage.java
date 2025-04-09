package com.afterhoursdev.scavengerhunt.hunt.websocket.messages;

public class TeamUpdatedMessage extends BaseHuntMessage {
    private final String teamName;
       
    public static final String TEAM_UPDATED_MESSAGE_VERSION = "1.0";
    public static final String TEAM_UPDATED_MESSAGE = "TEAM_UPDATED";
    
    private TeamUpdatedMessage(Builder builder) {
        super(builder);
        this.teamName = builder.teamName;
    }

    public static class Builder extends BaseHuntMessage.Builder<Builder> {
        private String teamName;
        
        public Builder teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }
        
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public TeamUpdatedMessage build() {
            return new TeamUpdatedMessage(this);
        }
    }

    public String getTeamName() {
        return teamName;
    }
 
}