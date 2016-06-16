package com.rene.pomodorotrello.controllers;

import android.content.Context;

import com.rene.pomodorotrello.dao.SharedPreferencesUtil;

/**
 * Created by rene on 6/15/16.
 */

public class AppController {

    public void initApp(Context context) {

        SharedPreferencesUtil.init(context);

    }

}
