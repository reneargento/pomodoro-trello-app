package com.rene.pomodorotrello.ui.pomodoro;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
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
import com.rene.pomodorotrello.controllers.NotificationController;
import com.rene.pomodorotrello.controllers.PomodoroController;
import com.rene.pomodorotrello.util.Constants;

import java.util.List;

import static com.rene.pomodorotrello.R.id.doing_spinner;

@SuppressWarnings("unchecked")
public class PomodoroFragment extends Fragment implements AdapterView.OnItemSelectedListener, PomodoroFragmentView {

    private Spinner doingListSpinner;
    private ArrayAdapter doingListSpinnerAdapter;

    private TextView selectTask;
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
    boolean userIsInteracting;
    private boolean isTimerStarted = false;
    private boolean isPomodoroCompleted = false;
    private boolean isOnBreak = false;

    private long totalTime = Constants.POMODORO_DEFAULT_TIME;

    private PomodoroFragmentPresenter pomodoroFragmentPresenter;

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

        pomodoroFragmentPresenter = new PomodoroFragmentPresenterImpl(this);
        pomodoroFragmentPresenter.onInit();

        pomodoroFragmentPresenter.cancelNotification();

        pomodoroFragmentPresenter.loadListItems();

        return view;
    }

    private void initViews(View view) {
        selectTask = (TextView) view.findViewById(R.id.select_text_view);

        doingListSpinner = (Spinner) view.findViewById(R.id.doing_spinner);
        doingListSpinner.setOnItemSelectedListener(this);

        doingListSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);

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
                    pomodoroFragmentPresenter.addTimeSpent(false, totalTime, totalTimeTextView.getText().toString());

                    resetTimer();
                }
            }
        });

        backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroFragmentPresenter.deleteTaskFromDatabase(Constants.TO_DO_ID);
            }
        });

        doneButton = (Button) view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroFragmentPresenter.deleteTaskFromDatabase(Constants.DONE_ID);
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
                //Is the application running on the background?
                if (!isAdded()) {
                    NotificationController notificationController = new NotificationController(getContext());
                    notificationController.generateNotification();
                }

                if (!isOnBreak) {
                    pomodoroPerformed();
                } else {
                    resetTimer();
                }
            }
        }.start();
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
            pomodoroFragmentPresenter.onItemSelected();
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

    @Override
    public void resetTimer(){

        totalTime = Constants.POMODORO_DEFAULT_TIME;
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }

        isTimerStarted = false;

        timerTextView.setText(R.string.zero_time_hours);

        if (isAdded()) {
            startPauseButton.setText(getString(R.string.start_time));
        }
    }

    @Override
    public void resetTotalTimeLabel() {
        totalTimeTextView.setText(getString(R.string.zero_time_hours));
    }

    @Override
    public void resetPomodorosLabel() {
        pomodorosSpentTextView.setText(String.valueOf(0));
    }

    private void pomodoroPerformed() {
        //Increment pomodoro counter on the screen
        int pomodoroCounter = Integer.parseInt(pomodorosSpentTextView.getText().toString());
        pomodoroCounter++;
        pomodorosSpentTextView.setText(String.valueOf(pomodoroCounter));

        pomodoroFragmentPresenter.addTimeSpent(true, totalTime, totalTimeTextView.getText().toString());
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

    private void startBreak(long duration) {
        countDownTimer.cancel();

        isOnBreak = true;
        totalTime = duration;
        startTimer();
    }



    @Override
    public void setSelectTaskTextViewGravityCenterHorizontal() {
        selectTask.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void setTotalTimeTextViewText(String text) {
        totalTimeTextView.setText(text);
    }

    @Override
    public String getDoingListSpinnerSelectedItem() {
        String selectedItem = null;

        if (doingListSpinner != null) {
            selectedItem = (String) doingListSpinner.getSelectedItem();
        }
        return selectedItem;
    }

    @Override
    public int getPomodorosSpentTextViewValue() {
        int pomodorosSpent = 0;

        if (pomodorosSpentTextView != null) {
            pomodorosSpent = Integer.parseInt(pomodorosSpentTextView.getText().toString());
        }

        return pomodorosSpent;
    }

    @Override
    public void initSpinnerAdapter(List<String> doingListNames) {
        initSpinnerAdapter(doingListSpinner, doingListSpinnerAdapter, doingListNames);
    }

    @Override
    public void setFormattedTimeOnTotalTimeTextView(String formattedTime) {
        if (totalTimeTextView != null) {
            totalTimeTextView.setText(formattedTime);
        }
    }

    @Override
    public void setPomodorosSpentValue(String pomodorosSpent) {
        if (pomodorosSpentTextView != null) {
            pomodorosSpentTextView.setText(pomodorosSpent);
        }
    }

    @Override
    public String getTotalTimeTextView() {
        return totalTimeTextView.getText().toString();
    }

    @Override
    public void removeCurrentlySelectedItemFromAdapter() {
        doingListSpinnerAdapter.remove(doingListSpinner.getSelectedItem());
        doingListSpinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public int getDoingListItemsCount() {
        return doingListSpinnerAdapter.getCount();
    }

    @Override
    public void setTaskCompletedButtonVisibility(boolean visible) {
        changeTaskCompletedButtonsVisibility(false);
    }

}