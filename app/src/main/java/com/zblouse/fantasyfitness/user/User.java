package com.zblouse.fantasyfitness.user;

public class User {

    private String id;
    private String username;

    public User(String id, String username){
        this.id = id;
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public String getId(){
        return this.id;
    }

}
