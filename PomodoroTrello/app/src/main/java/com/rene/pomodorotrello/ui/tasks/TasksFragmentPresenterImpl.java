package com.rene.pomodorotrello.ui.tasks;

import com.rene.pomodorotrello.interfaces.GetCardsCallback;
import com.rene.pomodorotrello.model.Card;

import java.util.List;

/**
 * Created by rene on 6/24/16.
 */

public class TasksFragmentPresenterImpl implements TasksFragmentPresenter, GetCardsCallback {

    private TasksFragmentView tasksFragmentView;
    private TasksFragmentInteractor tasksFragmentInteractor;

    TasksFragmentPresenterImpl(TasksFragmentView tasksFragmentView) {
        this.tasksFragmentView = tasksFragmentView;
        tasksFragmentInteractor = new TasksFragmentInteractorImpl();
    }

    @Override
    public void onInit(int listId) {
        if (!tasksFragmentInteractor.isConnected()) {
            tasksFragmentView.setWarningTextViewConnectText();
            tasksFragmentView.setWarningTextViewVisible();
            tasksFragmentView.setWarningTextGravityCenter();
            tasksFragmentView.setListRecyclerViewVisibilityGone();
        } else {
            tasksFragmentInteractor.onGetCards(listId, this);
        }
    }

    @Override
    public void onDestroy() {
        tasksFragmentView = null;
        tasksFragmentInteractor = null;
    }

    @Override
    public void onSuccessGetCards(List<Card> cardsList) {
        if (tasksFragmentView != null) {
            tasksFragmentView.setListCard(cardsList);
        }
    }

    @Override
    public void onFailureGetCards() {

    }
}