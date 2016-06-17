package com.rene.pomodorotrello.controllers;

import android.content.Context;

import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.interfaces.TrelloAPI;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Board;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by rene on 6/16/16.
 */

public class BoardController {

    public void getBoards(Context context, final ItemRetriever itemRetriever) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String token = SharedPreferencesHelper.getInstance(context).getValue(SharedPreferencesHelper.TOKEN_KEY);

        if (token != null) {
            TrelloAPI trelloAPI = retrofit.create(TrelloAPI.class);

            Call<List<Board>> boardListCall = trelloAPI.boardsList(Constants.KEY, token);

            boardListCall.enqueue(new Callback<List<Board>>() {
                @Override
                public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                    List<Board> boardList = response.body();
                    itemRetriever.retrieveItems(boardList);
                }

                @Override
                public void onFailure(Call<List<Board>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public List<String> getBoardsNamesFromBoardList(List<Board> boardList) {

        int averageBoardNumber = 5;

        List<String> boardNames = new ArrayList<>(averageBoardNumber);

        for (Board board : boardList) {
            boardNames.add(board.name);
        }

        return boardNames;
    }

}
