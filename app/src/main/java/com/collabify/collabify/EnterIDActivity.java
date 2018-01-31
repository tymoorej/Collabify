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
    public static String userID;
    public String Token;
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
        Log.d("EnterIDActivity", "onCreate: "+userID);
        d = new Database(getApplicationContext());
        d.readData(users, rooms);


        joinRoom = (Button) findViewById(R.id.join);



    }

    public void joinRoom(View view){
        Intent intent = new Intent(this, QueueActivity.class);
        EditText roomText = (EditText) findViewById(R.id.enterID);
        String roomID = roomText.getText().toString();
        u = User.getUserFromID(userID, users);
        u.setUserRoom(roomID);



        if (Room.getRoomFromID(roomID, rooms) != null && u != null) {

            d.updateChild(u.getClass(), userID, u);

            intent.putExtra(ROOM_NAME, roomID);
            intent.putExtra(USER,u.getUserID());
            intent.putExtra(TOKEN,Token);
            Log.d("enterIDact", "joinRoom: "+roomID+ " " + u.getUserID());
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Invalid Room name...",
                    Toast.LENGTH_SHORT).show();
        }


    }

}

