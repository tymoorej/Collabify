
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

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class QueueActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback{

    private SpotifyPlayer mPlayer;

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
    public static final String TOKEN = "com.collabify.collabify.TOKEN";

    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.d("Callback", "OK!");
        }

        @Override
        public void onError(Error error) {
            Log.d("Error","ERROR:" + error);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        data = new Database(this);
        data.readData(users, rooms);
        Intent intent = getIntent();

        String ID = intent.getStringExtra(EnterIDActivity.EXTRA_MESSAGE);
        final String Token = intent.getStringExtra(HostAndJoinActivity.TOKEN);

        Config playerConfig = new Config(this, Token, MainActivity.getClientId());
        Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                mPlayer = spotifyPlayer;
                mPlayer.addConnectionStateCallback(QueueActivity.this);
                mPlayer.addNotificationCallback(QueueActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });

        TextView roomID = findViewById(R.id.RoomID);
        isHost = intent.getBooleanExtra(HostAndJoinActivity.IS_HOST, false);
        //String ID;
        if(isHost){
            ID = intent.getStringExtra(HostAndJoinActivity.ROOM_NAME);
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
        String[] songJustAdded = intent.getStringArrayExtra(SearchActivity.ADDED_SONG);
        if(songJustAdded != null) {
            if(songJustAdded.length != 0) {
                mItems.add(new RecyclerViewClass(songJustAdded[1], songJustAdded[0], 0, songJustAdded[2]));
            }
        }
        mAdapter = new CustomAdapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        mUris = new ArrayList<String>();
        Collections.addAll(mUris, new String[]{"spotify:track:5PX4uS1LqlWEPL69phPVQQ", "spotify:track:1yKabXYK0QxNwgCeEJkREV", "spotify:track:2TpxZ7JUBn3uw46aR7qd6V"});
        mAdapter.notifyDataSetChanged();
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(TOKEN, Token);
                startActivity(intent);
                //mItems.add(new RecyclerViewClass("title", " artist", 0, mUris.remove(mUris.size() - 1)));
                //mAdapter.notifyDataSetChanged();
            }
        });

        //TODO: Add a skip to next track button and change the current functionaliy of the buttons
        //TODO: Also make the add song button go to the add song activity etc.
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                    if (mItems.size() != 0) {
                        Log.d("PLAAY", mItems.get(0).getUri());
                        mPlayer.playUri(null, mItems.get(0).getUri(), 0, 0); //2TpxZ7JUBn3uw46aR7qd6V
                        mItems.remove(0);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                    mPlayer.pause(mOperationCallback);
                    if (mItems.size() != 0) {
                        Log.d("PAUSE", mItems.get(0).getUri());
                    }
                }
                isPlaying = !isPlaying;
            }
        });



    }

    public void doClick(){
        refreshButton.performClick();
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error var1) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

}



