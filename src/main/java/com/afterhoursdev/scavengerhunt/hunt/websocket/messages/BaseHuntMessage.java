package com.afterhoursdev.scavengerhunt.hunt.websocket.messages;

public class BaseHuntMessage {
    private final String version;
    private final String huntId;
    private final String teamId;
    private final String messageType;

    protected BaseHuntMessage(Builder<?> builder) {
        this.version = builder.version;
        this.huntId = builder.huntId;
        this.teamId = builder.teamId;
        this.messageType = builder.messageType;
    }
    
    public String getVersion() {
        return version;
    }

    public String getHuntId() {
        return huntId;
    }
    
    public String getTeamId() {
        return teamId;
    }
    
    public String getMessageType() {
        return messageType;
    }
    
    public static abstract class Builder<T extends Builder<T>> {
        private String version;
        private String huntId;
        private String teamId;
        private String messageType;

        public T version(String version) {
            this.version = version;
            return self();
        }

        public T huntId(String huntId) {
            this.huntId = huntId;
            return self();
        }
        
        public T teamId(String teamId) {
            this.teamId = teamId;
            return self();
        }
        
        public T messageType(String messageType) {
            this.messageType = messageType;
            return self();
        }

        protected abstract T self();

        public abstract BaseHuntMessage build();
    }
}