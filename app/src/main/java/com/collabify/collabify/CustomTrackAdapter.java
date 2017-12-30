package com.collabify.collabify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gillianpierce on 2017-11-19.
 */

public class CustomTrackAdapter extends RecyclerView.Adapter<CustomTrackAdapter.CustomRecyclerViewHolder>{
    List<Song> mItems;
    Context mContext;

    public CustomTrackAdapter(Context context, List<Song> items){
        mContext = context;
        mItems = items;
    }

    @Override
    public CustomTrackAdapter.CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.layout2, parent, false);
        //set the margin if any, will be discussed in next blog
        return new CustomTrackAdapter.CustomRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomTrackAdapter.CustomRecyclerViewHolder holder, int position) {
        holder.title.setText(mItems.get(position).getTitle());
        holder.artist.setText(mItems.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView title, artist;

        public CustomRecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.trackTitle);
            artist = (TextView)itemView.findViewById(R.id.listDescription);

        }
}}
