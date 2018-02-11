package com.collabify.collabify;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
            Log.d("Database ERROR",e.getMessage());
        }
    }

    public void updateRoom(List<Song> songs, Room currentRoom) {
        ArrayList<Song> mItems = new ArrayList<>();
        mItems.addAll(songs);
        currentRoom.setRoomSongs(mItems);
        this.updateChild(Room.class, currentRoom.getRoomID(), currentRoom);
    }

    public void clearDatabase() {
         this.userRef.removeValue();
         this.roomRef.removeValue();
    }

    public void updateChild(Class type, String childID, Object newVal) {
        if (type.equals(User.class)) {
            userRef.child(childID).setValue((User)newVal);
        } else if (type.equals(Room.class)) {
            roomRef.child(childID).setValue((Room)newVal);
        }
    }


    public String addUser(User u, boolean isHost){
        try {
            DatabaseReference newRef = userRef.push();
            u.setUserID(newRef.getKey());
            u.setIsHost(isHost);
            newRef.setValue(u);
        }
        catch(Exception e){
            Log.d("User ERROR",e.getMessage());
        }
        finally {
            return u.getUserID();
        }
    }

    //TODO: Look into user names for rooms
    public String addRoom(Room r){
        String roomID = "";
        try {
            DatabaseReference newRef = roomRef.push();
            roomID = newRef.getKey();
            r.setRoomID(roomID);
            //r.setRoomSongs(new ArrayList<Song>(Arrays.asList(tempSong(), tempSong(), tempSong())));
            newRef.setValue(r);
    }
        catch(Exception e){
            Log.d("Room ERROR",e.getMessage());
        }
        finally {
            return r.getRoomID();
        }
    }

    public String addRoomWithName(Room r, String name){
        try {
            DatabaseReference newRef = roomRef.child(name);
            r.setRoomID(name);
            //r.setRoomSongs(new ArrayList<Song>(Arrays.asList(tempSong(), tempSong(), tempSong())));
            newRef.setValue(r);
        }
        catch(Exception e){
            Log.d("Room ERROR",e.getMessage());
        }
        finally {
            return r.getRoomID();
        }
    }

    public void incrementVote(Room r, Song s){
        try {
            for (int i = 0; i < r.songs.size(); i++) {
                if (r.songs.get(i).getUri() == s.getUri()) {
                    DatabaseReference songRef = FirebaseDatabase.getInstance().getReference().
                            child("Rooms").child(r.roomID).child("songs").child(String.valueOf(i)).child("votes");
                    songRef.setValue(s.getVotes() + 1);
                }
            }
        }
        catch(Exception e){
            Log.d("UpVote ERROR",e.getMessage());
        }
    }

    public void decrementVote(Room r, Song s){
        try {
            for (int i = 0; i < r.songs.size(); i++) {
                if (r.songs.get(i).getUri() == s.getUri()) {
                    DatabaseReference songRef = FirebaseDatabase.getInstance().getReference().
                            child("Rooms").child(r.roomID).child("songs").child(String.valueOf(i)).child("votes");
                    songRef.setValue(s.getVotes() - 1);
                }
            }
        }
        catch(Exception e){
            Log.d(" DownVote ERROR",e.getMessage());
        }
    }

    public User toUser(DataSnapshot ds){
        User u = new User();
        for (DataSnapshot postSnapshot : ds.getChildren()) {
            switch (postSnapshot.getKey()) {
                case ("isHost"):
                    u.setIsHost(postSnapshot.getValue(Boolean.class));
                case ("userID"):
                    u.setUserID(postSnapshot.getValue(String.class));
            }
        }
        return u;
    }

    public Song toSong(DataSnapshot ds) {
        Song s = new Song();
        for (DataSnapshot postSnapshot : ds.getChildren()) {
            switch (postSnapshot.getKey()) {
                case ("artist"):
                    s.setArtist(postSnapshot.getValue(String.class));
                case ("artwork"):
                    s.setImageURL(postSnapshot.getValue(String.class));
                case ("positionInMs"):
                    s.setPositionInMs(postSnapshot.getValue(Integer.class));
                case ("uri"):
                    s.setUri(postSnapshot.getValue(String.class));
                case ("votes"):
                    s.setVotes(postSnapshot.getValue(Integer.class));
            }
        }
        return s;
    }

    public Room toRoom(DataSnapshot ds){
        Room r = new Room();
        for (DataSnapshot postSnapshot : ds.getChildren()) {
            switch (postSnapshot.getKey()) {
                case ("hostID"):
                    r.setHostID(postSnapshot.getValue(String.class));
                case ("roomID"):
                    r.setRoomID(postSnapshot.getValue(String.class));
                case ("songs"):
                    for (DataSnapshot songs : postSnapshot.getChildren()) {
                        r.addRoomSong(toSong(songs));
                    }
            }
        }
        return r;
    }

    public void searchUser(final String uID, final User u) {
        Log.d("searchUser", "Database: "+uID +" "+u);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("searchUser", "onDataChanged: " + dataSnapshot.toString());
                u.setUserID(dataSnapshot.child(uID).getValue(User.class).getUserID());
                u.setIsHost(dataSnapshot.child(uID).getValue(User.class).getIsHost());
                u.setUserRoom(dataSnapshot.child(uID).getValue(User.class).getUserRoom());
                u.setUserName(dataSnapshot.child(uID).getValue(User.class).getUserName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*Query query = userRef.orderByChild("userID").equalTo(uID);
        query.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("addedUser", "onChildAdded: "+postSnapshot.toString());
                    switch (postSnapshot.getKey()) {
                        case ("isHost"):
                            u.setIsHost(postSnapshot.getValue(Boolean.class));
                        case ("userID"):
                            u.setUserID(postSnapshot.getValue(String.class));
                    }
                }
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
        });*/
    }

    public void searchRoom(final String rID, final Room r) {
        Log.d("roomSearch", "searchRoom: "+rID +" "+r);


        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("searchUser", "onDataChanged: " + dataSnapshot.toString());
                r.setHostID(dataSnapshot.child(rID).getValue(Room.class).getHostID());
                r.setRoomID(dataSnapshot.child(rID).getValue(Room.class).getRoomID());
                r.setRoomSongs(dataSnapshot.child(rID).getValue(Room.class).getSongs());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query query = roomRef.orderByChild("roomID").equalTo(rID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("searchRoom", "onDataChanged: " + dataSnapshot.toString());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("searchRoom", "onChildAdded: " + postSnapshot.toString());
                    switch (postSnapshot.getKey()) {
                        case ("hostID"):
                            r.setHostID(postSnapshot.getValue(String.class));
                        case ("roomID"):
                            r.setRoomID(postSnapshot.getValue(String.class));
                        case ("songs"):
                            for (DataSnapshot songs : postSnapshot.getChildren()) {
                                r.addRoomSong(toSong(songs));
                            }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        Query query = roomRef.orderByChild("roomID").equalTo(rID);
        query.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("searchRoom", "onChildAdded: "+postSnapshot.toString());
                    switch (postSnapshot.getKey()) {
                        case ("hostID"):
                            r.setHostID(postSnapshot.getValue(String.class));
                        case ("roomID"):
                            r.setRoomID(postSnapshot.getValue(String.class));
                        case ("songs"):
                            for (DataSnapshot songs : postSnapshot.getChildren()) {
                                r.addRoomSong(toSong(songs));
                            }
                    }
                }
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
        });*/
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
