package com.rene.pomodorotrello.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.tracker.MixpanelDelegate;
import com.rene.pomodorotrello.ui.configuration.ConfigActivity;
import com.rene.pomodorotrello.ui.tasks.TasksActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sendUserToNextActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MixpanelDelegate.track(MixpanelDelegate.MIXPANEL_OPEN_APP_EVENT, null);
    }

    private void sendUserToNextActivity(){
        Intent intent;

        SessionController sessionController = new SessionController();
        if (sessionController.isConnected()){
            intent = new Intent(this, TasksActivity.class);
        } else {
            intent = new Intent(this, ConfigActivity.class);
        }

        startActivity(intent);
    }
}
