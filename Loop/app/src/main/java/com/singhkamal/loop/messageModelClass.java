package com.singhkamal.loop;

public class messageModelClass {
    private String message;
    private String senderid;
    private Long timeStamp;

    public messageModelClass() {
        // Default constructor required for calls to DataSnapshot.getValue(messageModelClass.class)
    }

    public messageModelClass(String message, String senderid, Long timeStamp) {
        this.message = message;
        this.senderid = senderid;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
