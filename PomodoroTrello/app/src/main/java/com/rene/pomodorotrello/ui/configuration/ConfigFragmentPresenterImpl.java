package com.rene.pomodorotrello.ui.configuration;

import android.content.Context;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.BoardController;

import java.util.ArrayList;
import java.util.List;

import static com.rene.pomodorotrello.application.PomodoroTrelloApplication.getContext;

/**
 * Created by rene on 6/24/16.
 */

public class ConfigFragmentPresenterImpl implements ConfigFragmentPresenter {

    private ConfigFragmentView configFragmentView;
    private ConfigFragmentInteractor configFragmentInteractor;

    ConfigFragmentPresenterImpl(ConfigFragmentView configFragmentView) {
        this.configFragmentView = configFragmentView;
        configFragmentInteractor = new ConfigFragmentInteractorImpl(this);
    }

    @Override
    public void onInit(Context context) {

        if (!configFragmentInteractor.isConnected()) {
            login(context);
        } else {
            configFragmentView.setConnectButtonGone();
        }

        loadSpinners();
    }

    @Override
    public void login(Context context) {
        configFragmentInteractor.login(context);
    }

    @Override
    public void onConnectionSuccessful() {
        configFragmentView.setConnectButtonGone();
        loadSpinners();
    }

    @Override
    public void loadSpinners() {
        if (configFragmentInteractor.isConnected()){
            configFragmentInteractor.getBoards();
        } else {
            //User is not connected
            List<String> defaultLabel = new ArrayList<>(1);
            defaultLabel.add(getContext().getString(R.string.connect_warning));

            configFragmentView.initBoardSpinnerAdapter(defaultLabel);
            configFragmentView.initToDoSpinnerAdapter(defaultLabel);
            configFragmentView.initDoingSpinnerAdapter(defaultLabel);
            configFragmentView.initDoneSpinnerAdapter(defaultLabel);
        }
    }

    @Override
    public void initBoardSpinnerAdapter(List<String> boardNames) {
        configFragmentView.initBoardSpinnerAdapter(boardNames);
    }

    @Override
    public void initToDoSpinnerAdapter(List<String> boardListNames) {
        configFragmentView.initToDoSpinnerAdapter(boardListNames);
    }

    @Override
    public void initDoingSpinnerAdapter(List<String> boardListNames) {
        configFragmentView.initDoingSpinnerAdapter(boardListNames);
    }

    @Override
    public void initDoneSpinnerAdapter(List<String> boardListNames) {
        configFragmentView.initDoneSpinnerAdapter(boardListNames);
    }

    @Override
    public void selectBoardSpinnerItem(String boardName) {
        configFragmentView.selectBoardSpinnerItem(boardName);
    }

    @Override
    public void selectToDoSpinnerItem(String listName) {
        configFragmentView.selectToDoSpinnerItem(listName);
    }

    @Override
    public void selectDoingSpinnerItem(String listName) {
        configFragmentView.selectDoingSpinnerItem(listName);
    }

    @Override
    public void selectDoneSpinnerItem(String listName) {
        configFragmentView.selectDoneSpinnerItem(listName);
    }

    @Override
    public void saveSettings(String boardName, String toDoListName, String doingListName, String doneListName) {
        configFragmentInteractor.saveSettings(boardName, toDoListName, doingListName, doneListName);
    }

    @Override
    public void showSavedValuesDialogMessage() {
        configFragmentView.showSavedValuesDialogMessage();
    }

    @Override
    public void onBoardSelected(String boardName) {
        String boardId = BoardController.boardCache.get(boardName);
        configFragmentInteractor.loadListItems(boardId);
    }

    @Override
    public void onDestroy() {
        configFragmentView = null;
        configFragmentInteractor = null;
    }
}
