package com.rene.pomodorotrello.ui.configuration;

import android.content.Context;

import java.util.List;

/**
 * Created by rene on 6/24/16.
 */

public interface ConfigFragmentPresenter {

    void onInit(Context context);
    void onDestroy();
    void login(Context context);
    void onConnectionSuccessful();
    void loadSpinners();
    void initBoardSpinnerAdapter(List<String> boardNames);
    void initToDoSpinnerAdapter(List<String> boardListNames);
    void initDoingSpinnerAdapter(List<String> boardListNames);
    void initDoneSpinnerAdapter(List<String> boardListNames);
    void selectBoardSpinnerItem(String boardName);
    void selectToDoSpinnerItem(String listName);
    void selectDoingSpinnerItem(String listName);
    void selectDoneSpinnerItem(String listName);
    void saveSettings(String boardName, String toDoListName,
                      String doingListName, String doneListName);
    void showSavedValuesDialogMessage();
    void onBoardSelected(String boardName);

}