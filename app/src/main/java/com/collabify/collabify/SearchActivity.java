package com.collabify.collabify;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


import kaaes.spotify.webapi.android.models.Track;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class SearchActivity extends AppCompatActivity {


    private Button search;
    private Button back;
    private ListView listOfSongs;
    //public static String LIST_SONGS = "com.collabify.collabify.fuqdupshizza";
    private EditText searchText;
    private List<Track> tracks;
    //public static String ADDED_SONG = "com.collabify.collabify.fuqdupshizza";
    public static final String TOKEN = "com.collabify.collabify.TOKEN";
    public static final String ROOM_NAME = "com.collabify.collabify.MESSAGE";
    public static final String USER = "com.collabify.collabify.USER";
    public static final String IS_HOST = "com.collabify.collabify.HOST";
    public Room currentRoom;
    public Database data;
    public String Token;
    public String uID;
    public String RID;
    public boolean isHost;
    public ArrayList<User> users=new ArrayList<>();
    public ArrayList<Room> rooms=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = new Database(this);
        data.readData(users, rooms);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();

        //final String listOfSongs = intent.getStringExtra(SearchActivity.LIST_SONGS);
        //String ID = intent.getStringExtra(EnterIDActivity.EXTRA_MESSAGE);

        Token = intent.getStringExtra(QueueActivity.TOKEN);
        uID = intent.getStringExtra(USER);
        RID = intent.getStringExtra(QueueActivity.ROOM_NAME);
        isHost = intent.getBooleanExtra(QueueActivity.IS_HOST, false);
        Log.d("SearchActivity:", "onCreate Intents: " + Token + "\n " + uID + "\n " + RID);


        search = (Button) findViewById(R.id.searchButton);
        searchText = (EditText) findViewById(R.id.searchText);
        listOfSongs = (ListView) findViewById(R.id.listView);
        back = findViewById(R.id.button2);

        ArrayList<Song> searchedSongs = new ArrayList<Song>();
        final MyAdapter mAdapter = new MyAdapter(this, searchedSongs);
        listOfSongs.setAdapter(mAdapter);

        search = (Button) findViewById(R.id.searchButton);
        searchText = (EditText) findViewById(R.id.searchText);
        listOfSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) adapterView.getItemAtPosition(i);
                String title = song.getTitle();
                String artist = song.getArtist();
                String uri = song.getUri();
                String imageURL = song.getImageURL();
                String[] values = {title, artist, uri, imageURL};

                currentRoom = Room.getRoomFromID(RID, rooms);
                Log.d("SearchActivity", "onCreate: " + currentRoom + " " + song.getTitle());

                if (currentRoom.getSongs() == null) {
                    currentRoom.setRoomSongs(new ArrayList<Song>(
                            Arrays.asList(song)));
                } else {
                    currentRoom.addRoomSong(song);
                }

                data.updateChild(currentRoom.getClass(), currentRoom.getRoomID(), currentRoom);
                QueueActivity.mItems.add(song);

                Intent intent = new Intent(getApplicationContext(), QueueActivity.class);
                //intent.putExtra(ADDED_SONG, values);
                intent.putExtra(TOKEN, Token);
                intent.putExtra(USER, uID);
                intent.putExtra(ROOM_NAME, RID);
                intent.putExtra(QueueActivity.IS_HOST, isHost);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                // mItems.add(new RecyclerViewClass("Title", "Artist", 0, "uri"));
                //mAdapter.notifyDataSetChanged();

                String trackToSearch = searchText.getText().toString();

                if (trackToSearch.length() != 0) {
                    Network network = new Network(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            ArrayList<Song> searchedSongs = (ArrayList<Song>) output;
                            ListView l = (ListView) findViewById(R.id.listView);

                            l.setAdapter(new MyAdapter(getApplicationContext(), searchedSongs));
                            search.setVisibility(View.INVISIBLE);
                            searchText.setVisibility(View.INVISIBLE);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    network.execute(Token, trackToSearch);
                    //mAdapter.notifyDataSetChanged();
                    //getResults(network);
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QueueActivity.class);
                intent.putExtra(TOKEN, Token);
                startActivity(intent);
            }


        });


    }



    public ArrayList<Song> getResults(Network network) {

        tracks = network.getSongs();
        try {
            ArrayList<Song> ListOSongs = new ArrayList<Song>();
            for (Track t : tracks) {
                ListOSongs.add(new Song(t.name, t.artists.get(0).name, 0, t.uri, t.album.images.get(0).url, 0));
                //ListOSongs = ListOSongs + "Title: " + t.name + "   Artist: " + t.artists.get(0).name + "\n\n\n";
                Log.d("SKRAAAA", t.id);
                //RecyclerViewClass fuckinworkpls = new RecyclerViewClass(t.name, t.artists.get(0).toString(), 0, t.uri);
                //Log.d("wtf", fuckinworkpls.toString());
                //SearchActivity.mAdapter.notifyDataSetChanged();
                //mAdapter.notifyDataSetChanged();
            }
            final ArrayList<Song> endResult = ListOSongs;

        return endResult;
    } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Song>();
        }

    }




    }


