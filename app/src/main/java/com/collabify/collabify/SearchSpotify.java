package com.collabify.collabify;

import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPrivate;

/**
 * Created by Rizwan Qureshi on 2017-11-19.
 */
public class SearchSpotify<T> {

    public static List<Artist> queryArtist(String token, String artist) {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();
        Log.d("SEARCHING", artist);
        return spotify.searchArtists(artist).artists.items;
    }

    public static List<AlbumSimple> queryAlbum(String token, String album) {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();
        Log.d("SEARCHING", album);
        return spotify.searchAlbums(album).albums.items;
    }

    public static List<Track> queryTracks(String token, String track) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        api.setAccessToken(token);
        Log.d("SEARCHING", track);
        return spotify.searchTracks(track).tracks.items;

    }

    public static 
}