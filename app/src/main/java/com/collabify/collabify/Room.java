package com.collabify.collabify;

import java.util.ArrayList;

/**
 * Created by Tymoore on 11/18/2017.
 */

public class Room {
    String roomID;
    String hostID;
    ArrayList<Song> songs;

    public Room(String roomID, String hostID, ArrayList<Song> songs) {
        this.roomID = roomID;
        this.hostID = hostID;
        this.songs = songs;
    }

    public Room(){

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
            if(r.roomID.equals(roomID)){
                return r;
            }
        }
        return null;
    }
}
