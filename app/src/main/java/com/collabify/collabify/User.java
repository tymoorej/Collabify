package com.collabify.collabify;

import java.util.ArrayList;

/**
 * Created by Tymoore on 11/18/2017.
 */

public class User {
    // FIELDS
    public String userID;
    public boolean isHost;
    public String roomID;
    public String name;

    // CONSTRUCTORS

    public User(String userID, String name, boolean isHost, String roomID){
        this.userID=userID;
        this.isHost=isHost;
        this.roomID=roomID;
        this.name=name;
    }
    public User(){

    }

    // SETTERS
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setUserName(String name) {
        this.name = name;
    }
    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }
    public void setUserRoom(String roomID) {
        this.roomID = roomID;
    }

    // GETTERS
    public String getUserID() {
        return this.userID;
    }
    public String getUserName() {
        return this.name;
    }
    public boolean getIsHost() {
        return this.isHost;
    }
    public String getUserRoom() {
        return this.roomID;
    }




    @Override
    public String toString(){
        return String.valueOf(name);
    }

    static public User getUserFromID(String userID, ArrayList<User> users){
        for(User u: users){
            if(u.getUserID().equals(userID)){
                return u;
            }
        }
        return null;
    }
}
