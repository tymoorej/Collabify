package com.collabify.collabify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by gillianpierce on 2017-11-18.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomRecyclerViewHolder> {
    List<Song> mItems;
    Context mContext;

    public CustomAdapter(Context context, List<Song> items){
        mContext = context;
        mItems = items;
    }

    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.layout, parent, false);
        //set the margin if any, will be discussed in next blog
        return new CustomRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerViewHolder holder, int position) {
        //holder.artwork.setImageDrawable(mItems.get(position).getArtwork());
        holder.title.setText(mItems.get(position).getTitle());
        holder.artist.setText(mItems.get(position).getArtist());
        holder.votes.setText(""+mItems.get(position).getVotes());
        new DownloadImageTask(holder.artwork).execute(mItems.get(position).getImageURL());

        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song currentTop = mItems.get(0);
                Integer v = mItems.get(holder.getAdapterPosition()).getVotes()+1;
                mItems.get(holder.getAdapterPosition()).setVotes(v);
                Collections.sort(mItems, new VoteComparator());
                if(mItems.get(0)!= currentTop)  {
                    
                }
                notifyDataSetChanged();
            }
        });

        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer v = mItems.get(holder.getAdapterPosition()).getVotes()-1;
                mItems.get(holder.getAdapterPosition()).setVotes(v);
                Collections.sort(mItems, new VoteComparator());
                notifyDataSetChanged();
            }
        });


    }
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {
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


}
