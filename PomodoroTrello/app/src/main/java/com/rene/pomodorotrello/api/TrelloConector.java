package com.rene.pomodorotrello.api;

import com.rene.pomodorotrello.util.Config;

import retrofit2.Retrofit;

/**
 * Created by rene on 6/14/16.
 */

public class TrelloConector {

    public void getLists() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .build();


    }

}
