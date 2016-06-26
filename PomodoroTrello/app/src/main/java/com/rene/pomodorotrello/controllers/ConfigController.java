package com.rene.pomodorotrello.controllers;

import com.rene.pomodorotrello.dao.ObjectStreamHelper;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;

import java.util.HashMap;

import static com.rene.pomodorotrello.application.PomodoroTrelloApplication.getContext;

/**
 * Created by rene on 6/17/16.
 */

public class ConfigController {

    public void saveSettings(String boardName, String toDoListName,
                             String doingListName, String doneListName){

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_BOARD_KEY, boardName);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_TODO_LIST_KEY, toDoListName);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_DOING_LIST_KEY, doingListName);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_DONE_LIST_KEY, doneListName);

        //Saving the selected board and lists' IDs and Name's mapping
        if (BoardListController.listCache != null) {
            String boardId = BoardController.boardCache.get(boardName);
            String toDoListId = BoardListController.listCache.get(toDoListName);
            String doingListId = BoardListController.listCache.get(doingListName);
            String doneListId = BoardListController.listCache.get(doneListName);

            HashMap<String, String> listMap = new HashMap<>();
            listMap.put(boardName, boardId);
            listMap.put(toDoListName, toDoListId);
            listMap.put(doingListName, doingListId);
            listMap.put(doneListName, doneListId);

            ObjectStreamHelper objectStreamHelper = ObjectStreamHelper.getInstance();
            objectStreamHelper.saveMapObject(getContext(), ObjectStreamHelper.SELECTED_LISTS_FILE_KEY, listMap);
        }

    }

}
