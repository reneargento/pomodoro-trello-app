package com.rene.pomodorotrello.controllers;

import android.content.Context;
import android.util.Log;

import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.util.Config;
import com.rene.pomodorotrello.vo.Board;

import java.util.List;

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

                    SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
                    sharedPreferencesHelper.saveValue(SharedPreferencesHelper.TOKEN_KEY, data.token);
                    Log.d("APP","Expires in: " + data.expires_in);
                } else {
                    Log.e("APP","Error on login");
                }
            }
        });

    }

    public boolean isConnected(Context context){
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        String token = sharedPreferencesHelper.getValue(SharedPreferencesHelper.TOKEN_KEY);
        return token != null;
    }

    public void getBoards() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .build();

        //List<Board> boardList = retrofit.create();
    }

    public void getLists() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .build();


    }

}
