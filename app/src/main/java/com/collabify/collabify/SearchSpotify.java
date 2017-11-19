package com.collabify.collabify;

import android.util.Log;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import retrofit.RetrofitError;

/**
 * Created by Rizwan Qureshi on 2017-11-18.
 */

public class SearchSpotify {
    private SpotifyApi api = new SpotifyApi();
    private SpotifyService spotify;

    SearchSpotify(String accessToken) {
        api.setAccessToken(accessToken);
        spotify = api.getService();
    }

    public void test(String spotifyToken) {
        api = new SpotifyApi();
        api.setAccessToken(spotifyToken);
        spotify = api.getService();
        try {
            spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8");
        } catch (RetrofitError e) {
            SpotifyError spotifyError = SpotifyError.fromRetrofitError(e);
            Log.d("ERROR", spotifyToken);
        }
    }
}
