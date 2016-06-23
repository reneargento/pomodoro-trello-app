package com.rene.pomodorotrello.controllers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Board;
import com.rene.pomodorotrello.vo.BoardList;
import com.rene.pomodorotrello.vo.Card;
import com.rene.pomodorotrello.vo.TrelloObject;

import java.lang.reflect.Type;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by rene on 6/20/16.
 */

public class RetrofitController {

    private static Retrofit retrofit;

    private RetrofitController() {
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    //Using a custom converter to allow for subclasses of TrelloObject to be instantiated
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .registerTypeAdapter(TrelloObject.class, new JsonSerializer<TrelloObject>() {
                                @Override
                                public JsonElement serialize(TrelloObject sourceObject, Type typeOfSrc, JsonSerializationContext context) {
                                    if (sourceObject instanceof Board) {
                                        return context.serialize(sourceObject, Board.class);
                                    } else if (sourceObject instanceof BoardList) {
                                        return context.serialize(sourceObject, BoardList.class);
                                    } else if (sourceObject instanceof Card) {
                                        return context.serialize(sourceObject, Card.class);
                                    }
                                    return context.serialize(sourceObject);
                                }
                            }).create()))
                    .build();
        }
        return retrofit;
    }

}
