package com.collabify.collabify;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.collabify.collabify.R;

/**
 * Created by gillianpierce on 2017-11-18.
 */

public class CustomRecyclerViewHolder extends ViewHolder {
    private TextView title, artist, votes;
    private ImageView artwork;
    private ImageButton up, down;
    public CustomRecyclerViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.listTitle);
        artist = (TextView)itemView.findViewById(R.id.listDescription);
        artwork = (ImageView)itemView.findViewById(R.id.listImage);
        votes = (TextView)itemView.findViewById(R.id.votes);
        up = (ImageButton)itemView.findViewById(R.id.upVote);
        down = (ImageButton)itemView.findViewById(R.id.downVote);

    }
}
