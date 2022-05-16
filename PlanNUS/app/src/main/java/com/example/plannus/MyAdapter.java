package com.example.plannus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<ToDoTask> list;

    public MyAdapter(Context context, ArrayList<ToDoTask> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.todotask, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoTask toDoTask = list.get(position);
        holder.moduleName.setText(toDoTask.getModuleName());
        holder.task.setText(toDoTask.getTask());
        holder.status.setText(toDoTask.getStatus());
        holder.deadLine.setText(toDoTask.getDeadLine());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView moduleName, task, status, deadLine;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            moduleName = itemView.findViewById(R.id.moduleName);
            task = itemView.findViewById(R.id.toDoTask);
            status = itemView.findViewById(R.id.status);
            deadLine = itemView.findViewById(R.id.deadline);
        }
    }
}
