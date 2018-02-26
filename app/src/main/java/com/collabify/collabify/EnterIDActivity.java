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
    public static final String IS_HOST = "com.collabify.collabify.HOST";
    public static final String TAG = "EnterIDActivity";
    public static String uID;
    public String Token;
    public boolean isHost;
    private Room currentRoom = new Room();
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
        isHost = intent.getBooleanExtra(IS_HOST, false);
        d = new Database(getApplicationContext());
        d.readData(users, rooms);


        joinRoom = (Button) findViewById(R.id.join);



    }

    public void joinRoom(View view){
        Intent intent = new Intent(this, QueueActivity.class);
        EditText roomText = (EditText) findViewById(R.id.enterID);
        String roomID = roomText.getText().toString();
        roomID = Database.removeIllegalChars(roomID);



        String uID = "";
        u = new User();
        uID = d.addUser(u,isHost);
        u.setUserRoom(roomID);

        //TODO: Make it so that the rooms host gets cleared when the app closes
//        if (isHost) {
//            currentRoom = Room.getRoomFromID(roomID, rooms);
//            if (currentRoom != null && currentRoom.getHostID() == null && u != null) {
//                currentRoom.setHostID(u.getUserID());
//                Log.d(TAG, "joinRoom: " + currentRoom);
//
//                d.updateChild(currentRoom.getClass(), currentRoom.getRoomID(), currentRoom);
//                d.updateChild(u.getClass(), uID, u);
//
//                intent.putExtra(ROOM_NAME, roomID);
//                intent.putExtra(USER,uID);
//                intent.putExtra(TOKEN,Token);
//                Log.d("enterIDact", "joinRoom: As Host"+roomID+ " " + u.getUserID());
//                startActivity(intent);
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "Already a host...",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }



        if (Room.getRoomFromID(roomID, rooms) != null && u != null) {

                d.updateChild(u.getClass(), uID, u);

                intent.putExtra(ROOM_NAME, roomID);
                intent.putExtra(USER,uID);
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

