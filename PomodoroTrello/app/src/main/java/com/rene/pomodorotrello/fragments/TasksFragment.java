package com.rene.pomodorotrello.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.adapters.TaskAdapter;
import com.rene.pomodorotrello.controllers.TaskController;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.vo.CardList;

import java.util.List;

public class TasksFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //To do list
    private RecyclerView toDoListRecyclerView;
    private RecyclerView.Adapter toDoListAdapter;
    private RecyclerView.LayoutManager toDoListLayoutManager;

    //Doing list
    private RecyclerView doingListRecyclerView;
    private RecyclerView.Adapter doingListAdapter;
    private RecyclerView.LayoutManager doingListLayoutManager;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        initToDoRecyclerView(view);

        return view;
    }
    private void initToDoRecyclerView(View view) {
        toDoListRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recycler_view);
        toDoListRecyclerView.setHasFixedSize(true);
        toDoListLayoutManager = new LinearLayoutManager(getActivity());
        toDoListRecyclerView.setLayoutManager(toDoListLayoutManager);

        TaskController taskController = new TaskController();
        taskController.getToDoCards(getActivity().getApplicationContext(), new ItemRetriever() {
            @Override
            public void retrieveItems(Object items) {
                List<CardList> toDoCardList = (List<CardList>) items;

                toDoListAdapter = new TaskAdapter(toDoCardList);
                toDoListRecyclerView.setAdapter(toDoListAdapter);
            }
        });
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
