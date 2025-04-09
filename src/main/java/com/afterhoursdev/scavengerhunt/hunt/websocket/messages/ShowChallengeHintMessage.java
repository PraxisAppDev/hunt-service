package com.afterhoursdev.scavengerhunt.hunt.websocket.messages;

public class ShowChallengeHintMessage extends BaseHuntMessage {
    private final String challengeId;
    private final String hintId;
    
    public static final String SHOW_HINT_MESSAGE_VERSION = "1.0";
    public static final String SHOW_HINT_MESSAGE = "SHOW_CHALLENGE_HINT";

    private ShowChallengeHintMessage(Builder builder) {
        super(builder);
        this.challengeId = builder.challengeId;
        this.hintId = builder.hintId;
    }

    public static class Builder extends BaseHuntMessage.Builder<Builder> {
        private String challengeId;
        private String hintId;

        public Builder challengeId(String challengeId) {
            this.challengeId = challengeId;
            return this;
        }

        public Builder hintId(String hintId) {
            this.hintId = hintId;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ShowChallengeHintMessage build() {
            return new ShowChallengeHintMessage(this);
        }
    }

    public String getChallengeId() {
        return challengeId;
    }

    public String getHintId() {
        return hintId;
    }
}