package com.rene.pomodorotrello.ui.configuration;

import android.content.Context;
import android.util.Log;

import com.rene.pomodorotrello.controllers.BoardController;
import com.rene.pomodorotrello.controllers.BoardListController;
import com.rene.pomodorotrello.controllers.ConfigController;
import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ConnectionCallback;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.model.Board;
import com.rene.pomodorotrello.model.BoardList;

import java.util.List;

import static com.rene.pomodorotrello.application.PomodoroTrelloApplication.getContext;

/**
 * Created by rene on 6/24/16.
 */
@SuppressWarnings("unchecked")
class ConfigFragmentInteractorImpl implements ConfigFragmentInteractor{

    private ConfigFragmentPresenter configFragmentPresenter;
    private SessionController sessionController;

    ConfigFragmentInteractorImpl(ConfigFragmentPresenter configFragmentPresenter) {
        this.configFragmentPresenter = configFragmentPresenter;
        sessionController = new SessionController();
    }

    @Override
    public boolean isConnected() {
        return sessionController.isConnected();
    }

    @Override
    public void login(Context context) {
        sessionController.login(context, new ConnectionCallback() {
            @Override
            public void onLoginSuccess() {
                configFragmentPresenter.onConnectionSuccessful();
            }
            @Override
            public void onLoginError() {
                Log.e(Constants.LOG_KEY, "Error on login");
            }
        });
    }

    @Override
    public void getBoards() {

        final BoardController boardController = new BoardController();
        boardController.getBoards(new ItemRetriever() {
            @Override
            public void retrieveItems(Object items) {
                List<Board> boardList = (List<Board>) items;

                List<String> boardNames = boardController.getNamesFromList(boardList);
                configFragmentPresenter.initBoardSpinnerAdapter(boardNames);

                String defaultBoardId = null;
                if (boardList.get(0) != null) {
                    defaultBoardId = boardList.get(0).id;
                }

                loadListValues(defaultBoardId);
            }
        });
    }

    private void loadListValues(String defaultBoardId) {
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getContext());
        String selectedBoard = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_BOARD_KEY);

        if (selectedBoard != null) {
            String boardId = BoardController.boardCache.get(selectedBoard);
            loadListItems(boardId);
        } else if (defaultBoardId != null) {
            loadListItems(defaultBoardId);
        }
    }

    @Override
    public void loadListItems(String boardId) {

        final BoardListController boardListController = new BoardListController();
        boardListController.getBoardLists(new ItemRetriever() {
            @Override
            public void retrieveItems(Object items) {
                List<BoardList> boardList = (List<BoardList>) items;
                List<String> boardListNames = boardListController.getNamesFromList(boardList);

                configFragmentPresenter.initToDoSpinnerAdapter(boardListNames);
                configFragmentPresenter.initDoingSpinnerAdapter(boardListNames);
                configFragmentPresenter.initDoneSpinnerAdapter(boardListNames);

                setDefaultListItemsIfNotInteracting();
            }
        }, boardId);
    }

    private void setDefaultListItemsIfNotInteracting() {

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getContext());

        String selectedBoardName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_BOARD_KEY);
        String selectedToDoListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_TODO_LIST_KEY);
        String selectedDoingListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_DOING_LIST_KEY);
        String selectedDoneListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_DONE_LIST_KEY);

        if (selectedBoardName != null) {
            configFragmentPresenter.selectBoardSpinnerItem(selectedBoardName);
        }
        if (selectedToDoListName != null) {
            configFragmentPresenter.selectToDoSpinnerItem(selectedToDoListName);
        }
        if (selectedDoingListName != null) {
            configFragmentPresenter.selectDoingSpinnerItem(selectedDoingListName);
        }
        if (selectedDoneListName != null) {
            configFragmentPresenter.selectDoneSpinnerItem(selectedDoneListName);
        }
    }

    @Override
    public void saveSettings(String boardName, String toDoListName, String doingListName, String doneListName) {
        ConfigController configController = new ConfigController();
        configController.saveSettings(boardName, toDoListName, doingListName, doneListName);

        configFragmentPresenter.showSavedValuesDialogMessage();
    }
}
