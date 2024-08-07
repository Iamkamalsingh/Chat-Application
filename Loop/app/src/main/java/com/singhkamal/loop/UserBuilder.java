package com.singhkamal.loop;

public class UserBuilder {
    private String userId;
    private String userName;
    private String email;
    private String password;
    private String profileImage;
    private String status;
    private String id;
    private String name;
    private String rePassword;

    public UserBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public UserBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public UserBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setRePassword(String rePassword) {
        this.rePassword = rePassword;
        return this;
    }

    public User createUser() {
        return new User( userId, userName, email, password, profileImage, status ,System.currentTimeMillis());
    }
}