package com.rene.pomodorotrello.dao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rene on 6/15/16.
 */

public class SharedPreferencesUtil {

    public static final String SHARED_PREFERENCES_KEY = "pomodorotrellosp";
    public static final String TOKEN_KEY = "token";

    private static Context context;

    public static void init(Context context) {
        SharedPreferencesUtil.context = context;
    }

    public static void saveKey(String key, String value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY,
                    Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(key, value).apply();
        }
    }

    public static String getKey(String key) {
        String keyValue = null;

        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY,
                    Context.MODE_PRIVATE);
            keyValue = sharedPreferences.getString(key, null);
        }

        return keyValue;
    }

}
