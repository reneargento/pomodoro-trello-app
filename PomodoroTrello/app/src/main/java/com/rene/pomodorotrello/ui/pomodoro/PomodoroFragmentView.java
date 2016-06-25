package com.rene.pomodorotrello.ui.pomodoro;

import com.rene.pomodorotrello.interfaces.DeleteCardCallback;

import java.util.List;

/**
 * Created by rene on 6/24/16.
 */

public interface PomodoroFragmentView {

    void setSelectTaskTextViewGravityCenterHorizontal();
    void setTotalTimeTextViewText(String text);
    String getDoingListSpinnerSelectedItem();
    int getPomodorosSpentTextViewValue();
    void initSpinnerAdapter(List<String> doingListNames);
    void setFormattedTimeOnTotalTimeTextView(String formattedTime);
    void setPomodorosSpentValue(String pomodorosSpent);
    String getTotalTimeTextView();
    void removeCurrentlySelectedItemFromAdapter();
    int getDoingListItemsCount();
    void setTaskCompletedButtonVisibility(boolean visible);
    void resetTotalTimeLabel();
    void resetPomodorosLabel();
    void resetTimer();

}
