package com.rene.pomodorotrello.ui.pomodoro;

import com.rene.pomodorotrello.controllers.CardDatabaseController;
import com.rene.pomodorotrello.controllers.NotificationController;
import com.rene.pomodorotrello.controllers.PomodoroController;
import com.rene.pomodorotrello.model.Card;
import com.rene.pomodorotrello.util.Constants;

import java.util.List;

import static com.rene.pomodorotrello.application.PomodoroTrelloApplication.getContext;

/**
 * Created by rene on 6/24/16.
 */

class PomodoroFragmentPresenterImpl implements PomodoroFragmentPresenter{

    private PomodoroFragmentView pomodoroFragmentView;
    private PomodoroFragmentInteractor pomodoroFragmentInteractor;

    PomodoroFragmentPresenterImpl(PomodoroFragmentView pomodoroFragmentView) {
        this.pomodoroFragmentView = pomodoroFragmentView;
        pomodoroFragmentInteractor = new PomodoroFragmentInteractorImpl(this);
    }

    @Override
    public void onInit() {
        pomodoroFragmentView.setSelectTaskTextViewGravityCenterHorizontal();
    }

    @Override
    public void generateNotification(PomodoroFragmentView pomodoroFragmentView) {
        if (this.pomodoroFragmentView == null) {
            this.pomodoroFragmentView = pomodoroFragmentView;
        }
        if (pomodoroFragmentInteractor == null) {
            pomodoroFragmentInteractor = new PomodoroFragmentInteractorImpl(this);
        }

        NotificationController notificationController = new NotificationController(getContext());
        notificationController.generateNotification();
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
    public void deleteTaskFromDatabase(String cardName, int listId) {
        pomodoroFragmentInteractor.deleteTaskFromDatabase(cardName, listId);
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
    public String getFormattedTimeFromMilliseconds(long milliseconds) {
        PomodoroController pomodoroController = new PomodoroController();
        return pomodoroController.getFormattedTimeFromMilliseconds(milliseconds);
    }

    @Override
    public void incrementPomodoroCounter(int currentPomodoroCounter) {
        pomodoroFragmentInteractor.incrementPomodoroCounter(currentPomodoroCounter);
    }

    @Override
    public void playSound() {
        pomodoroFragmentInteractor.playSound();
    }

    @Override
    public void onDestroy() {
        pomodoroFragmentView = null;
        pomodoroFragmentInteractor = null;
    }
}
