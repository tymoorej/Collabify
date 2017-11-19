package com.collabify.collabify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MainActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    // Private Attributes
    private Player mPlayer;
    private Button b1;
    private Button b2;
    private Button logIn;
    private static final String CLIENT_ID = "2cd5102aa3354e2895257802f830566c";
    private static final String REDIRECT_URI = "collabcallback://callback";
    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    public static final String EXTRA_MESSAGE = "token";
    public AuthenticationResponse response;
    private static final int REQUEST_CODE = 1337;
    public String ss= "";

    private BroadcastReceiver mNetworkStateReceiver;

    /**
     * Used to log messages to a {@link android.widget.TextView} in this activity.
     */
    private TextView mStatusText;

    private TextView mMetadataText;

    private EditText mSeekEditText;

    /**
     * Used to scroll the {@link #mStatusText} to the bottom after updating text.
     */
    private ScrollView mStatusTextScrollView;
    private Metadata mMetadata;

    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.d("HELP", "OK!");
        }

        @Override
        public void onError(Error error) {
            Log.d("HELP","ERROR:" + error);
        }
    };

    private Connectivity getNetworkConnectivity(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return Connectivity.fromNetworkType(activeNetwork.getType());
        } else {
            return Connectivity.OFFLINE;
        }
    }

    public static String getClientId(){return CLIENT_ID;}
    private void turnButtonsOn(){
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
    }

    private void turnButtonsOff() {
        b1.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        logIn = findViewById(R.id.button_logIn);
        b1 = findViewById(R.id.button_song);
        b2 = findViewById(R.id.button_queue);

        turnButtonsOff();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Redirecting...",
                        Toast.LENGTH_SHORT).show();

                b1.setBackgroundColor(Color.RED);
                new CountDownTimer(5000, 50) {

                    @Override
                    public void onTick(long arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onFinish() {
                        b1.setBackgroundColor(Color.BLUE);
                    }
                }.start();
                Intent intent = new Intent(getApplicationContext(), SongSearch.class);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Redirecting...",
                        Toast.LENGTH_SHORT).show();

                b2.setBackgroundColor(Color.RED);
                new CountDownTimer(5000, 50) {

                    @Override
                    public void onTick(long arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onFinish() {
                        b2.setBackgroundColor(Color.GREEN);
                    }
                }.start();
                Intent intent = new Intent(getApplicationContext(), QueueActivity.class);
                Log.d("PLEASE", ss);
                intent.putExtra(EXTRA_MESSAGE, ss);
                startActivity(intent);
            }

        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Login...",
                        Toast.LENGTH_SHORT).show();

                openLoginWindow();
                //logIn();
            }

        });
    }

    private void openLoginWindow() {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            ss = response.getAccessToken();
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    //onAuthenticationComplete(response);
                    Log.d("good","Auth: " + response.getAccessToken());
                    onAuthenticationComplete(response);
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.d("HELPME","Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d("HELPME","Auth result: " + response.getType());
            }
        }
    }

    private void onAuthenticationComplete(AuthenticationResponse authResponse) {
        // Once we have obtained an authorization token, we can proceed with creating a Player.
        Log.d("Auth", "Got authentication token");
        if (mPlayer == null) {
            Config playerConfig = new Config(getApplicationContext(), authResponse.getAccessToken(), CLIENT_ID);
            // Since the Player is a static singleton owned by the Spotify class, we pass "this" as
            // the second argument in order to refcount it properly. Note that the method
            // Spotify.destroyPlayer() also takes an Object argument, which must be the same as the
            // one passed in here. If you pass different instances to Spotify.getPlayer() and
            // Spotify.destroyPlayer(), that will definitely result in resource leaks.
            mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer player) {
                    Log.d("HELP", "-- Player initialized --");
                    mPlayer.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(MainActivity.this));
                    mPlayer.addNotificationCallback(MainActivity.this);
                    mPlayer.addConnectionStateCallback(MainActivity.this);

                    // Trigger UI refresh
                }

                @Override
                public void onError(Throwable error) {
                    Log.d("HH", "Error in initialization: " + error.getMessage());
                }
            });
        } else {
            mPlayer.login(authResponse.getAccessToken());
        }
    }


    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        //mPlayer.playUri(null, "spotify:track:5syaaFva8b6Kgc1wiHkOFp", 0, 0);
        turnButtonsOn();
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error var1) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }
}
