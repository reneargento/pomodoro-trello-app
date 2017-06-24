package com.rene.pomodorotrello.application;

import android.app.Application;
import android.content.Context;

import com.rene.pomodorotrello.tracker.MixpanelDelegate;

import io.realm.Realm;

/**
 * Created by rene on 6/22/16.
 */

public class PomodoroTrelloApplication extends Application {

    private static PomodoroTrelloApplication context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        Realm.init(this);

        MixpanelDelegate.init();
    }

    public static Context getContext() {
        return context;
    }

}
