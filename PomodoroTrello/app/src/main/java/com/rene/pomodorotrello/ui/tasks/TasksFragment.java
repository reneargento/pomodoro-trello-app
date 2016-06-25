package com.rene.pomodorotrello.ui.tasks;

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
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Card;

import java.util.List;

public class TasksFragment extends Fragment implements TasksFragmentView {

    private OnFragmentInteractionListener mListener;
    static final String LIST_ID = "listId";

    private int listId = Constants.TO_DO_ID;

    private RecyclerView listRecyclerView;
    private TextView warningTextView;

    private TasksFragmentPresenter tasksFragmentPresenter;

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

        warningTextView = (TextView) view.findViewById(R.id.warning_text_view);
        listRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recycler_view);
        listRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager listLayoutManager = new LinearLayoutManager(getActivity());
        listRecyclerView.setLayoutManager(listLayoutManager);

        tasksFragmentPresenter = new TasksFragmentPresenterImpl(this);
        tasksFragmentPresenter.onInit(listId);

        return view;
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

    @Override
    public void setWarningTextViewConnectText() {
        warningTextView.setText(getActivity().getResources().getString(R.string.connect_warning_to_see_tasks));
    }

    @Override
    public void setWarningTextViewVisible() {
        warningTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setWarningTextGravityCenter() {
        warningTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void setListRecyclerViewVisibilityGone() {
        listRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setListCard(List<Card> cardList) {
        RecyclerView.Adapter listAdapter = new TaskAdapter(cardList, listId);
        listRecyclerView.setAdapter(listAdapter);
    }

    interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tasksFragmentPresenter.onDestroy();
    }
}

