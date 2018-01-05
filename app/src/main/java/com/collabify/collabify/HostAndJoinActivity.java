package com.collabify.collabify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class HostAndJoinActivity extends AppCompatActivity {
    public static final String IS_HOST = "com.collabify.collabify.HOST";
    public static final String ROOM_NAME = "com.collabify.collabify.MESSAGE";
    public static final String TOKEN = "com.collabify.collabify.TOKEN";
    public static final String USER = "com.collabify.collabify.USER";
    public String Token;
    public Database d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_and_join);
        Intent intent = getIntent();
        Token = intent.getStringExtra(TOKEN);
        d = new Database(getApplicationContext());
    }

    public void hostPress(View view){
        String roomID = "";String uID="";
        Room r = new Room();
        roomID = d.addRoom(r);
        User u = new User();
        uID = d.addUser(u,true);
        u.setUserRoom(roomID);

        Intent intent = new Intent(this, QueueActivity.class);
        intent.putExtra(IS_HOST, true);
        intent.putExtra(ROOM_NAME, roomID);
        intent.putExtra(TOKEN, Token);
        intent.putExtra(USER, uID);
        Log.d("user", "hostPress: "+u.getUserID());
        startActivity(intent);
    }

    public void joinPress(View view){
        String uID = "";
        User u = new User();
        uID = d.addUser(u,false);

        Intent intent = new Intent(this, EnterIDActivity.class);
        intent.putExtra(TOKEN, Token);
        intent.putExtra(USER, uID);
        startActivity(intent);
    }
}

