package com.rene.pomodorotrello.interfaces;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by rene on 6/22/16.
 */

public interface DatabaseFetchOperation {

    void onOperationSuccess(List<? extends RealmObject> objectList);
    void onOperationError();

}
