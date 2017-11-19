package com.collabify.collabify;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Tymoore on 11/18/2017.
 */

public class Song {

    int votes;
    String songID;


    public Song(String songID, int votes) {
        this.songID=songID;
        this.votes=votes;
    }

    public Song() {
    }

    @Override
    public String toString() {
        return String.valueOf(songID) + "     " + String.valueOf(votes);
    }


    static public Song getSongFromID(String songID, ArrayList<Song> songs){
        for(Song s: songs){
            if(s.songID.equals(songID)){
                return s;
            }
        }
        return null;
    }

}
