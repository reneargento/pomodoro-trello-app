package com.rene.pomodorotrello.controllers;

import android.content.Context;

import com.rene.pomodorotrello.dao.ObjectStreamHelper;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;

import java.util.HashMap;

/**
 * Created by rene on 6/17/16.
 */

public class ConfigController {

    public void saveSettings(Context context, String boardName, String toDoListName,
                             String doingListName, String doneListName){

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_BOARD_KEY, boardName);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_TODO_LIST_KEY, toDoListName);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_DOING_LIST_KEY, doingListName);
        sharedPreferencesHelper.saveValue(SharedPreferencesHelper.SELECTED_DONE_LIST_KEY, doneListName);

        //Saving the selected lists' IDs and Name's mapping
        if (BoardListController.listCache != null) {
            String toDoListId = BoardListController.listCache.get(toDoListName);
            String doingListId = BoardListController.listCache.get(doingListName);
            String doneListId = BoardListController.listCache.get(doneListName);

            HashMap<String, String> listMap = new HashMap<>();
            listMap.put(toDoListName, toDoListId);
            listMap.put(doingListName, doingListId);
            listMap.put(doneListName, doneListId);

            ObjectStreamHelper objectStreamHelper = ObjectStreamHelper.getInstance();
            objectStreamHelper.saveMapObject(context, ObjectStreamHelper.SELECTED_LISTS_FILE_KEY, listMap);
        }

    }

}
