package com.rene.pomodorotrello.interfaces;

import com.rene.pomodorotrello.vo.Board;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by rene on 6/14/16.
 */

public interface TrelloAPI {

    @GET("authorize")
    Call<> 

    @GET("boards/{boardId}")
    Call<List<Board>> boardsList(@Path("boardId") String boardId);
}
