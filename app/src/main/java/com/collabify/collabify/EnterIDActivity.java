package com.collabify.collabify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class EnterIDActivity extends AppCompatActivity {
    public static final String ROOM_NAME = "com.collabify.collabify.MESSAGE";
    public static final String TOKEN = "com.collabify.collabify.TOKEN";
    public static final String USER = "com.collabify.collabify.USER";
    public String Token;
    public String userID;
    public Database d;
    public User u;
    public ArrayList<User> users = new ArrayList<>();
    public ArrayList<Room> rooms = new ArrayList<>();
    Button joinRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_id);
        Intent intent = getIntent();
        Token = intent.getStringExtra(TOKEN);
        userID = intent.getStringExtra(USER);
        d = new Database(getApplicationContext());
        d.readData(users, rooms);


        joinRoom = (Button) findViewById(R.id.join);



    }

    public void joinRoom(View view){
        Intent intent = new Intent(this, QueueActivity.class);
        EditText roomID = (EditText) findViewById(R.id.enterID);
        String message = roomID.getText().toString();
        u = User.getUserFromID(userID, users);



        if (Room.getRoomFromID(message, rooms) != null && User.getUserFromID(intent.getStringExtra(USER), users) != null) {
            intent.putExtra(ROOM_NAME, message);
            intent.putExtra(USER,u.getUserID());
            Log.d("enterIDact", "joinRoom: "+message+ " " + u.getUserID());
            startActivity(intent);
        }

        Toast.makeText(getApplicationContext(), "Invalid Room name...",
                Toast.LENGTH_SHORT).show();


    }

}

