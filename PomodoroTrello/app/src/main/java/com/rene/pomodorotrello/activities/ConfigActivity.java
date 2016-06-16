package com.rene.pomodorotrello.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.TrelloController;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        TrelloController trelloController = new TrelloController();
        if (!trelloController.isConnected(getApplicationContext())){
            trelloController.login(ConfigActivity.this);
        }


    }
}
