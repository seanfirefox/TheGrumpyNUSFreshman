package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ToDoTask> toDoList;

    public AnnouncementsAdapter(Context context, ArrayList<ToDoTask> toDoList) {
        this.context = context;
        this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.announcement_todolist, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoTask task = toDoList.get(position);
        holder.taskName.setText(task.getTask());
        holder.typeName.setText("(" + task.getModuleName() + ")");
        holder.dueDate.setText(task.getDeadLineDate());
        holder.dueTime.setText(" " + task.getDeadLineTime());
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName, typeName, dueDate, dueTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //taskHeader = itemView.findViewById(R.id.taskHeader);
            taskName = itemView.findViewById(R.id.toDoTaskAnnouncements);
            typeName = itemView.findViewById(R.id.moduleNameAnnouncements);

            //dueHeader = itemView.findViewById(R.id.dueHeader);
            dueDate = itemView.findViewById(R.id.deadlineDateAnnouncements);
            dueTime = itemView.findViewById(R.id.deadlineTimeAnnouncements);
        }
    }
}
