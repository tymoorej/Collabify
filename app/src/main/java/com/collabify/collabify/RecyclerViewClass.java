package com.collabify.collabify;

import android.graphics.drawable.Drawable;

/**
 * Created by gillianpierce on 2017-11-18.
 */

public class RecyclerViewClass {
    private String title, artist;
    private Drawable artwork;
    private Integer votes;
    private String uri;


    public RecyclerViewClass(String title, String artist, Integer votes, String uri){
        this.title = title;
        this.artist = artist;
        //this.artwork = artwork;
        this.votes = votes;
        this.uri = uri;
    }


    //setters
    public void setTitle(String title){
        this.title = title;
    }
    public void setArtist(String artist){
        this.artist = artist;
    }
    //public void setmImage_url(Drawable artwork){
        //this.artwork = artwork;
    //}
    public void setVotes(Integer votes){this.votes = votes;}
    public void setUri(String uri){this.uri = uri;}

    //getters
    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }
    public Integer getVotes(){ return votes;}
    public String getUri(){return uri;}
    //public Drawable getArtwork(){
        //return artwork;
    //}

}
