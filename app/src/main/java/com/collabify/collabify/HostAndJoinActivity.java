package com.collabify.collabify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HostAndJoinActivity extends AppCompatActivity {
    public static final String IS_HOST = "com.tralg.collabify.HOST";
    public static final String EXTRA_MESSAGE = "com.collabify.collabify.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_and_join);
    }

    public void hostPress(View view){
        Intent intent = new Intent(this, QueueActivity.class);
        intent.putExtra(IS_HOST, true);
        intent.putExtra(EXTRA_MESSAGE, "fucked room yo");
        startActivity(intent);
    }

    public void joinPress(View view){
        Intent intent = new Intent(this, EnterIDActivity.class);
        startActivity(intent);
    }
}
