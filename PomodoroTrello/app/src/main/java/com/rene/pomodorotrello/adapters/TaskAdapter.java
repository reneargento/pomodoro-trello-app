package com.rene.pomodorotrello.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.vo.CardList;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by rene on 6/19/16.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<CardList> cardList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;

        public ViewHolder(View view) {
            super(view);

            this.nameTextView = (TextView) view.findViewById(R.id.card_name);
        }
    }

    public TaskAdapter(List<CardList> cardList) {
        this.cardList = cardList;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_card, parent, false);

        return new ViewHolder(view);
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
