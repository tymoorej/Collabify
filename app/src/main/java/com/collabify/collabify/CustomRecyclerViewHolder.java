package com.collabify.collabify;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by gillianpierce on 2017-11-18.
 */

public class CustomRecyclerViewHolder extends ViewHolder {
    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getArtist() {
        return artist;
    }

    public void setArtist(TextView artist) {
        this.artist = artist;
    }

    public TextView getVotes() {
        return votes;
    }

    public void setVotes(TextView votes) {
        this.votes = votes;
    }

    public ImageView getArtwork() {
        return artwork;
    }

    public void setArtwork(ImageView artwork) {
        this.artwork = artwork;
    }

    public ImageButton getUp() {
        return up;
    }

    public void setUp(ImageButton up) {
        this.up = up;
    }

    public ImageButton getDown() {
        return down;
    }

    public void setDown(ImageButton down) {
        this.down = down;
    }

    private TextView title, artist, votes;
    private ImageView artwork;
    private ImageButton up, down;
    public CustomRecyclerViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.trackTitle);
        artist = (TextView)itemView.findViewById(R.id.listDescription);
        artwork = (ImageView)itemView.findViewById(R.id.listImage);
        votes = (TextView)itemView.findViewById(R.id.votes);
        up = (ImageButton)itemView.findViewById(R.id.upVote);
        down = (ImageButton)itemView.findViewById(R.id.downVote);

    }
}
