package com.collabify.collabify;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by gillianpierce on 2017-11-18.
 */

public class Song {
    private String title, artist;
    private String imageURL;
    private Integer votes;
    private String uri;
    private Integer positionInMs;

    public Song(){

    }

    public Song(String title, String artist, Integer votes, String uri, String imageURL, Integer positionInMs){
        this.title = title;
        this.artist = artist;
        this.imageURL = imageURL;
        this.votes = votes;
        this.uri = uri;
        this.positionInMs = positionInMs;
    }


    //setters
    public void setTitle(String title){
        this.title = title;
    }
    public void setArtist(String artist){
        this.artist = artist;
    }
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
    public void setVotes(Integer votes){
        this.votes = votes;
    }
    public void setUri(String uri){
        this.uri = uri;
    }
    public void setPositionInMs(Integer positionInMs){
        this.positionInMs = positionInMs;
    }

    //getters
    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }
    public Integer getVotes(){
        return votes;
    }
    public String getUri(){
        return uri;
    }
    public String getImageURL(){
        return imageURL;
    }
    public Integer getPositionInMs() {
        return positionInMs;
    }

    @Override
    public String toString() {
        return "User{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", votes=" + votes + '\'' +
                ", uri=" + uri + '\'' +
                ", PosInMs=" + positionInMs + '\'' +
                '}';
    }


    static public Song getSongFromID(String uri, ArrayList<Song> songs){
        for(Song s: songs){
            if(s.uri.equals(uri)){
                return s;
            }
        }
        return null;
    }


}
