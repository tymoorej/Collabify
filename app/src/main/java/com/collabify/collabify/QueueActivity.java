
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
import android.widget.ImageView;
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
    public static List<Song> mItems = new ArrayList<>();
    private List<String> mUris;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean isHost;
    private boolean isPlaying;
    private ImageButton playButton;
    private ImageButton skipButton;
    private ArrayList<? extends Song> SongList;
    private Button refreshButton;
    public ArrayList<User> users=new ArrayList<>();
    public ArrayList<Room> rooms=new ArrayList<>();
    public ArrayList<Song> songs;
    public Database data;
    public TextView roomID;
    public String roomIDText;
    public Room currentRoom = new Room();
    public static final String TOKEN = "com.collabify.collabify.TOKEN";
    public static final String RID = "com.collabify.collabify.RID";
    public static final String ROOM_NAME = "com.collabify.collabify.MESSAGE";
    public static final String USER = "com.collabify.collabify.USER";
    public static Song nowPlaying;
    public static User u = new User();
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


        /* NOT NEEDED?
        roomID = (TextView)findViewById(R.id.RoomID);
        roomIDText = roomID.getText().toString();
        currentRoom = Room.getRoomFromID(roomIDText, rooms);
        */
        Intent intent = getIntent();
//        data.searchUser(intent.getStringExtra(USER),users);
//        Log.d("Queue", "onCreate: "+ users.size());

        String userID = intent.getStringExtra(USER);
        String ID = intent.getStringExtra(ROOM_NAME);
        String Token = intent.getStringExtra(TOKEN);

        final String t = Token;
        final String uID = userID;
        final String rID = ID;

        //String Token = intent.getStringExtra(HostAndJoinActivity.TOKEN);
        /*
        if(Token==null){
            Token = intent.getStringExtra(SearchActivity.TOKEN);
        }*/

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

        playButton = (ImageButton)findViewById(R.id.playButton);
        Button addSong = (Button)findViewById(R.id.addSong);
        refreshButton = (Button)findViewById(R.id.refreshButton);
        skipButton = findViewById(R.id.skip_button);

        if(isPlaying){
            TextView title = findViewById(R.id.textView4);
            TextView artist = findViewById(R.id.textView5);
            ImageView image = findViewById(R.id.imageView);
            title.setText((CharSequence) nowPlaying.getTitle());
            artist.setText((CharSequence) nowPlaying.getArtist());
            new DownloadImageTask(image).execute(nowPlaying.getImageURL());
            isPlaying = true;
            playButton.setImageResource(android.R.drawable.ic_media_pause);
        } else{
            if (nowPlaying != null) {
                TextView title = findViewById(R.id.textView4);
                TextView artist = findViewById(R.id.textView5);
                ImageView image = findViewById(R.id.imageView);
                title.setText((CharSequence) nowPlaying.getTitle());
                artist.setText((CharSequence) nowPlaying.getArtist());
                new DownloadImageTask(image).execute(nowPlaying.getImageURL());
            }
            isPlaying = false;
            playButton.setImageResource(android.R.drawable.ic_media_play);
        }
        final TextView roomID = findViewById(R.id.RoomID);
        isHost = intent.getBooleanExtra(HostAndJoinActivity.IS_HOST, false);
        Log.d("HOST", "is host? "+isHost);
        roomID.setText(ID);

        if(isHost){
            playButton.setVisibility(View.VISIBLE);
            skipButton.setVisibility(View.VISIBLE);
        } else{
            playButton.setVisibility(View.INVISIBLE);
            skipButton.setVisibility(View.INVISIBLE);
        }

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        doClick();
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentRoom = Room.getRoomFromID(rID, rooms);
                u = User.getUserFromID(uID, users);
                Log.d("QueueActivityRefresh", "onClick: "+currentRoom+" "+u);
//                data.searchUser(userID,u);data.searchRoom(ID,currentRoom);
                Log.d("refresh button", currentRoom.toString());
                Log.d("refresh button", u.toString());
                songs = currentRoom.songs;
                refreshButton.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        //doClick();
        Log.d("read data", "onCreate: " + users.size() + "<- user, room -> " + rooms.size());
        /*if(mItems == null) {
            mItems = new ArrayList<>();
        }
        //adding test items to the list
        String[] songJustAdded = intent.getStringArrayExtra(SearchActivity.ADDED_SONG);
        if(songJustAdded != null) {
            if(songJustAdded.length != 0) {
                mItems.add(new RecyclerViewClass(songJustAdded[1], songJustAdded[0], 0, songJustAdded[2], songJustAdded[3]));
            }
        }*/
        mAdapter = new CustomAdapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        mUris = new ArrayList<String>();
        Collections.addAll(mUris, new String[]{"spotify:track:5PX4uS1LqlWEPL69phPVQQ", "spotify:track:1yKabXYK0QxNwgCeEJkREV", "spotify:track:2TpxZ7JUBn3uw46aR7qd6V"});
        mAdapter.notifyDataSetChanged();

        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(TOKEN, t);
                intent.putExtra(RID, rID);
                intent.putExtra(USER, uID);
                Log.d("Queue to search", "onClick: "+uID);
                intent.putExtra(HostAndJoinActivity.IS_HOST, isHost);
                startActivity(intent);
                //mItems.add(new RecyclerViewClass("title", " artist", 0, mUris.remove(mUris.size() - 1)));
                //mAdapter.notifyDataSetChanged();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PlayButton", String.valueOf(isPlaying));
                if(isPlaying){
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                    mPlayer.pause(mOperationCallback);
                    //nowPlaying.setPositionInMs();
                    if (mItems.size() != 0) {
                        Log.d("PAUSE", mItems.get(0).getUri());
                    }
                } else {
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                    if (nowPlaying!=null) {
                        mPlayer.resume(mOperationCallback);
                    }
                    else if (mItems.size() != 0) {
                        Log.d("PLAAY", mItems.get(0).getUri());
                        mPlayer.playUri(null, mItems.get(0).getUri(), 0, 0); //2TpxZ7JUBn3uw46aR7qd6V
                        TextView title = findViewById(R.id.textView4);
                        TextView artist = findViewById(R.id.textView5);
                        ImageView image = findViewById(R.id.imageView);
                        title.setText((CharSequence) mItems.get(0).getTitle());
                        artist.setText((CharSequence) mItems.get(0).getArtist());
                        new DownloadImageTask(image).execute(mItems.get(0).getImageURL());
                        nowPlaying = mItems.get(0);
                        mItems.remove(0);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
                isPlaying = !isPlaying;
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (mItems.size() != 0) {
                    Log.d("PLAAY", mItems.get(0).getUri());
                    mPlayer.playUri(null, mItems.get(0).getUri(), 0, 0); //2TpxZ7JUBn3uw46aR7qd6V
                    TextView title = findViewById(R.id.textView4);
                    TextView artist = findViewById(R.id.textView5);
                    ImageView image = findViewById(R.id.imageView);
                    title.setText((CharSequence) mItems.get(0).getTitle());
                    artist.setText((CharSequence) mItems.get(0).getArtist());
                    new DownloadImageTask(image).execute(mItems.get(0).getImageURL());
                    nowPlaying = mItems.get(0);
                    mItems.remove(0);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });



    }

    public void doClick(){
        refreshButton.callOnClick();
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



