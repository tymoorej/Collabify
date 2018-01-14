
package com.collabify.collabify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    //private CustomAdapter mAdapter;
    private FirebaseRecyclerAdapter mAdapter;
    public static List<Song> mItems = new ArrayList<>();
    private List<String> mUris;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean isHost;
    private ImageButton playButton;
    private ImageButton skipButton;
    private Button addSong;
    private ImageButton toDelete;
    private ArrayList<? extends Song> SongList;
    private Button refreshButton;
    public ArrayList<User> users=new ArrayList<>();
    public ArrayList<Room> rooms=new ArrayList<>();
    public ArrayList<Song> songs;
    public static Database data;
    public TextView roomID;
    public String roomIDText;
    public static Room currentRoom = new Room();
    public String userID;
    public String ID;
    public String Token;
    public static final String TAG = "QueueActivity";
    public static final String TOKEN = "com.collabify.collabify.TOKEN";
    public static final String ROOM_NAME = "com.collabify.collabify.MESSAGE";
    public static final String USER = "com.collabify.collabify.USER";
    public static final String IS_HOST = "com.collabify.collabify.HOST";
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

    /*public boolean songFinished(){
        if(mPlayer.getPlaybackState().positionMs == mPlayer.getMetadata().currentTrack.durationMs){
            return true;
        } return false;
    }*/

    public void playNextSong(){
        if(mItems.size()!=0){
            Log.d("PLAAY", mItems.get(0).getUri());
            TextView title = findViewById(R.id.textView4);
            TextView artist = findViewById(R.id.textView5);
            ImageView image = findViewById(R.id.imageView);
            title.setText((CharSequence) mItems.get(0).getTitle());
            artist.setText((CharSequence) mItems.get(0).getArtist());
            new DownloadImageTask(image).execute(mItems.get(0).getImageURL());
            nowPlaying = mItems.get(0);
            mRecyclerView.setAdapter(mAdapter);
            mPlayer.playUri(null, nowPlaying.getUri(), 0, 0); //2TpxZ7JUBn3uw46aR7qd6V
            mItems.remove(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        data = new Database(this);
        data.readData(users, rooms);
        Log.d(TAG, "onCreate: ");

        Intent intent = getIntent();

        userID = intent.getStringExtra(USER);
        ID = intent.getStringExtra(ROOM_NAME);
        Token = intent.getStringExtra(TOKEN);





        Log.d("QueueActivity", "onCreate Intents: " + Token +"\n " + ID + "\n "+ userID);


        Config playerConfig = new Config(this, Token, MainActivity.getClientId());
        Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                mPlayer = spotifyPlayer;
                mPlayer.addConnectionStateCallback(QueueActivity.this);
                mPlayer.addNotificationCallback(QueueActivity.this);
                //mPlayer.getPlaybackState().positionMs;
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
            }
        });

        playButton = (ImageButton)findViewById(R.id.playButton);
        addSong = (Button)findViewById(R.id.addSong);
        refreshButton = (Button)findViewById(R.id.refreshButton);
        skipButton = (ImageButton)findViewById(R.id.skip_button);
        toDelete = (ImageButton)findViewById(R.id.toDeleteButton);

        if(mPlayer.getPlaybackState().isPlaying){

            TextView title = findViewById(R.id.textView4);
            TextView artist = findViewById(R.id.textView5);
            ImageView image = findViewById(R.id.imageView);
            title.setText((CharSequence) nowPlaying.getTitle());
            artist.setText((CharSequence) nowPlaying.getArtist());
            new DownloadImageTask(image).execute(nowPlaying.getImageURL());
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
            playButton.setImageResource(android.R.drawable.ic_media_play);
        }
        final TextView roomID = findViewById(R.id.RoomID);
        isHost = intent.getBooleanExtra(IS_HOST, false);
        Log.d("QueueHOST", "is host? "+isHost);
        roomID.setText(ID);

        if(isHost){
            playButton.setVisibility(View.VISIBLE);
            skipButton.setVisibility(View.VISIBLE);
            toDelete.setVisibility(View.VISIBLE);
        } else{
            playButton.setVisibility(View.INVISIBLE);
            skipButton.setVisibility(View.INVISIBLE);
            toDelete.setVisibility(View.INVISIBLE);
        }

        toDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
                Log.d("QueueActivity", "toDelete: " + Token +"\n " + userID + "\n "+ ID);
                intent.putExtra(TOKEN, Token);
                intent.putExtra(ROOM_NAME, ID);
                intent.putExtra(USER, userID);
                intent.putExtra(IS_HOST, isHost);

                startActivity(intent);
                //mItems.add(new RecyclerViewClass("title", " artist", 0, mUris.remove(mUris.size() - 1)));
                //mAdapter.notifyDataSetChanged();
            }
        });

        // TEST FIREBASE UI STUFFS

        Query songQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("Rooms")
                .child(ID)
                .child("songs");

        FirebaseRecyclerOptions<Song> options =
                new FirebaseRecyclerOptions.Builder<Song>()
                        .setQuery(songQuery, Song.class)
                        .build();

         mAdapter = new FirebaseRecyclerAdapter<Song, CustomRecyclerViewHolder>(options) {

            @Override
            public CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // create a new view
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout, parent, false);
                //set the margin if any, will be discussed in next blog
                return new CustomRecyclerViewHolder(v);
            }

            //TODO:Make it so that you can only vote once

            @Override
            protected void onBindViewHolder(final @NonNull com.collabify.collabify.CustomRecyclerViewHolder holder, int position, @NonNull Song model) {
                //holder.artwork.setImageDrawable(mItems.get(position).getArtwork());
                holder.getTitle().setText(model.getTitle());
                holder.getArtist().setText(model.getArtist());
                holder.getVotes().setText(""+model.getVotes());
                new DownloadImageTask(holder.getArtwork()).execute(model.getImageURL());


                holder.getUp().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("UpClick", "onClick: "+holder);
                        Integer v = mItems.get(holder.getAdapterPosition()).getVotes()+1;
                        mItems.get(holder.getAdapterPosition()).setVotes(v);
                        Collections.sort(mItems, new VoteComparator());
                        data.updateRoom(mItems, currentRoom);
                        notifyDataSetChanged();
                    }
                });

                holder.getDown().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("DownClick", "onClick: "+holder);
                        Integer v = mItems.get(holder.getAdapterPosition()).getVotes()-1;
                        mItems.get(holder.getAdapterPosition()).setVotes(v);
                        Collections.sort(mItems, new VoteComparator());
                        data.updateRoom(mItems, currentRoom);
                        notifyDataSetChanged();
                    }
                });
            }
        };

         // Populate the datas

        songQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                mItems = new ArrayList<Song>();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {

                    Song oldSong = dataSnapshot1.getValue(Song.class);
                    Song newSong = new Song();

                    newSong.setUri(oldSong.getUri());
                    newSong.setTitle(oldSong.getTitle());
                    newSong.setArtist(oldSong.getArtist());
                    newSong.setPositionInMs(oldSong.getPositionInMs());
                    newSong.setVotes(oldSong.getVotes());
                    newSong.setImageURL(oldSong.getImageURL());
                    mItems.add(newSong);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });



        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //doClick();
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Buttons not available until refresh
                mRecyclerView.setVisibility(View.VISIBLE);
                playButton.setEnabled(true);
                skipButton.setEnabled(true);

                currentRoom = Room.getRoomFromID(ID, rooms);
                u = User.getUserFromID(userID, users);
                Log.d("QueueActivityRefresh", "onClick: "+currentRoom+" "+u);
//                data.searchUser(userID,u);data.searchRoom(ID,currentRoom);
                if ((u != null) && (currentRoom != null)) {
                    Log.d("refresh button", currentRoom.toString());
                    Log.d("refresh button", u.toString());
                    songs = currentRoom.songs;
                    refreshButton.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        //doClick();
        Log.d("read data", "onCreate: " + users.size() + "<- user, room -> " + rooms.size());


        //mAdapter = new CustomAdapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                Log.d("QueueActivity", "addSong Intents: " + Token +"\n " + userID + "\n "+ ID);
                intent.putExtra(TOKEN, Token);
                intent.putExtra(ROOM_NAME, ID);
                intent.putExtra(USER, userID);
                intent.putExtra(IS_HOST, isHost);

                startActivity(intent);
                //mItems.add(new RecyclerViewClass("title", " artist", 0, mUris.remove(mUris.size() - 1)));
                //mAdapter.notifyDataSetChanged();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PlayButton", String.valueOf(mPlayer.getPlaybackState().isPlaying));
                if(mPlayer.getPlaybackState().isPlaying){
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
                        mPlayer.skipToNext(mOperationCallback);
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
                        data.updateRoom(mItems, currentRoom);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
                       mPlayer.skipToNext(mOperationCallback);
                    }


        });

        skipButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (mItems.size() != 0) {
                    /*Log.d("PLAAY", mItems.get(0).getUri());
                    mPlayer.playUri(null, mItems.get(0).getUri(), 0, 0); //2TpxZ7JUBn3uw46aR7qd6V
                    TextView title = findViewById(R.id.textView4);
                    TextView artist = findViewById(R.id.textView5);
                    ImageView image = findViewById(R.id.imageView);
                    title.setText((CharSequence) mItems.get(0).getTitle());
                    artist.setText((CharSequence) mItems.get(0).getArtist());
                    new DownloadImageTask(image).execute(mItems.get(0).getImageURL());
                    nowPlaying = mItems.get(0);
                    mItems.remove(0);
                    data.updateRoom(mItems, currentRoom);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);*/
                    mPlayer.skipToNext(mOperationCallback);
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

    @Override
    public void onStart() {
        super.onStart();
        //data.readData(users, rooms);
        //doClick();
        mRecyclerView.setVisibility(View.INVISIBLE);
        mAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    public void doClick(){
        refreshButton.callOnClick();
    }

    @Override
    public void onLoggedIn() {
        Log.d("QueueActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("QueueActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error var1) {
        Log.d("QueueActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("QueueActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("QueueActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("QueueActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            case kSpPlaybackNotifyTrackChanged:
                StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
                String cls = stackTraceElements[1].getClassName();
                int lineNum = stackTraceElements[1].getLineNumber();
                Log.d("whaaaaat is in da queue", mItems.toString());
                //playNextSong();
                //Log.d("whaaaaat is in da queue", mItems.toString());
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("QueueActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

}



