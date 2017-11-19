package com.collabify.collabify;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Tymoore on 11/18/2017.
 */

public class Song {
    String imageURL;
    String songURL;
    int votes;
    String roomID;
    String userID;
    String title;
    String artist;
    String songID;


    public Song(String imageURL, String songURL, int votes, String roomID, String userID,
                String title, String artist, String songID) {
        this.imageURL = imageURL;
        this.songURL = songURL;
        this.votes = votes;
        this.roomID = roomID;
        this.userID = userID;
        this.title = title;
        this.artist = artist;
        this.songID=songID;
    }

    public Song() {
    }

    @Override
    public String toString() {
        return String.valueOf(songID);
    }
}
