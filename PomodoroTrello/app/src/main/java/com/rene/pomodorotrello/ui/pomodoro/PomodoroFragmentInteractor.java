package com.rene.pomodorotrello.ui.pomodoro;

import com.rene.pomodorotrello.interfaces.DeleteCardCallback;

/**
 * Created by rene on 6/24/16.
 */

public interface PomodoroFragmentInteractor {

    void cancelNotification();
    void addTimeSpent(boolean pomodoroPerformed, long totalTime, String currentTaskTotalTimeString);
    void loadListItems();
    void deleteTaskFromDatabase(int listId);
    void onItemSelected();

}
