package com.collabify.collabify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button msendDataTFB;

    ArrayList<User> users=new ArrayList<>();
    ArrayList<Room> rooms=new ArrayList<>();

    Database data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new Database(this);
        data.readData(users,rooms);

        msendDataTFB=(Button) findViewById(R.id.button2);
        msendDataTFB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
//                data.addUser(new User("",String.valueOf((int)(Math.random()*1000)),false,null));
//                data.addSong(new Song(String.valueOf((int)(Math.random()*1000)),5));
//                ArrayList<Song> songs1=new ArrayList<>();
//                songs1.add(new Song("14",5));
//                songs1.add(new Song("7",7));
//
//                ArrayList<Song> songs2=new ArrayList<>();
//                songs2.add(new Song("1456",1));
//                songs2.add(new Song("71",4));
//
//                data.addRoom(new Room("","5",songs1));
//                data.addRoom(new Room("","6",songs2));

                Room r=Room.getRoomFromID("-KzHKTTBKiazjmImP-KR",rooms);
                Song s=Song.getSongFromID("1456",r.songs);
                data.incrementVote(r,s);
                Log.d("HELP","-----------------");
                for(Room r1: rooms){
                    Log.d("HELP",r1.toString());
                }
                Log.d("HELP","-----------------");
            }
        });
    }
}