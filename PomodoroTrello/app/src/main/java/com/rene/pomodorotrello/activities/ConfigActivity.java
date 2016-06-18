package com.rene.pomodorotrello.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.BoardController;
import com.rene.pomodorotrello.controllers.BoardListController;
import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.vo.Board;
import com.rene.pomodorotrello.vo.BoardList;

import java.util.ArrayList;
import java.util.List;

import static com.rene.pomodorotrello.R.id.board_spinner;
import static com.rene.pomodorotrello.R.id.doing_spinner;
import static com.rene.pomodorotrello.R.id.done_spinner;
import static com.rene.pomodorotrello.R.id.todo_spinner;

public class ConfigActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner boardSpinner;
    private Spinner toDoListSpinner;
    private Spinner doingListSpinner;
    private Spinner doneListSpinner;

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

        List<String> defaultLabel = new ArrayList<>(1);
        defaultLabel.add("Connect to Trello");

        SessionController sessionController = new SessionController();
        if (sessionController.isConnected(getApplicationContext())){

            final BoardController boardController = new BoardController();
            boardController.getBoards(getApplicationContext(), new ItemRetriever() {
                @Override
                public void retrieveItems(Object items) {
                    List<Board> boardList = (List<Board>) items;
                    boardController.saveBoardDataOnCache(boardList);

                    List<String> boardNames = boardController.getBoardsNamesFromBoardList(boardList);
                    initSpinnerAdapter(boardSpinner, boardNames);
                }
            });

        } else {
            //User is not connected
            initSpinnerAdapter(boardSpinner, defaultLabel);
            initSpinnerAdapter(toDoListSpinner, defaultLabel);
            initSpinnerAdapter(doingListSpinner, defaultLabel);
            initSpinnerAdapter(doneListSpinner, defaultLabel);
        }

    }

    private void initSpinnerAdapter(Spinner spinner, List<String> labels) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(ConfigActivity.this, android.R.layout.simple_spinner_item, labels);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    //Spinner callbacks
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        if (parent.getId() == board_spinner) {
            String boardId = BoardController.boardCache.get(parent.getItemAtPosition(pos));

            final BoardListController boardListController = new BoardListController();
            boardListController.getBoardLists(getApplicationContext(), new ItemRetriever() {
                @Override
                public void retrieveItems(Object items) {
                    List<BoardList> boardList = (List<BoardList>) items;
                    List<String> boardListNames = boardListController.getListsNamesFromBoardList(boardList);

                    initSpinnerAdapter(toDoListSpinner, boardListNames);
                    initSpinnerAdapter(doingListSpinner, boardListNames);
                    initSpinnerAdapter(doneListSpinner, boardListNames);
                }
            }, boardId);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

}
