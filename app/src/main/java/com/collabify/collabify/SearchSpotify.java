package com.collabify.collabify;

import android.util.Log;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rizwan Qureshi on 2017-11-18.
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
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();
        Log.d("SEARCHING", track);
        return spotify.searchTracks(track).tracks.items;
    }

}
