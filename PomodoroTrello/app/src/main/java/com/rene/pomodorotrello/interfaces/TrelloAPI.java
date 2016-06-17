package com.rene.pomodorotrello.interfaces;

import com.rene.pomodorotrello.vo.Board;

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
    Call<List<Board>> boardsList(@Query("key") String key, @Query("token") String token);

    @GET("boards/{boardId}")
    Call<List<Board>> boardsList(@Path("boardId") String boardId);
}
