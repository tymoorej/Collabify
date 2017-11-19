package com.collabify.collabify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ArrayList;
import java.util.List;


import kaaes.spotify.webapi.android.models.Track;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

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
                network.execute("token", trackToSearch);

            }


    });}

    public void getResults(Network network){
        tracks = network.getSongs();

//        for(Track t: tracks){
//            mItems.add(new RecyclerViewClass(t.name, t.artists.get(0).toString(), 0, t.uri));
//        }
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Network n = new Network();
                n.execute(response.getAccessToken(), "kid cudi");
            }
        }
    }
}
