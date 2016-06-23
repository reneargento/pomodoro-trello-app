package com.rene.pomodorotrello.dao;

import android.util.Log;

import com.rene.pomodorotrello.interfaces.DatabaseFetchOperation;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.CardPomodoro;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by rene on 6/21/16.
 */

public class CardDatabaseManager {

    public void getAllCards(DatabaseFetchOperation databaseFetchOperation){
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<CardPomodoro> query = realm.where(CardPomodoro.class);

        RealmResults<CardPomodoro> cards = query.findAll();
        databaseFetchOperation.onOperationSuccess(cards);
    }

    public void getCardById(String id, DatabaseFetchOperation databaseFetchOperation) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<CardPomodoro> cards = realm.where(CardPomodoro.class)
                .equalTo(CardPomodoro.ID_KEY, id)
                .findAll();

        databaseFetchOperation.onOperationSuccess(cards);
    }

    public void getCardByName(String name, DatabaseFetchOperation databaseFetchOperation) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<CardPomodoro> cards = realm.where(CardPomodoro.class)
                .equalTo(CardPomodoro.NAME_KEY, name)
                .findAll();

        databaseFetchOperation.onOperationSuccess(cards);
    }

    public void saveCard(final CardPomodoro cardPomodoro, final boolean update) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (update) {
                    realm.copyToRealmOrUpdate(cardPomodoro);
                } else {
                    //Throws exception if object already exists
                    try {
                        realm.copyToRealm(cardPomodoro);
                    } catch (Exception e){
                        Log.e(Constants.LOG_KEY, "Object already exists");
                    }

                }

            }
        });
    }

    public void deleteCard(String name) {
        Realm realm = Realm.getDefaultInstance();

        final RealmResults<CardPomodoro> queryResult = realm.where(CardPomodoro.class)
                .equalTo(CardPomodoro.NAME_KEY, name)
                .findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                queryResult.deleteFirstFromRealm();
            }
        });
    }

}
