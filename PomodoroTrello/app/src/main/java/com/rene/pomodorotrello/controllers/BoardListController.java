package com.rene.pomodorotrello.controllers;

import android.content.Context;

import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.interfaces.TrelloAPI;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Board;
import com.rene.pomodorotrello.vo.BoardList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by rene on 6/17/16.
 */

public class BoardListController {

    public static HashMap<String, String> listCache = new HashMap<>();

    public void getBoardLists(Context context, final ItemRetriever itemRetriever, String boardId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String token = SharedPreferencesHelper.getInstance(context).getValue(SharedPreferencesHelper.TOKEN_KEY);

        if (token != null) {
            TrelloAPI trelloAPI = retrofit.create(TrelloAPI.class);

            Call<List<BoardList>> boardListsCall = trelloAPI.getBoardLists(boardId, Constants.KEY, token);

            boardListsCall.enqueue(new Callback<List<BoardList>>() {
                @Override
                public void onResponse(Call<List<BoardList>> call, Response<List<BoardList>> response) {
                    List<BoardList> boardList = response.body();

                    saveBoardListDataOnCache(boardList);
                    itemRetriever.retrieveItems(boardList);
                }

                @Override
                public void onFailure(Call<List<BoardList>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public List<String> getListsNamesFromBoardList(List<BoardList> boardList) {

        int averageListNumber = 3;

        List<String> listNames = new ArrayList<>(averageListNumber);

        for (BoardList list : boardList) {
            listNames.add(list.name);
        }

        return listNames;
    }

    private void saveBoardListDataOnCache(List<BoardList> boardList) {

        listCache.clear();

        for (BoardList list : boardList) {
            listCache.put(list.name, list.id);
        }

    }

}
