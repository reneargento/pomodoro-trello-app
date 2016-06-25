package com.rene.pomodorotrello.ui.pomodoro;

import android.util.Log;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.CardDatabaseController;
import com.rene.pomodorotrello.controllers.NotificationController;
import com.rene.pomodorotrello.controllers.PomodoroController;
import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.controllers.TaskController;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.DatabaseFetchOperation;
import com.rene.pomodorotrello.interfaces.DeleteCardCallback;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.model.Card;
import com.rene.pomodorotrello.model.CardPomodoro;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

import static com.rene.pomodorotrello.application.PomodoroTrelloApplication.getContext;

/**
 * Created by rene on 6/24/16.
 */

@SuppressWarnings("unchecked")
class PomodoroFragmentInteractorImpl implements PomodoroFragmentInteractor {

    private PomodoroFragmentPresenter pomodoroFragmentPresenter;
    private SessionController sessionController;

    PomodoroFragmentInteractorImpl(PomodoroFragmentPresenter pomodoroFragmentPresenter) {
        this.pomodoroFragmentPresenter = pomodoroFragmentPresenter;
        sessionController = new SessionController();
    }

    @Override
    public void cancelNotification() {
        NotificationController notificationController = new NotificationController(getContext());
        notificationController.cancelNotification();
    }

    @Override
    public void addTimeSpent(boolean pomodoroPerformed, long totalTime, String currentTaskTotalTimeString) {
        long timeSpent = Constants.POMODORO_DEFAULT_TIME - totalTime;

        PomodoroController pomodoroController = new PomodoroController();
        @SuppressWarnings("redundant") //using currentTaskTotalTime for code readability
        long currentTaskTotalTime = pomodoroController.getMillisecondsFromFormattedTime(currentTaskTotalTimeString);

        long newTotalTime = currentTaskTotalTime;
        if(pomodoroPerformed) {
            newTotalTime = newTotalTime + Constants.POMODORO_DEFAULT_TIME;
        } else {
            newTotalTime = newTotalTime + timeSpent;
        }

        pomodoroFragmentPresenter.setTotalTimeTextViewText(pomodoroController.getFormattedTimeFromMilliseconds(newTotalTime));

        updateCardOnDatabase(newTotalTime);
    }

    private void updateCardOnDatabase(long newTotalTime) {
        //Saves time and pomodoro counter on database
        Card updatedCard = new Card();
        updatedCard.name = pomodoroFragmentPresenter.getDoingListSpinnerSelectedItem();
        updatedCard.totalMillisecondsSpent = newTotalTime;
        updatedCard.pomodoros = pomodoroFragmentPresenter.getPomodorosSpentTextViewValue();

        CardDatabaseController cardDatabaseController = new CardDatabaseController();
        cardDatabaseController.updateCard(updatedCard);
    }

    @Override
    public void loadListItems() {

        if (sessionController.isConnected()){

            SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getContext());
            final String doingListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_DOING_LIST_KEY);

            if (doingListName != null) {
                //User is connected and has selected a list
                final TaskController taskController = new TaskController();
                taskController.getCardsFromList(new ItemRetriever() {
                    @Override
                    public void retrieveItems(Object items) {
                        final List<Card> doingList = (List<Card>) items;

                        List<String> doingListNames = taskController.getNamesFromList(doingList);
                        pomodoroFragmentPresenter.initSpinnerAdapter(doingListNames);

                        PomodoroController getOrCreateAllCardsFetched = new PomodoroController();
                        //Used to get attributes totalMillisecondsSpent and pomodoros that are not in the server
                        getOrCreateAllCardsFetched.getOrCreateAllCardsFetched(doingList, new DatabaseFetchOperation() {
                            @Override
                            public void onOperationSuccess(List<? extends RealmObject> objectList) {
                                List<CardPomodoro> firstCard = (List<CardPomodoro>) objectList;

                                if (firstCard != null && firstCard.size() > 0 && firstCard.get(0).name.
                                        equals(doingList.get(0).name)) {
                                    updateTimeAndPomodoroLabels(firstCard.get(0));
                                }
                            }

                            @Override
                            public void onOperationError() {

                            }
                        });
                    }
                }, Constants.DOING_ID);
            } else{
                //User is connected, but has not selected a board  yet
                List<String> defaultLabel = new ArrayList<>(1);
                defaultLabel.add(getContext().getString(R.string.select_board));

                pomodoroFragmentPresenter.initSpinnerAdapter(defaultLabel);
            }
        } else {
            //User is not connected
            List<String> defaultLabel = new ArrayList<>(1);
            defaultLabel.add(getContext().getString(R.string.connect_warning));

            pomodoroFragmentPresenter.initSpinnerAdapter(defaultLabel);
        }
    }

    private void updateTimeAndPomodoroLabels(final CardPomodoro cardPomodoro) {

        PomodoroController pomodoroController = new PomodoroController();

        String formattedTime = pomodoroController.getFormattedTimeFromMilliseconds(cardPomodoro.totalMillisecondsSpent);
        pomodoroFragmentPresenter.setFormattedTimeOnTotalTimeTextView(formattedTime);
        pomodoroFragmentPresenter.setPomodorosSpentValue(String.valueOf(cardPomodoro.pomodoros));
    }

    private void updateTimeAndPomodoroLabels(String cardName) {

        CardDatabaseController cardDatabaseController = new CardDatabaseController();
        cardDatabaseController.getCardByName(cardName, new DatabaseFetchOperation() {
            @Override
            public void onOperationSuccess(List<? extends RealmObject> objectList) {
                if (objectList.size() > 0) {
                    List<CardPomodoro> cardPomodoroList = (List<CardPomodoro>) objectList;
                    CardPomodoro cardPomodoro = cardPomodoroList.get(0);

                    PomodoroController pomodoroController = new PomodoroController();
                    String formattedTime = pomodoroController.getFormattedTimeFromMilliseconds(cardPomodoro.totalMillisecondsSpent);
                    pomodoroFragmentPresenter.setFormattedTimeOnTotalTimeTextView(formattedTime);
                    pomodoroFragmentPresenter.setPomodorosSpentValue(String.valueOf(cardPomodoro.pomodoros));
                }
            }

            @Override
            public void onOperationError() {
                Log.e(Constants.LOG_KEY, "An error occurred while updating views");
            }
        });
    }

    @Override
    public void onItemSelected() {

        String cardName = pomodoroFragmentPresenter.getDoingListSpinnerSelectedItem();

        //If card does not exist on database, create it
        if (cardName != null) {
            CardDatabaseController cardDatabaseController = new CardDatabaseController();
            cardDatabaseController.getCardByName(cardName, new DatabaseFetchOperation() {
                @Override
                public void onOperationSuccess(List<? extends RealmObject> objectList) {

                    List<CardPomodoro> cardList = (List<CardPomodoro>) objectList;
                    if(cardList != null && cardList.size() > 0) {
                        updateTimeAndPomodoroLabels(cardList.get(0));
                    } else {
                        pomodoroFragmentPresenter.resetTotalTimeLabel();
                        pomodoroFragmentPresenter.resetPomodorosLabel();
                    }
                }

                @Override
                public void onOperationError() {
                    Log.e(Constants.LOG_KEY, "An exception occurred while fetching card from spinner");
                }
            });
        }

        pomodoroFragmentPresenter.resetTimer();
    }

    @Override
    public void incrementPomodoroCounter(int currentPomodoroCounter) {
        currentPomodoroCounter++;
        pomodoroFragmentPresenter.setPomodorosSpentValue(String.valueOf(currentPomodoroCounter));
    }

    @Override
    public void playSound() {
        PomodoroController pomodoroController = new PomodoroController();
        pomodoroController.playSound();
    }

    private DeleteCardCallback generateDeleteCardCallback(final String cardName, final int listId) {

        return new DeleteCardCallback() {
            @Override
            public void onDeleteSuccessful(String cardId) {
                moveDoingTaskTo(listId, cardId);

                if (cardName != null) {
                    updateTimeAndPomodoroLabels(cardName);
                }
            }
        };
    }

    private void moveDoingTaskTo(int listId, String cardId) {
        final TaskController taskController = new TaskController();
        taskController.moveTask(cardId, Constants.DOING_ID, listId);

        if (listId == Constants.DONE_ID) {
            final int pomodoroCounter = pomodoroFragmentPresenter.getPomodorosSpentTextViewValue();
            final String totalTimeSpentString = pomodoroFragmentPresenter.getTotalTimeTextView();

            String generateCommentForFinishedTask = taskController.generateCommentForFinishedTask(pomodoroCounter, totalTimeSpentString);
            taskController.addCommentToTask(cardId, generateCommentForFinishedTask);
        }

        pomodoroFragmentPresenter.removeCurrentlySelectedItemFromAdapter();

        if(pomodoroFragmentPresenter.getDoingListItemsCount() == 0) {
            pomodoroFragmentPresenter.setTaskCompletedButtonVisibility(false);

            pomodoroFragmentPresenter.resetTotalTimeLabel();
            pomodoroFragmentPresenter.resetPomodorosLabel();
        }
    }

    @Override
    public void deleteTaskFromDatabase(String cardName, int listId) {
        DeleteCardCallback deleteCardCallback = generateDeleteCardCallback(cardName, listId);

        if (cardName != null) {
            CardDatabaseController cardDatabaseController = new CardDatabaseController();
            cardDatabaseController.deleteCard(cardName, deleteCardCallback);
        }
    }

}
