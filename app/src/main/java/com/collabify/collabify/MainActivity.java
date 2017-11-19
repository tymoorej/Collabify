package com.collabify.collabify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import com.tralg.collabify.R;

public class MainActivity extends AppCompatActivity {

    private Button msendDataTFB;

    ArrayList<User> users=new ArrayList<>();
    ArrayList<Song> songs=new ArrayList<>();

    UserDatabase data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new UserDatabase(this);
        data.readData(users,songs);

        msendDataTFB=(Button) findViewById(R.id.button2);
        msendDataTFB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                data.addUser(new User("",String.valueOf((int)(Math.random()*1000)),false,null));
                data.addSong(new Song("","",(int)(Math.random()*1000),"","","","",""));
            }
        });
    }
}