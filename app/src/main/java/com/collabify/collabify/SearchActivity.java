package com.collabify.collabify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CustomTrackAdapter mAdapter;
    private List<RecyclerViewClass> mItems;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button search;
    private EditText searchText;
    private List<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = (Button)findViewById(R.id.searchButton);
        searchText = (EditText)findViewById(R.id.searchText);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView2);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mItems = new ArrayList<>();
        //adding test items to the list
        mAdapter = new CustomTrackAdapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trackToSearch = searchText.getText().toString();
                Network network = new Network();
                network.execute(null, trackToSearch);
                getResults(network);
            }
        });

    }

    public Void getResults(Network network){
        tracks = network.getSongs();

        for(Track t: tracks){
            mItems.add(new RecyclerViewClass(t.name, t.artists.get(0).toString(), 0, t.uri));
        }
        mAdapter.notifyDataSetChanged();
        return null;
    }
}
