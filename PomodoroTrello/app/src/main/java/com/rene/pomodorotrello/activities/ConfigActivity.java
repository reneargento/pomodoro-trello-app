package com.rene.pomodorotrello.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.api.TrelloConector;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        TrelloConector trelloConector = new TrelloConector();
        trelloConector.login(ConfigActivity.this);
    }
}
