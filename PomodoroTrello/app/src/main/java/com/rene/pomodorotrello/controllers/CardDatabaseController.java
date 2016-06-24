package com.rene.pomodorotrello.controllers;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rene.pomodorotrello.dao.CardDatabaseManager;
import com.rene.pomodorotrello.interfaces.DatabaseFetchOperation;
import com.rene.pomodorotrello.interfaces.DeleteCardCallback;
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

    public void getOrCreateCard(@NonNull final Card card, @Nullable final DatabaseFetchOperation databaseFetchOperation) {
        getCardById(card.id, new DatabaseFetchOperation() {
            @Override
            public void onOperationSuccess(List<? extends RealmObject> objectList) {
                if (objectList.size() > 0) {
                    if (databaseFetchOperation != null) {
                        databaseFetchOperation.onOperationSuccess(objectList);
                    }
                } else {
                    //Card does not exist, let's create it
                    saveCard(card, false);
                }
            }

            @Override
            public void onOperationError() {
                Log.e(Constants.LOG_KEY, "An exception occurred while fetching cards");
            }
        });
    }

    private void getCardById(String id, DatabaseFetchOperation databaseFetchOperation) {
        CardDatabaseManager cardDatabaseManager = new CardDatabaseManager();
        cardDatabaseManager.getCardById(id, databaseFetchOperation);
    }

    public void getCardByName(String name, DatabaseFetchOperation databaseFetchOperation) {
        CardDatabaseManager cardDatabaseManager = new CardDatabaseManager();
        cardDatabaseManager.getCardByName(name, databaseFetchOperation);
    }

    public void updateCard(final Card card) {
        if(card != null && card.name != null && card.id == null) {
            getCardByName(card.name, new DatabaseFetchOperation() {
                @Override
                public void onOperationSuccess(List<? extends RealmObject> objectList) {
                    List<CardPomodoro> cardList = (List<CardPomodoro>) objectList;
                    if (cardList.size() > 0) {
                        card.id = cardList.get(0).id;
                        saveCard(card, true);
                    }
                }

                @Override
                public void onOperationError() {
                    Log.e(Constants.LOG_KEY, "An exception occurred while updating a card");
                }
            });
        }
    }

    private void saveCard(Card card, final boolean update) {

        final CardPomodoro cardPomodoro = new CardPomodoro();
        cardPomodoro.id = card.id;
        cardPomodoro.name = card.name;

        cardPomodoro.totalMillisecondsSpent = card.totalMillisecondsSpent;
        cardPomodoro.pomodoros = card.pomodoros;

        CardDatabaseManager cardDatabaseManager = new CardDatabaseManager();
        cardDatabaseManager.saveCard(cardPomodoro, update);
    }

    public void deleteCard(String name, DeleteCardCallback deleteCardCallback) {
        CardDatabaseManager cardDatabaseManager = new CardDatabaseManager();
        cardDatabaseManager.deleteCard(name, deleteCardCallback);
    }

}