package com.rene.pomodorotrello.ui.configuration;

import java.util.List;

/**
 * Created by rene on 6/24/16.
 */

public interface ConfigFragmentPresenter {

    void onInit();
    void onDestroy();
    void login();
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