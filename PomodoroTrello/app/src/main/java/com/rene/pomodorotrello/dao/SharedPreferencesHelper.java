package com.rene.pomodorotrello.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.rene.pomodorotrello.R;

/**
 * Created by rene on 6/15/16.
 */

public class SharedPreferencesHelper {

    public static final String SHARED_PREFERENCES_KEY = "pomodorotrellosp";
    public static final String TOKEN_KEY = "token";

    public static final String SELECTED_BOARD_KEY = "selectedBoard";
    public static final String SELECTED_TODO_LIST_KEY = "selectedToDoList";
    public static final String SELECTED_DOING_LIST_KEY = "selectedDoingList";
    public static final String SELECTED_DONE_LIST_KEY = "selectedDoneList";

    private static SharedPreferencesHelper sharedPreferencesHelper;
    private static SharedPreferences sharedPreferences;

    private SharedPreferencesHelper() {
    }

    public static SharedPreferencesHelper getInstance(Context context) {
        if(sharedPreferencesHelper == null) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
            sharedPreferencesHelper = new SharedPreferencesHelper();
        }
        return sharedPreferencesHelper;
    }


    public void saveValue(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getValue(String key) {
        return sharedPreferences.getString(key, null);
    }

}
