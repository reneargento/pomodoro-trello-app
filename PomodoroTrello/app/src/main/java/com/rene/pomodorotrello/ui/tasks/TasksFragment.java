package com.rene.pomodorotrello.ui.tasks;

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
import com.rene.pomodorotrello.model.Card;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TasksFragment extends Fragment implements TasksFragmentView {

    static final String LIST_ID = "listId";

    private int listId = Constants.TO_DO_ID;

    @BindView(R.id.todo_recycler_view)
    RecyclerView listRecyclerView;
    @BindView(R.id.warning_text_view)
    TextView warningTextView;

    private Unbinder unbinder;

    private TasksFragmentPresenter tasksFragmentPresenter;

    public TasksFragment() {
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
        unbinder = ButterKnife.bind(this, view);

        listRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager listLayoutManager = new LinearLayoutManager(getActivity());
        listRecyclerView.setLayoutManager(listLayoutManager);

        tasksFragmentPresenter = new TasksFragmentPresenterImpl(this);
        tasksFragmentPresenter.onInit(listId);

        return view;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        tasksFragmentPresenter.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

