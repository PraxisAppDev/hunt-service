package com.afterhoursdev.scavengerhunt.hunt.websocket.messages;

public class StartHuntMessage extends BaseHuntMessage {
    private final String teamName;
    private final String teamLeader;
       
    public static final String START_HUNT_MESSAGE_VERSION = "1.0";
    public static final String START_HUNT_MESSAGE = "HUNT_STARTED";
    
    private StartHuntMessage(Builder builder) {
        super(builder);
        this.teamName = builder.teamName;
        this.teamLeader = builder.teamLeader;
    }

    public static class Builder extends BaseHuntMessage.Builder<Builder> {
        private String teamName;
        private String teamLeader;
   
        public Builder teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder teamLeader(String teamLeader) {
            this.teamLeader = teamLeader;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public StartHuntMessage build() {
            return new StartHuntMessage(this);
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamLeader() {
        return teamLeader;
    }
}