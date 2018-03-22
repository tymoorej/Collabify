
package com.collabify.collabify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback, FriendAdapter.ItemClickListener{

    private SpotifyPlayer mPlayer;
    public static final int REQUEST_CODE = 1;
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
    public static User host = new User();
    private DrawerLayout mDrawerLayout;
    private RecyclerView friendsRecyclerView;
    private LinearLayoutManager friendLayoutManager;
    private FriendAdapter friendAdapter;
    private List<String> friendList= new ArrayList<String>();
    private List<String> selectedFriendList= new ArrayList<String>();
    private Button addFriends;


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
        Log.d(TAG, "onCreate: ");

        Intent intent = getIntent();

        userID = intent.getStringExtra(USER);
        ID = intent.getStringExtra(ROOM_NAME);
        Token = intent.getStringExtra(TOKEN);

        // Adding toolbar so side drawer can be opened from top of page
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //Handling when users click on stuff inside the drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

        friendList.add("friend1");
        friendList.add("friend2");
        friendList.add("friend3");
        friendList.add("friend4");
        friendList.add("friend5");
        friendList.add("friend6");
        friendsRecyclerView = (RecyclerView) findViewById(R.id.friends_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        friendsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        friendLayoutManager = new LinearLayoutManager(this);
        friendsRecyclerView.setLayoutManager(friendLayoutManager);

        // specify an adapter (see also next example)
        friendAdapter = new FriendAdapter(this, friendList);
        friendAdapter.setClickListener(this);
        friendsRecyclerView.setAdapter(friendAdapter);

        addFriends = findViewById(R.id.add_friends);


        Log.d("QueueActivity", "onCreate Intents: " + Token +"\n " + ID + "\n "+ userID);

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
                Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
            }
        });

        playButton = (ImageButton)findViewById(R.id.playButton);
        addSong = (Button)findViewById(R.id.addSong);
        refreshButton = (Button)findViewById(R.id.refreshButton);
        skipButton = (ImageButton)findViewById(R.id.skip_button);
        toDelete = (ImageButton)findViewById(R.id.toDeleteButton);

        if (mPlayer.getPlaybackState().isPlaying){
            TextView title = findViewById(R.id.textView4);
            TextView artist = findViewById(R.id.textView5);
            ImageView image = findViewById(R.id.imageView);
            title.setText((CharSequence) nowPlaying.getTitle());
            artist.setText((CharSequence) nowPlaying.getArtist());
            new DownloadImageTask(image).execute(nowPlaying.getImageURL());
            playButton.setImageResource(android.R.drawable.ic_media_pause);
        } else{
            if(nowPlaying != null && currentRoom.getCurrentlyPlaying() != null) {
                TextView title = findViewById(R.id.textView4);
                TextView artist = findViewById(R.id.textView5);
                ImageView image = findViewById(R.id.imageView);
                title.setText((CharSequence) currentRoom.getCurrentlyPlaying().getTitle());
                artist.setText((CharSequence) currentRoom.getCurrentlyPlaying().getArtist());
                new DownloadImageTask(image).execute(currentRoom.getCurrentlyPlaying().getImageURL());
            } else if (nowPlaying != null) {
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
                mPlayer.logout();

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
                        Log.d("UpClick", "onClick: "+holder.getAdapterPosition());
                        Log.d(TAG, "onClick: "+ u);

                        Integer v = mItems.get(holder.getAdapterPosition()).getVotes()+1;

                        if (u.getSongVote() == null && !u.getIsHost()) {
                            host = User.getUserFromID(currentRoom.getHostID(), users);
                            u.setSongVote(host.getSongVote());
                            u.resetSongVote();
                        } else if (u.getSongVote() == null) {
                            u.setSongVote(new HashMap<String, Integer>());
                        }
                        for (Song song: currentRoom.getSongs()) {
                            if (!u.getSongVote().containsKey(song.getTitle())) {
                                u.getSongVote().put(song.getTitle(), 0);
                            }
                        }
//                        else {
//                            u = User.getUserFromID(userID, users);
//                        }

                        if (!u.getSongVote().containsKey(mItems.get(holder.getAdapterPosition()).getTitle()) ||
                                u.getSongVote().get(mItems.get(holder.getAdapterPosition()).getTitle()) < 1) {
                            mItems.get(holder.getAdapterPosition()).setVotes(v);

                            Log.d(TAG, "SongDude: "+mItems.get(holder.getAdapterPosition()));
                            Log.d(TAG, "onClick: "+u.getSongVote());
                            Log.d(TAG, "onClick: "+ mItems.get(holder.getAdapterPosition()).getTitle());
                            if (u.getSongVote().get(mItems.get(holder.getAdapterPosition()).getTitle()) == -1) {
                                u.getSongVote().put(mItems.get(holder.getAdapterPosition()).getTitle(), 0);
                            } else if (u.getSongVote().get(mItems.get(holder.getAdapterPosition()).getTitle()) == 0) {
                                u.getSongVote().put(mItems.get(holder.getAdapterPosition()).getTitle(), 1);
                            }
                            Collections.sort(mItems, new VoteComparator());
                            data.updateUser(u);
                            data.updateRoomSongs(mItems, currentRoom);
                            notifyDataSetChanged();
                        }

                    }
                });

                holder.getDown().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("DownClick", "onClick: "+holder);
                        Integer v = mItems.get(holder.getAdapterPosition()).getVotes()-1;

                        if (u.getSongVote() == null && !u.getIsHost()) {
                            host = User.getUserFromID(currentRoom.getHostID(), users);
                            u.setSongVote(host.getSongVote());
                            u.resetSongVote();
                        }

                        for (Song song: currentRoom.getSongs()) {
                            if (!u.getSongVote().containsKey(song.getTitle())) {
                                u.getSongVote().put(song.getTitle(), 0);
                            }
                        }

                        if (!u.getSongVote().containsKey(mItems.get(holder.getAdapterPosition()).getTitle()) ||
                                u.getSongVote().get(mItems.get(holder.getAdapterPosition()).getTitle()) > -1) {
                            mItems.get(holder.getAdapterPosition()).setVotes(v);

                            if (u.getSongVote().get(mItems.get(holder.getAdapterPosition()).getTitle()) == 1) {
                                u.getSongVote().put(mItems.get(holder.getAdapterPosition()).getTitle(), 0);
                            } else if (u.getSongVote().get(mItems.get(holder.getAdapterPosition()).getTitle()) == 0) {
                                u.getSongVote().put(mItems.get(holder.getAdapterPosition()).getTitle(), -1);
                            }
                            Collections.sort(mItems, new VoteComparator());
                            data.updateUser(u);
                            data.updateRoomSongs(mItems, currentRoom);
                            notifyDataSetChanged();
                        }
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

                    refreshButton.performClick();
                    if (currentRoom.getSongs() != null) {
                        HashSet<Song> songCheck = new HashSet<>(currentRoom.getSongs());
                        if (!songCheck.contains(newSong)) {
                            currentRoom.addRoomSong(newSong);
                        }
                    } else {
                        currentRoom.setRoomSongs(new ArrayList<Song>());
                        currentRoom.addRoomSong(newSong);
                    }
                    mItems.add(newSong);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        // Populate now playing
        Query nowPlayingQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("Rooms")
                .child(ID)
                .child("currentlyPlaying");

        nowPlayingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                TextView title = findViewById(R.id.textView4);
                TextView artist = findViewById(R.id.textView5);
                ImageView image = findViewById(R.id.imageView);
                if (currentRoom != null && dataSnapshot.child("title") != null){
                    title.setText((CharSequence) dataSnapshot.child("title").getValue(String.class));
                    artist.setText((CharSequence) dataSnapshot.child("artist").getValue(String.class));
                    new DownloadImageTask(image).execute(dataSnapshot.child("imageURL").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value. nowPlay", error.toException());
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
                playButton.setEnabled(true);
                skipButton.setEnabled(true);

                currentRoom = Room.getRoomFromID(ID, rooms);
                u = User.getUserFromID(userID, users);
                Log.d("QueueActivityRefresh", "onClick: "+currentRoom+" "+u);

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


                startActivityForResult(intent, 1);

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
                        skipButton.performClick();
                    }
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (mItems.size() != 0) {
                    Log.d("PLAAY", mItems.get(0).getUri());
                    mPlayer.playUri(null, mItems.get(0).getUri(), 0, 0); //2TpxZ7JUBn3uw46aR7qd6V
                    if(mPlayer.getPlaybackState().isPlaying){
                        playButton.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        playButton.setImageResource(android.R.drawable.ic_media_pause);
                    }
                    TextView title = findViewById(R.id.textView4);
                    TextView artist = findViewById(R.id.textView5);
                    ImageView image = findViewById(R.id.imageView);
                    title.setText((CharSequence) mItems.get(0).getTitle());
                    artist.setText((CharSequence) mItems.get(0).getArtist());
                    new DownloadImageTask(image).execute(mItems.get(0).getImageURL());
                    nowPlaying = mItems.get(0);
                    mItems.remove(0);
                    currentRoom.setCurrentlyPlaying(nowPlaying);
                    //Databoos
                    data.updateUserVotes(mItems, u);
                    data.updateRoomSongs(mItems, currentRoom);

                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        //data.readData(users, rooms);
        //doClick();
        mRecyclerView.setVisibility(View.INVISIBLE);
        mAdapter.startListening();
        doClick();

    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        currentRoom.setHostID(null);
        data.updateChild(Room.class, currentRoom.getRoomID(), currentRoom);
    }

    public void doClick(){
        //refreshButton.callOnClick();
        refreshButton.performClick();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + friendAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        view.setBackgroundColor(getResources().getColor(R.color.grey));
        selectedFriendList.add(friendAdapter.getItem(position));
        Log.d("selectedFriends:" , selectedFriendList.toString());
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
            case kSpPlaybackNotifyAudioDeliveryDone:
                skipButton.performClick();
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

    // Call Back method  to get the Songs form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(SearchActivity.MESSAGE);
                // do something with the result
                Log.d(TAG, "confirmation");

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }
        }
    }

}



