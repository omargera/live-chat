package com.livechat.adapter;

public class ChatMessage {
    public enum MessageType {
        STATUS, LINE
    }

    private String from;
    private String to;
    private MessageType type;
    private String source;
    private String message;
    private int id;
    private int systemID;

    public ChatMessage(String from, String to, String message, MessageType type, String source, int id) {
        this(from,to,message,type,source,id,-1);
    }

    public ChatMessage(String from, String to, String message, MessageType type, String source, int id, int systemID) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.message = message;
        this.source = source;
        this.id = id;
        this.systemID = systemID;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public MessageType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public int getID() {
        return id;
    }
    public int getSystemID() {
        return systemID;
    }

}

