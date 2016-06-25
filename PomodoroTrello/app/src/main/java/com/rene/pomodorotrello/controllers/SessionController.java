package com.rene.pomodorotrello.controllers;

import android.content.Context;
import android.util.Log;

import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ConnectionCallback;
import com.rene.pomodorotrello.util.Constants;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;

import static com.rene.pomodorotrello.application.PomodoroTrelloApplication.getContext;

/**
 * Created by rene on 6/14/16.
 */

public class SessionController {

    public void login (final Context context, final ConnectionCallback connectionCallback) {

        OAuth oAuth = new OAuth(context);

        oAuth.initialize(Constants.PUBLIC_KEY);

        oAuth.popup(Constants.TRELLO_KEY, new OAuthCallback() {
            @Override
            public void onFinished(OAuthData data) {
                if (!data.status.equals(Constants.ERROR_KEY)) {

                    SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
                    sharedPreferencesHelper.saveValue(SharedPreferencesHelper.TOKEN_KEY, data.token);
                    Log.d(Constants.LOG_KEY,"Expires in: " + data.expires_in);

                    if(connectionCallback != null) {
                        connectionCallback.onLoginSuccess();
                    }
                } else {
                    connectionCallback.onLoginError();
                }
            }
        });

    }

    public boolean isConnected(){
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getContext());
        String token = sharedPreferencesHelper.getValue(SharedPreferencesHelper.TOKEN_KEY);
        return token != null;
    }

}