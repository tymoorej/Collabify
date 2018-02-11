package com.collabify.collabify;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Tymoore on 11/18/2017.
 */

public class Room {
    // FIELDS
    String roomID;
    String hostID;
    String roomName;
    ArrayList<Song> songs;

    // CONSTRUCTORS

    public Room(String roomName, String roomID, String hostID, ArrayList<Song> songs) {
        this.roomName = roomName;
        this.roomID = roomID;
        this.hostID = hostID;
        this.songs = songs;
    }

    public Room(){

    }

    // SETTERS
    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public void setHostID(String hostID) {
        this.hostID = hostID;
    }
    public void setRoomSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public void addRoomSong(Song song) {
        this.songs.add(song);
    }

    // GETTERS
    public String getRoomID() {
        return this.roomID;
    }
    public String getRoomName() {
        return this.roomName;
    }
    public String getHostID() {
        return this.hostID;
    }
    public ArrayList<Song> getSongs() {
        return this.songs;
    }


    @Override
    public String toString() {
        return "Room{" +
                "roomID='" + roomID + '\'' +
                ", hostID='" + hostID + '\'' +
                ", songs=" + songs +
                '}';
    }

    static public Room getRoomFromID(String roomID, ArrayList<Room> rooms){
        for(Room r: rooms){
            Log.d("ROOM.java", "getRoomFromID: "+ roomID +" "+r.toString());
            if(r.getRoomID().equals(roomID)){
                Log.d("ROOM.java", "getRoomFromID - This one: "+r.toString());
                return r;
            }
        }
        return null;
    }
}
