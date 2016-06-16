package com.rene.pomodorotrello.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.TrelloController;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sendUserToNextActivity();
    }


    private void sendUserToNextActivity(){
        Intent intent;

        TrelloController trelloController = new TrelloController();
        if (!trelloController.isConnected(getApplicationContext())){
            intent = new Intent(this, TasksActivity.class);
        } else {
            intent = new Intent(this, ConfigActivity.class);
        }

        startActivity(intent);
    }
}
