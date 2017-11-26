package com.collabify.collabify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HostAndJoinActivity extends AppCompatActivity {
    public static final String IS_HOST = "com.tralg.collabify.HOST";
    public static final String ROOM_NAME = "com.collabify.collabify.MESSAGE";
    public static final String TOKEN = "com.collabify.collabify.TOKEN";
    public String Token;
    public Database d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_and_join);
        Intent intent = getIntent();
        Token = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        d = new Database(getApplicationContext());
    }

    public void hostPress(View view){
        Room r = new Room();
        d.addRoom(r);
        Intent intent = new Intent(this, QueueActivity.class);
        intent.putExtra(IS_HOST, true);
        intent.putExtra(ROOM_NAME, r.roomID);
        intent.putExtra(TOKEN, Token);
        startActivity(intent);
    }

    public void joinPress(View view){
        Intent intent = new Intent(this, EnterIDActivity.class);
        intent.putExtra(TOKEN, Token);
        startActivity(intent);
    }
}

