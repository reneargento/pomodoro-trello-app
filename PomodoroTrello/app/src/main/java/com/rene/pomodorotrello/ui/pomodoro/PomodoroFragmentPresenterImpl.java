package com.rene.pomodorotrello.ui.pomodoro;

import com.rene.pomodorotrello.interfaces.DeleteCardCallback;

import java.util.List;

/**
 * Created by rene on 6/24/16.
 */

public class PomodoroFragmentPresenterImpl implements PomodoroFragmentPresenter{

    private PomodoroFragmentView pomodoroFragmentView;
    private PomodoroFragmentInteractor pomodoroFragmentInteractor;

    public PomodoroFragmentPresenterImpl(PomodoroFragmentView pomodoroFragmentView) {
        this.pomodoroFragmentView = pomodoroFragmentView;
        pomodoroFragmentInteractor = new PomodoroFragmentInteractorImpl(this);
    }

    @Override
    public void onInit() {
        pomodoroFragmentView.setSelectTaskTextViewGravityCenterHorizontal();

    }

    @Override
    public void cancelNotification() {
        pomodoroFragmentInteractor.cancelNotification();
    }

    @Override
    public void addTimeSpent(boolean pomodoroPerformed, long totalTime, String currentTaskTotalTimeString) {
        pomodoroFragmentInteractor.addTimeSpent(pomodoroPerformed, totalTime, currentTaskTotalTimeString);
    }

    @Override
    public void setTotalTimeTextViewText(String text) {
        pomodoroFragmentView.setTotalTimeTextViewText(text);
    }


    @Override
    public String getDoingListSpinnerSelectedItem() {
        return pomodoroFragmentView.getDoingListSpinnerSelectedItem();
    }

    @Override
    public int getPomodorosSpentTextViewValue() {
        return pomodoroFragmentView.getPomodorosSpentTextViewValue();
    }

    @Override
    public void loadListItems() {
        pomodoroFragmentInteractor.loadListItems();
    }

    @Override
    public void initSpinnerAdapter(List<String> doingListNames) {
        pomodoroFragmentView.initSpinnerAdapter(doingListNames);
    }

    @Override
    public void setFormattedTimeOnTotalTimeTextView(String formattedTime) {
        pomodoroFragmentView.setFormattedTimeOnTotalTimeTextView(formattedTime);
    }

    @Override
    public void setPomodorosSpentValue(String pomodorosSpent) {
        pomodoroFragmentView.setPomodorosSpentValue(pomodorosSpent);
    }

    @Override
    public String getTotalTimeTextView() {
        return pomodoroFragmentView.getTotalTimeTextView();
    }

    @Override
    public void removeCurrentlySelectedItemFromAdapter() {
        pomodoroFragmentView.removeCurrentlySelectedItemFromAdapter();
    }

    @Override
    public int getDoingListItemsCount() {
        return pomodoroFragmentView.getDoingListItemsCount();
    }

    @Override
    public void setTaskCompletedButtonVisibility(boolean visible) {
        pomodoroFragmentView.setTaskCompletedButtonVisibility(visible);
    }

    @Override
    public void resetTotalTimeLabel() {
        pomodoroFragmentView.resetTotalTimeLabel();
    }

    @Override
    public void resetPomodorosLabel() {
        pomodoroFragmentView.resetPomodorosLabel();
    }

    @Override
    public void deleteTaskFromDatabase(int listId) {
        pomodoroFragmentInteractor.deleteTaskFromDatabase(listId);
    }

    @Override
    public void resetTimer() {
        pomodoroFragmentView.resetTimer();
    }

    @Override
    public void onItemSelected() {
        pomodoroFragmentInteractor.onItemSelected();
    }

    @Override
    public void onDestroy() {

    }
}
