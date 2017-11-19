package com.collabify.collabify;

import android.content.Context;
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

public class UserDatabase {

    public DatabaseReference userRef;
    public DatabaseReference songRef;


    private Context mContext;


    public UserDatabase(Context context) {
        mContext = context;
        Firebase.setAndroidContext(context);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        songRef = FirebaseDatabase.getInstance().getReference().child("Songs");
    }


    public void addUser(User u){
        DatabaseReference newRef = userRef.push();
        u.userID = newRef.getKey();
        newRef.setValue(u);
    }

    public void addSong(Song s){
        DatabaseReference newRef = songRef.push();
        s.songID = newRef.getKey();
        newRef.setValue(s);
    }

    public void readData(final ArrayList<User> users, final ArrayList<Song> songs){
        userRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                User u = dataSnapshot.getValue(User.class);
                users.add(u);
                Toast.makeText(mContext, "User added!", Toast.LENGTH_LONG).show();
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

        songRef.addChildEventListener(new com.google.firebase.database.ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Song song = dataSnapshot.getValue(Song.class);
                songs.add(song);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
}
