package com.rene.pomodorotrello.ui.tasks;

import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.controllers.TaskController;
import com.rene.pomodorotrello.interfaces.GetCardsCallback;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.model.Card;

import java.util.List;

/**
 * Created by rene on 6/24/16.
 */

@SuppressWarnings("unchecked")
public class TasksFragmentInteractorImpl implements TasksFragmentInteractor {

    private SessionController sessionController;
    private TaskController taskController;

    TasksFragmentInteractorImpl() {
        sessionController = new SessionController();
        taskController = new TaskController();
    }

    @Override
    public void onGetCards(int listId, final GetCardsCallback getCardsCallback) {
        taskController.getCardsFromList(new ItemRetriever() {
            @Override
            public void retrieveItems(Object items) {
                List<Card> cardList = (List<Card>) items;

                if (cardList != null) {
                    getCardsCallback.onSuccessGetCards(cardList);
                } else {
                    getCardsCallback.onFailureGetCards();
                }
            }
        }, listId);
    }

    @Override
    public boolean isConnected() {
        return sessionController.isConnected();
    }
}