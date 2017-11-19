package com.collabify.collabify;

import android.os.AsyncTask;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

import com.collabify.collabify.SearchActivity;

/**
 * Created by Rizwan Qureshi on 2017-11-19.
 */
public class Network extends AsyncTask<String, Void, List<Track>> {
    public AsyncResponse delegate = null;//Call back interface
    private List<Track> songs;

    public Network(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    public List<Track> getSongs() {
        return songs;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // doInBackground(): This method constantly runs in the background while AsyncTask is
    // running.
    @Override
    protected List<Track> doInBackground(final String... params) {
        songs = SearchSpotify.queryTracks(params[0], params[1]);
        return songs ;
    }

    // onPostExecute(): This method runs on the UI thread after the doInBackground operation has
    // completed.
    @Override
    protected void onPostExecute(List<Track> lv) {
        SearchActivity sa = new SearchActivity();
        String result = sa.getResults(this);
        delegate.processFinish(result);
    }
}