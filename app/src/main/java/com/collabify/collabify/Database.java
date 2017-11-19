package com.collabify.collabify;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by Tymoore on 11/18/2017.
 */

public class Database {

    public DatabaseReference userRef;
    public DatabaseReference roomRef;


    private Context mContext;


    public Database(Context context) {
        try {
            mContext = context;
            Firebase.setAndroidContext(context);
            userRef = FirebaseDatabase.getInstance().getReference().child("Users");
            roomRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
        }
        catch(Exception e){
            Log.d("ERROR",e.getMessage());
        }
    }


    public void addUser(User u){
        try {
            DatabaseReference newRef = userRef.push();
            u.userID = newRef.getKey();
            newRef.setValue(u);
        }
        catch(Exception e){
            Log.d("ERROR",e.getMessage());
        }
    }


    public void addRoom(Room r){
        try {
            DatabaseReference newRef = roomRef.push();
            r.roomID = newRef.getKey();
            newRef.setValue(r);
        }
        catch(Exception e){
            Log.d("ERROR",e.getMessage());
        }
    }

    public void incrementVote(Room r, Song s){
        try {
            for (int i = 0; i < r.songs.size(); i++) {
                if (r.songs.get(i).songID == s.songID) {
                    DatabaseReference songRef = FirebaseDatabase.getInstance().getReference().
                            child("Rooms").child(r.roomID).child("songs").child(String.valueOf(i)).child("votes");
                    songRef.setValue(s.votes + 1);
                }
            }
        }
        catch(Exception e){
            Log.d("ERROR",e.getMessage());
        }
    }

    public void decrementVote(Room r, Song s){
        try {
            for (int i = 0; i < r.songs.size(); i++) {
                if (r.songs.get(i).songID == s.songID) {
                    DatabaseReference songRef = FirebaseDatabase.getInstance().getReference().
                            child("Rooms").child(r.roomID).child("songs").child(String.valueOf(i)).child("votes");
                    songRef.setValue(s.votes - 1);
                }
            }
        }
        catch(Exception e){
            Log.d("ERROR",e.getMessage());
        }
    }

    public void readData(final ArrayList<User> users, final ArrayList<Room> rooms){
        try {
            userRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
                @Override
                public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    User u = dataSnapshot.getValue(User.class);
                    users.add(u);
                }

                @Override
                public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            roomRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Room r = dataSnapshot.getValue(Room.class);
                    rooms.add(r);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Room r = dataSnapshot.getValue(Room.class);
                    for (Room room : rooms) {
                        if (room.roomID == r.roomID) {
                            room.hostID = r.hostID;
                            room.songs = r.songs;
                        }
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e){
            Log.d("ERROR",e.getMessage());
        }
    }
}
