package com.rene.pomodorotrello.controllers;

import android.content.Context;
import android.util.Log;

import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ConnectionCallback;
import com.rene.pomodorotrello.util.Constants;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;

/**
 * Created by rene on 6/14/16.
 */

public class SessionController {

    public void login (final Context context, final ConnectionCallback connectionCallback) {

        OAuth oAuth = new OAuth(context);

        oAuth.initialize(Constants.PUBLIC_KEY);

        oAuth.popup("trello", new OAuthCallback() {
            @Override
            public void onFinished(OAuthData data) {
                if (!data.status.equals("error")) {

                    SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
                    sharedPreferencesHelper.saveValue(SharedPreferencesHelper.TOKEN_KEY, data.token);
                    Log.d(Constants.LOG_KEY,"Expires in: " + data.expires_in);

                    if(connectionCallback != null) {
                        connectionCallback.connectionSuccessful();
                    }
                } else {
                    Log.e(Constants.LOG_KEY,"Error on login");
                }
            }
        });

    }

    public boolean isConnected(Context context){
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        String token = sharedPreferencesHelper.getValue(SharedPreferencesHelper.TOKEN_KEY);
        return token != null;
    }

}
