package com.collabify.collabify;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import kaaes.spotify.webapi.android.models.Track;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private static CustomTrackAdapter mAdapter;
    private List<RecyclerViewClass> mItems = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private Button search;
    private Button daButton;
    private EditText searchText;
    private List<Track> tracks;
    private TextView listOfSongs;
    //public static String LIST_SONGS = "com.collabify.collabify.fuqdupshizza";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();

        //String ID = intent.getStringExtra(EnterIDActivity.EXTRA_MESSAGE);
        final String Token = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //final String listOfSongs = intent.getStringExtra(SearchActivity.LIST_SONGS);

        search = (Button) findViewById(R.id.searchButton);
        searchText = (EditText) findViewById(R.id.searchText);
        listOfSongs = (TextView)findViewById(R.id.textView);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItems.add(new RecyclerViewClass("Title", "Artist", 0, "uri"));
                //mAdapter.notifyDataSetChanged();
                String trackToSearch = searchText.getText().toString();
                Network network =new Network(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        TextView t = (TextView)findViewById(R.id.textView);
                        t.setText((String) output);
                    }
                });
                network.execute(Token, trackToSearch);
                //mAdapter.notifyDataSetChanged();
                //getResults(network);
            }


        });}






    public String getResults(Network network) {

        tracks = network.getSongs();
        try {
            String ListOSongs = "";
            for (Track t : tracks) {
                ListOSongs = ListOSongs + "Title: " + t.name + "   Artist: " + t.artists.get(0).name + "\n\n\n";
                Log.d("SKRAAAA", t.id);
                //RecyclerViewClass fuckinworkpls = new RecyclerViewClass(t.name, t.artists.get(0).toString(), 0, t.uri);
                //Log.d("wtf", fuckinworkpls.toString());
                //SearchActivity.mAdapter.notifyDataSetChanged();
                //mAdapter.notifyDataSetChanged();
            }
            final String endResult = ListOSongs;

        return endResult;
    } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }


}
