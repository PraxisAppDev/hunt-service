package com.afterhoursdev.scavengerhunt.hunt.websocket.messages;

/**
 * The EndHuntMessage class contains information about the End of
 * a Hunt.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

public class EndHuntMessage extends BaseHuntMessage {
    private final String teamName;
    private final String playerName;
       
    public static final String END_HUNT_MESSAGE_VERSION = "1.0";
    public static final String END_HUNT_MESSAGE = "HUNT_ENDED";
    
    private EndHuntMessage(Builder builder) {
        super(builder);
        this.teamName = builder.teamName;
        this.playerName = builder.playerName;
    }

    public static class Builder extends BaseHuntMessage.Builder<Builder> {
        private String teamName;
        private String playerName;
   
        public Builder teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder playerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public EndHuntMessage build() {
            return new EndHuntMessage(this);
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPlayerName() {
        return playerName;
    }
}