package com.collabify.collabify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//TODO:Add back button
public class ResetActivity extends AppCompatActivity {

    public static final String IS_HOST = "com.collabify.collabify.HOST";
    public static final String ROOM_NAME = "com.collabify.collabify.MESSAGE";
    public static final String TOKEN = "com.collabify.collabify.TOKEN";
    public static final String USER = "com.collabify.collabify.USER";
    public static final String TAG = "ResetActivity";

    public String userID;
    public String roomID;
    public String Token;

    private Button deleteButton;
    public Database d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        d = new Database(getApplicationContext());

        Intent intent = getIntent();

        userID = intent.getStringExtra(USER);
        roomID = intent.getStringExtra(ROOM_NAME);
        Token = intent.getStringExtra(TOKEN);


        deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.clearDatabase();

                Intent intent = new Intent(getApplicationContext(), HostAndJoinActivity.class);
                intent.putExtra(IS_HOST, true);
                intent.putExtra(ROOM_NAME, roomID);
                intent.putExtra(TOKEN, Token);
                intent.putExtra(USER, userID);

                Log.d(TAG, "onClick: " + " Deleted all my songs ri");

                startActivity(intent);
            }
        });
    }

}
