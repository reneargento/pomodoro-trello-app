package com.rene.pomodorotrello.fragments;

import android.os.Bundle;
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
import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.controllers.TaskController;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.CardList;

import java.util.ArrayList;
import java.util.List;

import static com.rene.pomodorotrello.R.id.doing_spinner;

@SuppressWarnings("unchecked")
public class PomodoroFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner doingListSpinner;
    private ArrayAdapter doingListSpinnerAdapter;

    //Used to avoid calling spinner's onItemSelected in initialization
    public boolean userIsInteracting;

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
        View detailedCardLayout = view.findViewById(R.id.detailed_card_layout);

        TextView runningTimeTextView = (TextView) view.findViewById(R.id.running_time_value_text_view);
        TextView totalTimeTextView = (TextView) view.findViewById(R.id.total_time_value_text_view);
        TextView pomodorosSpentTextView = (TextView) view.findViewById(R.id.pomodoros_spent_value_text_view);
        TextView timerTextView = (TextView) view.findViewById(R.id.timer);

        Button start_pause_button = (Button) view.findViewById(R.id.start_pause_button);
        Button stop_button = (Button) view.findViewById(R.id.stop_button);


    }

    private void loadListItems() {
        SessionController sessionController = new SessionController();
        if (sessionController.isConnected(getContext())){

            SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getContext());
            String doingListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_DOING_LIST_KEY);

            if (doingListName != null) {
                //User is connected and has selected a list
                final TaskController taskController = new TaskController();
                taskController.getCardsFromList(getContext(), new ItemRetriever() {
                    @Override
                    public void retrieveItems(Object items) {
                        List<CardList> doingList = (List<CardList>) items;

                        List<String> doingListNames = taskController.getNamesFromList(doingList);
                        initSpinnerAdapter(doingListSpinner, doingListSpinnerAdapter, doingListNames);
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

        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

}