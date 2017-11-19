package com.collabify.collabify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class HostAndJoinActivity extends AppCompatActivity {
    public static final String IS_HOST = "com.tralg.collabify.HOST";
    public static final String EXTRA_MESSAGE = "com.tralg.collabify.MESSAGE";
    Database data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_and_join);
        data = new Database(this);
    }

    public void hostPress(View view){
        Intent intent = new Intent(this, QueueActivity.class);
        data.addRoom(new Room("roomID", "hostID"+Math.random(), new ArrayList<Song>()));
        intent.putExtra(IS_HOST, true);
        intent.putExtra(EXTRA_MESSAGE, "roomID");
        startActivity(intent);
    }

    public void joinPress(View view){
        Intent intent = new Intent(this, EnterIDActivity.class);
        startActivity(intent);
    }
}
