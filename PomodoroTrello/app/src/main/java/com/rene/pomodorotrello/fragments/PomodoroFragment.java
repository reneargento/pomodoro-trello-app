package com.rene.pomodorotrello.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.CardDatabaseController;
import com.rene.pomodorotrello.controllers.PomodoroController;
import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.controllers.TaskController;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.DatabaseFetchOperation;
import com.rene.pomodorotrello.interfaces.DeleteCardCallback;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Card;
import com.rene.pomodorotrello.vo.CardPomodoro;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

import static com.rene.pomodorotrello.R.id.doing_spinner;

@SuppressWarnings("unchecked")
public class PomodoroFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner doingListSpinner;
    private ArrayAdapter doingListSpinnerAdapter;

    private TextView totalTimeTextView;
    private TextView pomodorosSpentTextView;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private Button startPauseButton;

    private Button backButton;
    private Button doneButton;
    private Button shortBreakButton;
    private Button longBreakButton;

    //Used to avoid calling spinner's onItemSelected in initialization
    public boolean userIsInteracting;
    private boolean isTimerStarted = false;
    private boolean isPomodoroCompleted = false;
    private boolean isOnBreak = false;

    private long totalTime = Constants.POMODORO_DEFAULT_TIME;

    public PomodoroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        TextView selectTask = (TextView) view.findViewById(R.id.select_text_view);
        selectTask.setGravity(Gravity.CENTER_HORIZONTAL);

        doingListSpinner = (Spinner) view.findViewById(R.id.doing_spinner);
        doingListSpinner.setOnItemSelectedListener(this);

        doingListSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        loadListItems();

        initCardDetails(view);
    }

    private void initCardDetails(View view) {

        totalTimeTextView = (TextView) view.findViewById(R.id.total_time_value_text_view);
        pomodorosSpentTextView = (TextView) view.findViewById(R.id.pomodoros_spent_value_text_view);
        timerTextView = (TextView) view.findViewById(R.id.timer);

        startPauseButton = (Button) view.findViewById(R.id.start_pause_button);
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTime();
            }
        });

        Button stopButton = (Button) view.findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOnBreak) {
                    addTimeSpent(false);
                    resetTimer();
                }
            }
        });

        backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCardCallback deleteCardCallback = generateDeleteCardCallback(Constants.TO_DO_ID);
                deleteTaskFromDatabase(deleteCardCallback);
            }
        });

        doneButton = (Button) view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCardCallback deleteCardCallback = generateDeleteCardCallback(Constants.DONE_ID);
                deleteTaskFromDatabase(deleteCardCallback);
            }
        });

        shortBreakButton = (Button) view.findViewById(R.id.short_break_button);
        shortBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBreak(Constants.SHORT_BREAK_DEFAULT_TIME);
            }
        });

        longBreakButton = (Button) view.findViewById(R.id.long_break_button);
        longBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBreak(Constants.LONG_BREAK_DEFAULT_TIME);
            }
        });
    }

    private void startTimer() {

        countDownTimer = new CountDownTimer(totalTime, Constants.MILLISECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                totalTime = millisUntilFinished;

                PomodoroController pomodoroController = new PomodoroController();
                timerTextView.setText(pomodoroController.getFormattedTimeFromMilliseconds(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                if (!isOnBreak) {
                    pomodoroPerformed();
                } else {
                    resetTimer();
                }
            }
        }.start();
    }

    private void loadListItems() {
        SessionController sessionController = new SessionController();
        if (sessionController.isConnected(getContext())){

            SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getContext());
            final String doingListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_DOING_LIST_KEY);

            if (doingListName != null) {
                //User is connected and has selected a list
                final TaskController taskController = new TaskController();
                taskController.getCardsFromList(getContext(), new ItemRetriever() {
                    @Override
                    public void retrieveItems(Object items) {
                        final List<Card> doingList = (List<Card>) items;

                        List<String> doingListNames = taskController.getNamesFromList(doingList);
                        initSpinnerAdapter(doingListSpinner, doingListSpinnerAdapter, doingListNames);

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
                defaultLabel.add(getActivity().getResources().getString(R.string.select_board));

                initSpinnerAdapter(doingListSpinner, doingListSpinnerAdapter, defaultLabel);
            }
        } else {
            //User is not connected
            List<String> defaultLabel = new ArrayList<>(1);
            defaultLabel.add(getActivity().getResources().getString(R.string.connect_warning));

            initSpinnerAdapter(doingListSpinner, doingListSpinnerAdapter, defaultLabel);
        }
    }

    private void initSpinnerAdapter(Spinner spinner, ArrayAdapter arrayAdapter, List<String> labels) {
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.clear();
        arrayAdapter.addAll(labels);
        spinner.setAdapter(arrayAdapter);
    }

    //Spinner callbacks
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        if (parent.getId() == doing_spinner && userIsInteracting) {
            //If card does not exist on database, create it
            if (doingListSpinner != null && doingListSpinner.getSelectedItem() != null) {
                CardDatabaseController cardDatabaseController = new CardDatabaseController();
                String name = (String) doingListSpinner.getSelectedItem();
                cardDatabaseController.getCardByName(name, new DatabaseFetchOperation() {
                    @Override
                    public void onOperationSuccess(List<? extends RealmObject> objectList) {

                        List<CardPomodoro> cardList = (List<CardPomodoro>) objectList;
                        if(cardList != null && cardList.size() > 0) {
                            updateTimeAndPomodoroLabels(cardList.get(0));
                        } else {
                            resetPomodorosLabel();
                            resetTotalTimeLabel();
                        }
                    }

                    @Override
                    public void onOperationError() {
                        Log.e(Constants.LOG_KEY, "An exception occurred while fetching card from spinner");
                    }
                });
            }

            resetTimer();
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    private void handleTime() {
        if(isPomodoroCompleted) {
            isPomodoroCompleted = false;
            changeTaskCompletedButtonsVisibility(false);
            countDownTimer.cancel();
            totalTime = Constants.POMODORO_DEFAULT_TIME;
        }

        isOnBreak = false;

        //Start timer
        if(!isTimerStarted) {
            isTimerStarted = true;
            startPauseButton.setText(getString(R.string.pause_time));
            startTimer();
        } else {
            //Pause timer
            isTimerStarted = false;
            countDownTimer.cancel();
            startPauseButton.setText(getString(R.string.start_time));
        }
    }

    private void addTimeSpent(boolean pomodoroPerformed) {

        long timeSpent = Constants.POMODORO_DEFAULT_TIME - totalTime;

        String currentTaskTotalTimeString = totalTimeTextView.getText().toString();

        PomodoroController pomodoroController = new PomodoroController();
        @SuppressWarnings("redundant") //using currentTaskTotalTime for code readability
                long currentTaskTotalTime = pomodoroController.getMillisecondsFromFormattedTime(currentTaskTotalTimeString);

        long newTotalTime = currentTaskTotalTime;
        if(pomodoroPerformed) {
            newTotalTime = newTotalTime + Constants.POMODORO_DEFAULT_TIME;
        } else {
            newTotalTime = newTotalTime + timeSpent;
        }

        totalTimeTextView.setText(pomodoroController.getFormattedTimeFromMilliseconds(newTotalTime));

        updateCardOnDatabase(newTotalTime, pomodoroPerformed);
    }

    private void resetTimer(){

        totalTime = Constants.POMODORO_DEFAULT_TIME;
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }

        isTimerStarted = false;

        timerTextView.setText(R.string.zero_time_hours);
        startPauseButton.setText(getString(R.string.start_time));
    }

    private void resetTotalTimeLabel() {
        totalTimeTextView.setText(getString(R.string.zero_time_hours));
    }

    private void resetPomodorosLabel() {
        pomodorosSpentTextView.setText(String.valueOf(0));
    }

    private void pomodoroPerformed() {
        //Increment pomodoro counter on the screen
        int pomodoroCounter = Integer.parseInt(pomodorosSpentTextView.getText().toString());
        pomodoroCounter++;
        pomodorosSpentTextView.setText(String.valueOf(pomodoroCounter));

        addTimeSpent(true);
        resetTimer();

        //Play sound
        PomodoroController pomodoroController = new PomodoroController();
        pomodoroController.playSound(getContext());

        isPomodoroCompleted = true;
        changeTaskCompletedButtonsVisibility(true);
    }

    private void changeTaskCompletedButtonsVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;

        backButton.setVisibility(visibility);
        doneButton.setVisibility(visibility);
        shortBreakButton.setVisibility(visibility);
        longBreakButton.setVisibility(visibility);
    }

    private void moveDoingTaskTo(int listId, String cardId) {
        final TaskController taskController = new TaskController();
        taskController.moveTask(Constants.DOING_ID, listId);

        if (listId == Constants.DONE_ID) {
            final int pomodoroCounter = Integer.parseInt(pomodorosSpentTextView.getText().toString());
            final String totalTimeSpentString = totalTimeTextView.getText().toString();

            String generateCommentForFinishedTask = taskController.generateCommentForFinishedTask(pomodoroCounter, totalTimeSpentString);
            taskController.addCommentToTask(cardId, generateCommentForFinishedTask);
        }

        doingListSpinnerAdapter.remove(doingListSpinner.getSelectedItem());
        doingListSpinnerAdapter.notifyDataSetChanged();

        if(doingListSpinnerAdapter.getCount() == 0) {
            changeTaskCompletedButtonsVisibility(false);

            resetTotalTimeLabel();
            resetPomodorosLabel();
        }

    }

    private void deleteTaskFromDatabase(DeleteCardCallback deleteCardCallback) {
        if (doingListSpinner != null && doingListSpinner.getSelectedItem() != null) {
            String name = (String) doingListSpinner.getSelectedItem();

            CardDatabaseController cardDatabaseController = new CardDatabaseController();
            cardDatabaseController.deleteCard(name, deleteCardCallback);
        }
    }

    private DeleteCardCallback generateDeleteCardCallback(final int listId) {

        return new DeleteCardCallback() {
            @Override
            public void onDeleteSuccessful(String cardId) {
                moveDoingTaskTo(listId, cardId);

                if (doingListSpinner != null) {
                    String cardName = (String) doingListSpinner.getSelectedItem();
                    updateTimeAndPomodoroLabels(cardName);
                }
            }
        };
    }

    private void startBreak(long duration) {
        countDownTimer.cancel();

        isOnBreak = true;
        totalTime = duration;
        startTimer();
    }

    private void updateTimeAndPomodoroLabels(final CardPomodoro cardPomodoro) {

        PomodoroController pomodoroController = new PomodoroController();
        if (totalTimeTextView != null) {
            totalTimeTextView.setText(pomodoroController.getFormattedTimeFromMilliseconds(cardPomodoro.totalMillisecondsSpent));
        }

        if (pomodorosSpentTextView != null) {
            pomodorosSpentTextView.setText(String.valueOf(cardPomodoro.pomodoros));
        }
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
                    if (totalTimeTextView != null) {
                        totalTimeTextView.setText(pomodoroController.getFormattedTimeFromMilliseconds(cardPomodoro.totalMillisecondsSpent));
                    }

                    if (pomodorosSpentTextView != null) {
                        pomodorosSpentTextView.setText(String.valueOf(cardPomodoro.pomodoros));
                    }
                }
            }

            @Override
            public void onOperationError() {
                Log.e(Constants.LOG_KEY, "An error occurred while updating views");
            }
        });
    }

    private void updateCardOnDatabase(long newTotalTime, boolean pomodoroPerformed) {
        final Card updatedCard = new Card();

        //Saves time and pomodoro counter on database
        CardDatabaseController cardDatabaseController = new CardDatabaseController();
        String name = (String) doingListSpinner.getSelectedItem();
        cardDatabaseController.getCardByName(name, new DatabaseFetchOperation() {
            @Override
            public void onOperationSuccess(List<? extends RealmObject> objectList) {

                List<CardPomodoro> cardList = (List<CardPomodoro>) objectList;
                if(cardList != null && cardList.size() > 0) {
                    updatedCard.name = cardList.get(0).name;
                }
            }

            @Override
            public void onOperationError() {
                Log.e(Constants.LOG_KEY, "An exception occurred while updating a card");
            }
        });

        updatedCard.totalMillisecondsSpent = newTotalTime;
        if (pomodorosSpentTextView != null) {
            updatedCard.pomodoros = Integer.parseInt(pomodorosSpentTextView.getText().toString());
        }
        cardDatabaseController.updateCard(updatedCard);
    }
}