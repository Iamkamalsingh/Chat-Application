package com.singhkamal.loop;

public class User {
    private String profileImage;
    private String email;
    private String userName;
    private String password;
    private String userId;
    private String lastMessage;
    private String status;
    private long registrationTimestamp; // New field to store registration time

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    // Constructor with parameters
    public User(String userId, String userName, String email, String password, String profileImage, String status, long registrationTimestamp) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.status = status;
        this.registrationTimestamp = registrationTimestamp; // Initialize new field
    }

    // Getter and Setter methods
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getRegistrationTimestamp() {
        return registrationTimestamp; // Getter for new field
    }

    public void setRegistrationTimestamp(long registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp; // Setter for new field
    }
}
