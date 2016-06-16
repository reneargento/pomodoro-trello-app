package com.rene.pomodorotrello.controllers;

import android.content.Context;
import android.util.Log;

import com.rene.pomodorotrello.dao.SharedPreferencesUtil;
import com.rene.pomodorotrello.util.Config;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import retrofit2.Retrofit;

/**
 * Created by rene on 6/14/16.
 */

public class TrelloController {

    public void login (final Context context) {

        OAuth oAuth = new OAuth(context);

        oAuth.initialize("_EKkCwzCd2_vigduk5_BG6mwq5w");

        oAuth.popup("trello", new OAuthCallback() {
            @Override
            public void onFinished(OAuthData data) {
                if (!data.status.equals("error")) {

                    SharedPreferencesUtil.saveKey(SharedPreferencesUtil.TOKEN_KEY, data.token);
                    Log.d("APP","Expires in: " + data.expires_in);
                } else {
                    Log.e("APP","Error on login");
                }
            }
        });

    }

    public boolean isConnected(){
        String token = SharedPreferencesUtil.getKey(SharedPreferencesUtil.TOKEN_KEY);
        return token != null;
    }

    public void getLists() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .build();


    }

}
