package com.rene.pomodorotrello.ui.configuration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.rene.pomodorotrello.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.rene.pomodorotrello.R.id.board_spinner;
import static com.rene.pomodorotrello.R.id.connect_config_button;
import static com.rene.pomodorotrello.R.id.doing_spinner;
import static com.rene.pomodorotrello.R.id.done_spinner;
import static com.rene.pomodorotrello.R.id.save_config_button;
import static com.rene.pomodorotrello.R.id.todo_spinner;

@SuppressWarnings("unchecked")
public class ConfigFragment extends Fragment implements AdapterView.OnItemSelectedListener, ConfigFragmentView {

    @BindView(board_spinner)
    Spinner boardSpinner;
    @BindView(todo_spinner)
    Spinner toDoListSpinner;
    @BindView(doing_spinner)
    Spinner doingListSpinner;
    @BindView(done_spinner)
    Spinner doneListSpinner;

    private Unbinder unbinder;

    private ArrayAdapter boardSpinnerAdapter;
    private ArrayAdapter toDoListSpinnerAdapter;
    private ArrayAdapter doingListSpinnerAdapter;
    private ArrayAdapter doneListSpinnerAdapter;

    @BindView(connect_config_button)
    Button connectButton;

    //Used to avoid calling spinner's onItemSelected in initialization
    boolean isUserInteracting;

    private ConfigFragmentPresenter configFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_config, container, false);

        initViews(view);

        configFragmentPresenter = new ConfigFragmentPresenterImpl(this);
        configFragmentPresenter.onInit(getActivity());

        return view;
    }

    private void initViews(View view) {
        initSpinners(view);
        initOtherElements(view);
    }

    private void initSpinners(View view) {

        if (view != null) {
            unbinder = ButterKnife.bind(this, view);

            boardSpinner.setOnItemSelectedListener(this);
            toDoListSpinner.setOnItemSelectedListener(this);
            doingListSpinner.setOnItemSelectedListener(this);
            doneListSpinner.setOnItemSelectedListener(this);

            boardSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
            toDoListSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
            doingListSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
            doneListSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        }
    }

    private void initOtherElements(View view) {
        if (view != null) {
            Button saveButton = ButterKnife.findById(view, save_config_button);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String boardName = (String) boardSpinner.getItemAtPosition(boardSpinner.getSelectedItemPosition());
                    String toDoListName = (String) toDoListSpinner.getItemAtPosition(toDoListSpinner.getSelectedItemPosition());
                    String doingListName = (String) doingListSpinner.getItemAtPosition(doingListSpinner.getSelectedItemPosition());
                    String doneListName = (String) doneListSpinner.getItemAtPosition(doneListSpinner.getSelectedItemPosition());

                    configFragmentPresenter.saveSettings(boardName, toDoListName, doingListName, doneListName);
                }
            });

            connectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    configFragmentPresenter.login(getActivity());
                }
            });
        }
    }

    //Spinner callbacks
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        if (parent.getId() == board_spinner && isUserInteracting) {
            configFragmentPresenter.onBoardSelected((String) parent.getItemAtPosition(pos));
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    @Override
    public void showSavedValuesDialogMessage() {

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.success_value)
                .setMessage(R.string.values_saved)
                .setNeutralButton(R.string.Ok, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    public void setConnectButtonGone() {
        connectButton.setVisibility(View.GONE);
    }

    @Override
    public void initBoardSpinnerAdapter(List<String> boardNames) {
        initSpinnerAdapter(boardSpinner, boardSpinnerAdapter, boardNames);
    }

    @Override
    public void initToDoSpinnerAdapter(List<String> boardListNames) {
        initSpinnerAdapter(toDoListSpinner, toDoListSpinnerAdapter, boardListNames);
    }

    @Override
    public void initDoingSpinnerAdapter(List<String> boardListNames) {
        initSpinnerAdapter(doingListSpinner, doingListSpinnerAdapter, boardListNames);
    }

    @Override
    public void initDoneSpinnerAdapter(List<String> boardListNames) {
        initSpinnerAdapter(doneListSpinner, doneListSpinnerAdapter, boardListNames);
    }

    private void initSpinnerAdapter(Spinner spinner, ArrayAdapter arrayAdapter, List<String> labels) {
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.clear();
        arrayAdapter.addAll(labels);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void selectBoardSpinnerItem(String boardName) {
        selectSpinnerItem(boardSpinner, boardSpinnerAdapter, boardName);
    }

    @Override
    public void selectToDoSpinnerItem(String listName) {
        selectSpinnerItem(toDoListSpinner, toDoListSpinnerAdapter, listName);
    }

    @Override
    public void selectDoingSpinnerItem(String listName) {
        selectSpinnerItem(doingListSpinner, doingListSpinnerAdapter, listName);
    }

    @Override
    public void selectDoneSpinnerItem(String listName) {
        selectSpinnerItem(doneListSpinner, doneListSpinnerAdapter, listName);
    }

    private void selectSpinnerItem(Spinner spinner, ArrayAdapter arrayAdapter, String itemName) {
        if (!isUserInteracting) {
            int position = arrayAdapter.getPosition(itemName);
            spinner.setSelection(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        configFragmentPresenter.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
