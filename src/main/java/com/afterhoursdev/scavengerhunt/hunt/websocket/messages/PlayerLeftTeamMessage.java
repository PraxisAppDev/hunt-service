package com.afterhoursdev.scavengerhunt.hunt.websocket.messages;

public class PlayerLeftTeamMessage extends BaseHuntMessage {
    private final String teamName;
    private final String playerName;

    public static final String PLAYER_LEFT_TEAM_MESSAGE_VERSION = "1.0";
    public static final String PLAYER_LEFT_TEAM_MESSAGE = "PLAYER_LEFT_TEAM";

    private PlayerLeftTeamMessage(Builder builder) {
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
        public PlayerLeftTeamMessage build() {
            return new PlayerLeftTeamMessage(this);
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPlayerName() {
        return playerName;
    }
}