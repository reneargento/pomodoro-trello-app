package com.rene.pomodorotrello.application;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rene on 6/22/16.
 */

public class PomodoroTrelloApplication extends Application {

    private static PomodoroTrelloApplication context;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        context = this;
    }

    public static Context getContext() {
        return context;
    }

}
