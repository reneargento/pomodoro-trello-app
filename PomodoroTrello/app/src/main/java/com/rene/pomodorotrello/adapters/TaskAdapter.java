package com.rene.pomodorotrello.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.PomodoroController;
import com.rene.pomodorotrello.controllers.TaskController;
import com.rene.pomodorotrello.interfaces.GetTimeSpentCallback;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.model.Card;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rene on 6/19/16.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Card> card;
    private int listId;

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_name)
        TextView nameTextView;
        @BindView(R.id.start_task_button)
        Button startButton;
        @BindView(R.id.time_spent_title)
        TextView timeSpentTitleTextView;
        @BindView(R.id.time_spent)
        TextView timeSpentTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public TaskAdapter(List<Card> cardList, int listId) {
        this.card = cardList;
        this.listId = listId;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.nameTextView.setText(card.get(position).name);
        holder.nameTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        final TaskController taskController = new TaskController();

        if (listId == Constants.TO_DO_ID) {
            holder.startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskController.moveTask(card.get(holder.getAdapterPosition()).id, Constants.TO_DO_ID, Constants.DOING_ID);

                    card.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });

            holder.timeSpentTitleTextView.setVisibility(View.GONE);
        } else {
            holder.startButton.setVisibility(View.GONE);

            if (listId == Constants.DOING_ID) {
                final PomodoroController pomodoroController = new PomodoroController();

                taskController.getDoingTaskTimeSpent(card.get(position).name, new GetTimeSpentCallback() {
                    @Override
                    public void onSuccessGetTimeSpent(long timeSpent) {
                        String formattedTime = pomodoroController.getFormattedTimeFromMilliseconds(timeSpent);
                        holder.timeSpentTextView.setText(formattedTime);
                    }

                    @Override
                    public void onFailureGetTimeSpent() {
                        String formattedTime = pomodoroController.getFormattedTimeFromMilliseconds(0);
                        holder.timeSpentTextView.setText(formattedTime);
                    }
                });
            } else {
                holder.timeSpentTitleTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return card.size();
    }
}
