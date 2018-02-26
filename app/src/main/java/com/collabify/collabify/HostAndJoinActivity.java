package com.collabify.collabify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class HostAndJoinActivity extends AppCompatActivity {
    public static final String IS_HOST = "com.collabify.collabify.HOST";
    public static final String ROOM_NAME = "com.collabify.collabify.MESSAGE";
    public static final String TOKEN = "com.collabify.collabify.TOKEN";
    public static final String USER = "com.collabify.collabify.USER";
    public static final String TAG = "HostAndJoinActivity";

    public Button CreateRoom;
    public EditText RoomNameText;


    public ArrayList<User> users=new ArrayList<>();
    public ArrayList<Room> rooms=new ArrayList<>();
    public String Token;
    public Database d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_and_join);
        CreateRoom = findViewById(R.id.newRoomButton);
        RoomNameText = findViewById(R.id.newRoomText);

        Intent intent = getIntent();
        Token = intent.getStringExtra(TOKEN);
        d = new Database(getApplicationContext());
        d.readData(users, rooms);
    }

    //TODO: Look into user names for rooms
    public void hostPress(View view){
        CreateRoom.setVisibility(View.VISIBLE);
        RoomNameText.setVisibility(View.VISIBLE);

        CreateRoom.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                String roomName = RoomNameText.getText().toString();
                if ((roomName.length() != 0) && (Room.getRoomFromID(roomName, rooms) == null)){
                    String roomID = "";String uID="";
                    Room r = new Room();
                    roomID = d.addRoomWithName(r, roomName); // Illegal Chars Removed
                    User u = new User();
                    uID = d.addUser(u,true);

                    u.setUserRoom(roomID);
                    r.setHostID(uID);
                    Log.d(TAG, "onClick: " + u + r);
                    d.updateChild(r.getClass(), roomID, r);
                    d.updateChild(u.getClass(), uID, u);

                    Toast.makeText(getApplicationContext(), "You just created room: " + roomID,
                            Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(getApplicationContext(), QueueActivity.class);
                    intent.putExtra(IS_HOST, true);
                    intent.putExtra(ROOM_NAME, roomID);
                    intent.putExtra(TOKEN, Token);
                    intent.putExtra(USER, uID);
                    Log.d("user", "hostPress: "+u.getUserID());
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid Room name...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void joinHostPress(View view){
        Log.d("HostandJoinButton", "joinPress: ");

        Intent intent = new Intent(this, EnterIDActivity.class);
        intent.putExtra(TOKEN, Token);
        intent.putExtra(IS_HOST, true);
        startActivity(intent);
    }

    public void joinPress(View view){


        Log.d("HostandJoinButton", "joinPress: ");

        Intent intent = new Intent(this, EnterIDActivity.class);
        intent.putExtra(TOKEN, Token);
        intent.putExtra(IS_HOST, false);
        startActivity(intent);
    }
}

