package com.rene.pomodorotrello.ui.pomodoro;

import com.rene.pomodorotrello.interfaces.DeleteCardCallback;

import java.util.List;

/**
 * Created by rene on 6/24/16.
 */

public interface PomodoroFragmentPresenter {

    void onInit();
    void generateNotification(PomodoroFragmentView pomodoroFragmentView);
    void cancelNotification();
    void addTimeSpent(boolean pomodoroPerformed, long totalTime, String currentTaskTotalTimeString);
    void setTotalTimeTextViewText(String text);
    String getDoingListSpinnerSelectedItem();
    int getPomodorosSpentTextViewValue();
    void loadListItems();
    void initSpinnerAdapter(List<String> doingListNames);
    void setFormattedTimeOnTotalTimeTextView(String formattedTime);
    void setPomodorosSpentValue(String pomodorosSpent);
    String getTotalTimeTextView();
    void removeCurrentlySelectedItemFromAdapter();
    int getDoingListItemsCount();
    void setTaskCompletedButtonVisibility(boolean visible);
    void resetTotalTimeLabel();
    void resetPomodorosLabel();
    void deleteTaskFromDatabase(String cardName, int listId);
    void resetTimer();
    void onItemSelected();
    String getFormattedTimeFromMilliseconds(long milliseconds);
    void incrementPomodoroCounter(int currentPomodoroCounter);
    void playSound();
    void logPomodoroCompletedOnTracker();

    void onDestroy();

}
