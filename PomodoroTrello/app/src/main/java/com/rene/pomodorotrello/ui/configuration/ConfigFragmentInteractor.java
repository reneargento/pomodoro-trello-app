package com.rene.pomodorotrello.ui.configuration;

import android.content.Context;

/**
 * Created by rene on 6/24/16.
 */

public interface ConfigFragmentInteractor {

    boolean isConnected();
    void login(Context context);
    void getBoards();
    void saveSettings(String boardName, String toDoListName,
                      String doingListName, String doneListName);
    void loadListItems(String boardId);

}