package com.collabify.collabify;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Rizwan Qureshi on 2017-11-19.
 */

public class Network extends AsyncTask<String, Void, Void> {
    private List<Track> songs;

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
    protected Void doInBackground(final String... params) {
        songs = SearchSpotify.queryTracks(params[0], params[1]);
        return null;
    }

    // onPostExecute(): This method runs on the UI thread after the doInBackground operation has
    // completed.
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
