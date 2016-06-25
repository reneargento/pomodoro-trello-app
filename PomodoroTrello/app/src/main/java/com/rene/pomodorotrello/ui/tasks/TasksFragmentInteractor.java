package com.rene.pomodorotrello.ui.tasks;

import com.rene.pomodorotrello.interfaces.GetCardsCallback;

/**
 * Created by rene on 6/24/16.
 */

public interface TasksFragmentInteractor {

    void onGetCards(int listId, GetCardsCallback getCardsCallback);
    boolean isConnected();

}
