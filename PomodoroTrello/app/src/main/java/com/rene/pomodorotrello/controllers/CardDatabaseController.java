package com.rene.pomodorotrello.controllers;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rene.pomodorotrello.dao.CardDatabaseManager;
import com.rene.pomodorotrello.interfaces.DatabaseFetchOperation;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Card;
import com.rene.pomodorotrello.vo.CardPomodoro;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by rene on 6/22/16.
 */

@SuppressWarnings("unchecked")
public class CardDatabaseController {

    public void getOrCreateCardByName(final String name, @Nullable final DatabaseFetchOperation databaseFetchOperation) {
        getCardByName(name, new DatabaseFetchOperation() {
            @Override
            public void onOperationSuccess(List<? extends RealmObject> objectList) {
                if (objectList.size() > 0) {
                    if (databaseFetchOperation != null) {
                        databaseFetchOperation.onOperationSuccess(objectList);
                    }
                } else {
                    //Card does not exist, let's create it
                    Card card = new Card();
                    card.name = name;
                    saveCard(card, false);
                }
            }

            @Override
            public void onOperationError() {
                Log.e(Constants.LOG_KEY, "An exception occurred while fetching cards");
            }
        });
    }

    private void getAllCards(@NonNull DatabaseFetchOperation databaseFetchOperation){
        CardDatabaseManager cardDatabaseManager = new CardDatabaseManager();
        cardDatabaseManager.getAllCards(databaseFetchOperation);
    }

    private void getCardByName(String name, DatabaseFetchOperation databaseFetchOperation) {
        CardDatabaseManager cardDatabaseManager = new CardDatabaseManager();
        cardDatabaseManager.getCardByName(name, databaseFetchOperation);
    }

    public void saveCard(Card card, final boolean updateIfExists) {

        final CardPomodoro cardPomodoro = new CardPomodoro();
        cardPomodoro.name = card.name;

        //This is a new card, so we start it with zero seconds and zero pomodoros
        cardPomodoro.totalMillisecondsSpent = 0;
        cardPomodoro.pomodoros = 0;

        CardDatabaseManager cardDatabaseManager = new CardDatabaseManager();
        cardDatabaseManager.saveCard(cardPomodoro, updateIfExists);
    }

    public void deleteCard(String name) {
        CardDatabaseManager cardDatabaseManager = new CardDatabaseManager();
        cardDatabaseManager.deleteCard(name);
    }

}
