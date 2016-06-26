package com.rene.pomodorotrello.interfaces;

/**
 * Created by rene on 6/25/16.
 */

public interface GetTimeSpentCallback {

    void onSuccessGetTimeSpent(long timeSpent);
    void onFailureGetTimeSpent();

}
