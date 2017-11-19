package com.collabify.collabify;

/**
 * Created by Tymoore on 11/18/2017.
 */

public class User {
    public String userID;
    public boolean isHost;
    public String roomID;
    public String name;

    User(String userID, String name, boolean isHost, String roomID){
        this.userID=userID;
        this.isHost=isHost;
        this.roomID=roomID;
        this.name=name;
    }
    User(){

    }

    @Override
    public String toString(){
        return String.valueOf(name);
    }
}
