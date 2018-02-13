package com.collabify.collabify;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tymoore on 11/18/2017.
 */

public class User {
    // FIELDS
    public String userID;
    public boolean isHost;
    public String userRoom;
    public String name;
    public HashMap<String, Integer> songVote;

    // CONSTRUCTORS

    public User(){

    }
    public User(String userID, String name, boolean isHost, String userRoom){
        this.userID=userID;
        this.isHost=isHost;
        this.userRoom=userRoom;
        this.name=name;
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
    public void setUserRoom(String userRoom) {
        this.userRoom = userRoom;
    }
    public void setSongVote(HashMap<String, Integer> songVote) {
        this.songVote = songVote;
    }

    public void addSongVote(String name, Integer vote) {
        this.songVote.put(name, vote);
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
        return this.userRoom;
    }
    public HashMap<String, Integer> getSongVote() {
        return songVote;
    }




    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", isHost=" + isHost + '\'' +
                ", roomID=" + userRoom + '\'' +
                '}';
    }

    static public User getUserFromID(String userID, ArrayList<User> users){
        for(User u: users){
            Log.d("USERs", "getUserFromID: "+userID + " " +u.toString());
            if(u.getUserID().equals(userID)){
                return u;
            }
        }
        return null;
    }
}
