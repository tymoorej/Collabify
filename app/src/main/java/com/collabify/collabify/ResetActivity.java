package com.collabify.collabify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//TODO:Add back button
public class ResetActivity extends AppCompatActivity {

    private Button deleteButton;
    public Database d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        d = new Database(getApplicationContext());


        deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.clearDatabase();
            }
        });
    }

}
