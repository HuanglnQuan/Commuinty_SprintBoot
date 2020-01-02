package com.lnquan.community.dto;

public class User {
    private String login;
    private String id;
    private String bio;
    private String email;
    private String avatar_url;

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", id='" + id + '\'' +
                ", bio='" + bio + '\'' +
                ", email='" + email + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public User() {
    }

    public User(String login, String id, String bio, String email, String avatar_url) {
        this.login = login;
        this.id = id;
        this.bio = bio;
        this.email = email;
        this.avatar_url = avatar_url;
    }
}
