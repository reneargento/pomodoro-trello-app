package com.rene.pomodorotrello.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.controllers.TaskController;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Card;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by rene on 6/19/16.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Card> card;
    private int listId;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        Button startButton;

        ViewHolder(View view) {
            super(view);

            nameTextView = (TextView) view.findViewById(R.id.card_name);
            startButton = (Button) view.findViewById(R.id.start_task_button);
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.nameTextView.setText(card.get(position).name);
        holder.nameTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        if (listId != Constants.TO_DO_ID) {
            holder.startButton.setVisibility(View.GONE);
        } else {
            holder.startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   TaskController taskController = new TaskController();
                    taskController.moveTask(card.get(position).id, Constants.TO_DO_ID, Constants.DOING_ID);

                    card.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return card.size();
    }
}
