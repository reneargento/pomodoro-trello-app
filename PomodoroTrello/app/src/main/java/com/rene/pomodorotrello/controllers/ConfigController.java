package com.rene.pomodorotrello.controllers;

import android.content.Context;

import com.rene.pomodorotrello.dao.SharedPreferencesHelper;

/**
 * Created by rene on 6/17/16.
 */

public class ConfigController {

    public void saveSettings(Context context, String boardName, String toDoListName,
                             String doingListName, String doneListName){

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_BOARD_KEY, boardName);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_TODO_LIST_KEY, toDoListName);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_DOING_LIST_KEY, doingListName);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_DONE_LIST_KEY, doneListName);
    }

}
