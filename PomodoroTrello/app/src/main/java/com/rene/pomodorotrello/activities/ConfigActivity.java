package com.rene.pomodorotrello.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.BoardController;
import com.rene.pomodorotrello.controllers.BoardListController;
import com.rene.pomodorotrello.controllers.ConfigController;
import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.vo.Board;
import com.rene.pomodorotrello.vo.BoardList;

import java.util.ArrayList;
import java.util.List;

import static com.rene.pomodorotrello.R.id.board_spinner;
import static com.rene.pomodorotrello.R.id.doing_spinner;
import static com.rene.pomodorotrello.R.id.done_spinner;
import static com.rene.pomodorotrello.R.id.save_config_button;
import static com.rene.pomodorotrello.R.id.todo_spinner;

public class ConfigActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner boardSpinner;
    private Spinner toDoListSpinner;
    private Spinner doingListSpinner;
    private Spinner doneListSpinner;

    private ArrayAdapter boardSpinnerAdapter;
    private ArrayAdapter toDoListSpinnerAdapter;
    private ArrayAdapter doingListSpinnerAdapter;
    private ArrayAdapter doneListSpinnerAdapter;

    //Used to avoid calling spinner's onItemSelected in initialization
    private boolean userIsInteracting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        SessionController sessionController = new SessionController();
        if (!sessionController.isConnected(getApplicationContext())){
            sessionController.login(ConfigActivity.this);
        }

        initViews();
    }

    private void initViews() {
        initSpinners();
        initOtherElements();
    }

    private void initSpinners() {
        boardSpinner = (Spinner) findViewById(board_spinner);
        toDoListSpinner = (Spinner) findViewById(todo_spinner);
        doingListSpinner = (Spinner) findViewById(doing_spinner);
        doneListSpinner = (Spinner) findViewById(done_spinner);

        boardSpinner.setOnItemSelectedListener(this);
        toDoListSpinner.setOnItemSelectedListener(this);
        doingListSpinner.setOnItemSelectedListener(this);
        doneListSpinner.setOnItemSelectedListener(this);

        boardSpinnerAdapter = new ArrayAdapter(ConfigActivity.this, android.R.layout.simple_spinner_item);
        toDoListSpinnerAdapter = new ArrayAdapter(ConfigActivity.this, android.R.layout.simple_spinner_item);
        doingListSpinnerAdapter = new ArrayAdapter(ConfigActivity.this, android.R.layout.simple_spinner_item);
        doneListSpinnerAdapter = new ArrayAdapter(ConfigActivity.this, android.R.layout.simple_spinner_item);

        List<String> defaultLabel = new ArrayList<>(1);
        defaultLabel.add("Connect to Trello");

        SessionController sessionController = new SessionController();
        if (sessionController.isConnected(getApplicationContext())){

            final BoardController boardController = new BoardController();
            boardController.getBoards(getApplicationContext(), new ItemRetriever() {
                @Override
                public void retrieveItems(Object items) {
                    List<Board> boardList = (List<Board>) items;

                    List<String> boardNames = boardController.getBoardsNamesFromBoardList(boardList);
                    initSpinnerAdapter(boardSpinner, boardSpinnerAdapter, boardNames);

                    loadListValues();
                }
            });
        } else {
            //User is not connected
            initSpinnerAdapter(boardSpinner, boardSpinnerAdapter, defaultLabel);
            initSpinnerAdapter(toDoListSpinner, toDoListSpinnerAdapter, defaultLabel);
            initSpinnerAdapter(doingListSpinner, doingListSpinnerAdapter, defaultLabel);
            initSpinnerAdapter(doneListSpinner, doneListSpinnerAdapter, defaultLabel);
        }

    }

    private void initOtherElements() {
        Button saveButton = (Button) findViewById(save_config_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String boardName = (String) boardSpinner.getItemAtPosition(boardSpinner.getSelectedItemPosition());
                String toDoListName = (String) toDoListSpinner.getItemAtPosition(toDoListSpinner.getSelectedItemPosition());
                String doingListName = (String) doingListSpinner.getItemAtPosition(doingListSpinner.getSelectedItemPosition());
                String doneListName = (String) doneListSpinner.getItemAtPosition(doneListSpinner.getSelectedItemPosition());

                ConfigController configController = new ConfigController();
                configController.saveSettings(getApplicationContext(), boardName, toDoListName,
                        doingListName, doneListName);

                showSavedValuesDialog();
            }
        });
    }

    private void initSpinnerAdapter(Spinner spinner, ArrayAdapter arrayAdapter, List<String> labels) {
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.clear();
        arrayAdapter.addAll(labels);
        spinner.setAdapter(arrayAdapter);
    }

    //Spinner callbacks
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        if (parent.getId() == board_spinner && userIsInteracting) {
            String boardId = BoardController.boardCache.get(parent.getItemAtPosition(pos));
            loadListItems(boardId);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    private void loadListItems(String boardId) {

        final BoardListController boardListController = new BoardListController();
        boardListController.getBoardLists(getApplicationContext(), new ItemRetriever() {
            @Override
            public void retrieveItems(Object items) {
                List<BoardList> boardList = (List<BoardList>) items;
                List<String> boardListNames = boardListController.getListsNamesFromBoardList(boardList);

                initSpinnerAdapter(toDoListSpinner, toDoListSpinnerAdapter, boardListNames);
                initSpinnerAdapter(doingListSpinner, doingListSpinnerAdapter, boardListNames);
                initSpinnerAdapter(doneListSpinner, doneListSpinnerAdapter, boardListNames);

                if(!userIsInteracting) {
                    setDefaultListItems();
                }

            }
        }, boardId);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    private void loadListValues() {
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getApplicationContext());
        String selectedBoard = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_BOARD_KEY);

        if (selectedBoard != null) {
            String boardId = BoardController.boardCache.get(selectedBoard);
            loadListItems(boardId);
        }
    }

    private void setDefaultListItems() {

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getApplicationContext());

        String selectedBoardName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_BOARD_KEY);
        String selectedToDoListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_TODO_LIST_KEY);
        String selectedDoingListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_DOING_LIST_KEY);
        String selectedDoneListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_DONE_LIST_KEY);

        if (selectedBoardName != null) {
            int position = boardSpinnerAdapter.getPosition(selectedBoardName);
            boardSpinner.setSelection(position);
        }
        if (selectedToDoListName != null) {
            int position = toDoListSpinnerAdapter.getPosition(selectedToDoListName);
            toDoListSpinner.setSelection(position);
        }
        if (selectedDoingListName != null) {
            int position = doingListSpinnerAdapter.getPosition(selectedDoingListName);
            doingListSpinner.setSelection(position);
        }
        if (selectedDoneListName != null) {
            int position = doneListSpinnerAdapter.getPosition(selectedDoneListName);
            doneListSpinner.setSelection(position);
        }

    }

    private void showSavedValuesDialog() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.success_value)
                .setMessage(R.string.values_saved)
                .setNeutralButton(R.string.Ok, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

}
