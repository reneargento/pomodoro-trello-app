package com.rene.pomodorotrello.interfaces;

import com.rene.pomodorotrello.model.Board;
import com.rene.pomodorotrello.model.BoardList;
import com.rene.pomodorotrello.model.Card;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rene on 6/14/16.
 */

public interface TrelloAPI {

    //https://api.trello.com/1/members/me/boards?key=[application_key]&token=[optional_auth_token]
    @GET("members/me/boards")
    Call<List<Board>> getBoards(@Query("key") String key,
                                @Query("token") String token);

    @GET("boards/{boardId}/lists")
    Call<List<BoardList>> getBoardLists(@Path("boardId") String boardId,
                                        @Query("key") String key,
                                        @Query("token") String token);

    @GET("lists/{listId}/cards")
    Call<List<Card>> getListCards(@Path("listId") String listId,
                                  @Query("key") String key,
                                  @Query("token") String token);
}
