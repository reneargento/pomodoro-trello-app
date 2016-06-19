package com.rene.pomodorotrello.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.SessionController;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sendUserToNextActivity();
    }


    private void sendUserToNextActivity(){
        Intent intent;

        SessionController sessionController = new SessionController();
        if (sessionController.isConnected(getApplicationContext())){
            intent = new Intent(this, TasksActivity.class);
        } else {
            intent = new Intent(this, ConfigActivity.class);
        }

        startActivity(intent);
    }
}
