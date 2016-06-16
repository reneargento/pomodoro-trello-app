package com.rene.pomodorotrello.api;

import android.content.Context;
import android.util.Log;

import com.rene.pomodorotrello.util.Config;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import retrofit2.Retrofit;

/**
 * Created by rene on 6/14/16.
 */

public class TrelloConector {

    public void login (final Context context) {

        OAuth oAuth = new OAuth(context);

        oAuth.initialize("_EKkCwzCd2_vigduk5_BG6mwq5w");Log.d("APP","TES33TE");

        oAuth.popup("trello", new OAuthCallback() {
            @Override
            public void onFinished(OAuthData data) {
                if (!data.status.equals("error")) {
                    
                    Log.d("APP","TESTE");

                    String token = data.token;

                } else {
                    Log.d("APP","Error");
                }
            }
        });

    }

    public void getLists() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .build();


    }

}
