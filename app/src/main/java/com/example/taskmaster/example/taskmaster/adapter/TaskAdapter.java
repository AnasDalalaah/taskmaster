package com.example.taskmaster.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.example.taskmaster.R;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {


    private static final String TAG = "TaskAdapter";
    private final List<Task> task;
    private final OnTaskItemClickListener listener;

    public interface OnTaskItemClickListener {
        void onItemClicked(int position);

        void onDeleteItem(int position);
    }


    public TaskAdapter(List<Task> tasks, OnTaskItemClickListener listener) {
        this.task = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new TaskViewHolder(view, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task oneTask = task.get(position);

        holder.taskTitle.setText(oneTask.getTitle());
        holder.taskBody.setText(oneTask.getBody());
        intent.putExtra("lat", task.getLat());
        intent.putExtra("lon", task.getLon());
        view.getContext().startActivity(intent);
//        holder.taskState.setText(oneTask.getState());

        Log.i(TAG, "onBindViewHolder: Called for position" + position);

    }

    @Override
    public int getItemCount() {
        return task.size();
    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskTitle;
        private final TextView taskBody;
        private final TextView taskState;

        TaskViewHolder(@NonNull View itemView, OnTaskItemClickListener listener) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.task_title);
            taskBody = itemView.findViewById(R.id.task_body);
            taskState = itemView.findViewById(R.id.task_state);

            ImageButton delete = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(v -> {
                Log.i(TAG, "onClick: called");
                listener.onItemClicked(getAdapterPosition());
            });

            delete.setOnClickListener(v ->
                    listener.onDeleteItem(getAdapterPosition()));
        }
    }
}