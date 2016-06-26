package com.rene.pomodorotrello.controllers;

import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.interfaces.TrelloAPI;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.model.Board;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.Retrofit;

import static com.rene.pomodorotrello.application.PomodoroTrelloApplication.getContext;

/**
 * Created by rene on 6/16/16.
 */

public class BoardController extends TrelloObjectController {

    public static HashMap<String, String> boardCache = new HashMap<>();

    public void getBoards(final ItemRetriever itemRetriever) {

        Retrofit retrofit = RetrofitController.getInstance();

        String token = SharedPreferencesHelper.getInstance().getValue(SharedPreferencesHelper.TOKEN_KEY);

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
