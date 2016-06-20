package com.rene.pomodorotrello.controllers;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.interfaces.TrelloAPI;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Board;
import com.rene.pomodorotrello.vo.TrelloObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by rene on 6/16/16.
 */

public class BoardController extends TrelloObjectController {

    public static HashMap<String, String> boardCache = new HashMap<>();

    public void getBoards(Context context, final ItemRetriever itemRetriever) {

        Retrofit retrofit = RetrofitController.getInstance();

        String token = SharedPreferencesHelper.getInstance(context).getValue(SharedPreferencesHelper.TOKEN_KEY);

        if (token != null) {
            TrelloAPI trelloAPI = retrofit.create(TrelloAPI.class);

            Call<List<Board>> boardListCall = trelloAPI.getBoards(Constants.KEY, token);

            boardListCall.enqueue(new Callback<List<Board>>() {
                @Override
                public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                    List<Board> boardList = response.body();

                    saveBoardDataOnCache(boardList);
                    itemRetriever.retrieveItems(boardList);
                }

                @Override
                public void onFailure(Call<List<Board>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void saveBoardDataOnCache(List<Board> boardList) {

        boardCache.clear();

        for (Board board : boardList) {
            boardCache.put(board.name, board.id);
        }

    }

}
