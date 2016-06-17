package com.rene.pomodorotrello.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.BoardController;
import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.vo.Board;

import java.util.List;

import static com.rene.pomodorotrello.R.id.board_spinner;

public class ConfigActivity extends AppCompatActivity {

    private Spinner boardSpinner;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        SessionController sessionController = new SessionController();
        if (!sessionController.isConnected(getApplicationContext())){
            sessionController.login(ConfigActivity.this);
        }

        initView();
    }

    private void initView() {

        boardSpinner = (Spinner) findViewById(board_spinner);

        SessionController sessionController = new SessionController();
        if (sessionController.isConnected(getApplicationContext())){

            final BoardController boardController = new BoardController();
            boardController.getBoards(getApplicationContext(), new ItemRetriever() {
                @Override
                public void retrieveItems(Object items) {
                    List<Board> boardList = (List<Board>) items;
                    List<String> boardNames = boardController.getBoardsNamesFromBoardList(boardList);
                    initSpinnerAdapter(boardNames);
                }
            });
        }

    }

    private void initSpinnerAdapter(List<String> boardNames) {

        arrayAdapter = new ArrayAdapter(ConfigActivity.this, android.R.layout.simple_spinner_item, boardNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boardSpinner.setAdapter(arrayAdapter);
    }
}
