package com.rene.pomodorotrello.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.adapters.TaskAdapter;
import com.rene.pomodorotrello.controllers.SessionController;
import com.rene.pomodorotrello.controllers.TaskController;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Card;

import java.util.List;

public class TasksFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public static final String LIST_ID = "listId";

    private int listId = Constants.TO_DO_ID;

    private RecyclerView listRecyclerView;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager listLayoutManager;

    public TasksFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listId = getArguments().getInt(LIST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        initRecyclerView(view);

        SessionController sessionController = new SessionController();
        if (!sessionController.isConnected(getActivity().getApplicationContext())) {
            TextView warningTextView = (TextView) view.findViewById(R.id.warning_text_view);
            warningTextView.setText(getActivity().getResources().getString(R.string.connect_warning_to_see_tasks));
            warningTextView.setVisibility(View.VISIBLE);
            warningTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

            listRecyclerView.setVisibility(View.GONE);
        }

        return view;
    }

    private void initRecyclerView(View view) {
        listRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recycler_view);
        listRecyclerView.setHasFixedSize(true);
        listLayoutManager = new LinearLayoutManager(getActivity());
        listRecyclerView.setLayoutManager(listLayoutManager);

        TaskController taskController = new TaskController();
        getCards(taskController);
    }

    private void getCards(TaskController taskController) {

        taskController.getCardsFromList(getActivity().getApplicationContext(), new ItemRetriever() {
            @Override
            public void retrieveItems(Object items) {
                List<Card> cardList = (List<Card>) items;

                listAdapter = new TaskAdapter(cardList, listId);
                listRecyclerView.setAdapter(listAdapter);
            }
        }, listId);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
