package com.rene.pomodorotrello.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.vo.Card;

import java.util.List;

/**
 * Created by rene on 6/19/16.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Card> cardList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;

        public ViewHolder(TextView nameTextView) {
            super(nameTextView);
            this.nameTextView = nameTextView;
        }
    }

    public TaskAdapter(List<Card> cardList) {
        this.cardList = cardList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_card, parent, false);

        TextView name = (TextView) view.findViewById(R.id.card_name);

        return new ViewHolder(name);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTextView.setText(cardList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }
}
