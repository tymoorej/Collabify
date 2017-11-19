package com.collabify.collabify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueueActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    private List<RecyclerViewClass> mItems;
    private List<String> mUris;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean isHost;
    private boolean isPlaying = true;
    private ImageButton playButton;
    private ArrayList<? extends Song> SongList;
    private Button refreshButton;
    ArrayList<User> users=new ArrayList<>();
    ArrayList<Room> rooms=new ArrayList<>();
    ArrayList<Song> songs;
    Database data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        data = new Database(this);
        data.readData(users, rooms);
        Intent intent = getIntent();
        TextView roomID = findViewById(R.id.RoomID);
        isHost = intent.getBooleanExtra(HostAndJoinActivity.IS_HOST, false);
        String ID;
        if(isHost){
            ID = intent.getStringExtra(HostAndJoinActivity.EXTRA_MESSAGE);
            roomID.setText(ID);
        } else{
            ID = intent.getStringExtra(EnterIDActivity.EXTRA_MESSAGE);
            roomID.setText(ID);
        }
        Button addSong = (Button)findViewById(R.id.addSong);
        playButton = (ImageButton)findViewById(R.id.playButton);
        refreshButton = (Button)findViewById(R.id.refreshButton);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room r = Room.getRoomFromID("-KzHKTTBKiazjmImP-KR", rooms);
                Log.d("help", r.toString());
                songs = r.songs;
                refreshButton.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });



        mItems = new ArrayList<>();
        //adding test items to the list
        mAdapter = new CustomAdapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        mUris = new ArrayList<String>();
        Collections.addAll(mUris, new String[]{"spotify:track:5PX4uS1LqlWEPL69phPVQQ", "spotify:track:1yKabXYK0QxNwgCeEJkREV", "spotify:track:6RD9GItAGZ3gbUbx14okHF"});
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItems.add(new RecyclerViewClass("title", " artist", 0, mUris.remove(mUris.size() - 1)));
                mAdapter.notifyDataSetChanged();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HALP",rooms.toString());
                if(isPlaying){
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                }
                isPlaying = !isPlaying;
            }
        });



    }

    public void doClick(){
        refreshButton.performClick();
    }
}
