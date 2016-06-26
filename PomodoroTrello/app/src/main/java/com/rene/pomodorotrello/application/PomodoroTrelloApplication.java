package com.rene.pomodorotrello.application;

import android.app.Application;
import android.content.Context;

import com.rene.pomodorotrello.tracker.MixpanelDelegate;

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

        context = this;

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        MixpanelDelegate.init();
    }

    public static Context getContext() {
        return context;
    }

}
