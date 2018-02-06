package com.collabify.collabify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by gillianpierce on 2017-11-18.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomRecyclerViewHolder> {
    public static final String TAG = "Queue:CustomAdapter";
    List<Song> mItems;
    Context mContext;

    public Database d = QueueActivity.data;
    User u = QueueActivity.u;
    Room r = QueueActivity.currentRoom;

    Set<Song> votedSongs= new HashSet<>();


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
    //TODO:Make it so that you can only vote once
    @Override
    public void onBindViewHolder(final CustomRecyclerViewHolder holder, int position) {
        //holder.artwork.setImageDrawable(mItems.get(position).getArtwork());
        holder.title.setText(mItems.get(position).getTitle());
        holder.artist.setText(mItems.get(position).getArtist());
        holder.votes.setText(""+mItems.get(position).getVotes());
        new DownloadImageTask(holder.artwork).execute(mItems.get(position).getImageURL());
        Log.d(TAG, "onDownClick: "+u + r);


        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UpClick", "onClick: "+holder);
                Integer v = mItems.get(holder.getAdapterPosition()).getVotes()+1;
                mItems.get(holder.getAdapterPosition()).setVotes(v);
                Collections.sort(mItems, new VoteComparator());

                Log.d(TAG, "onUpClick: "+u + r);
                notifyDataSetChanged();
            }
        });

        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DownClick", "onClick: "+holder);
                Integer v = mItems.get(holder.getAdapterPosition()).getVotes()-1;
                mItems.get(holder.getAdapterPosition()).setVotes(v);
                Collections.sort(mItems, new VoteComparator());

                Log.d(TAG, "onDownClick: "+u + r);
                notifyDataSetChanged();
            }
        });


    }
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<Song> updateSongList(List<Song> mItems) {
        List<Song> songs = r.getSongs();

        songs = mItems;
        d.updateChild(r.getClass(), r.getRoomID(), r);

        return songs;
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
