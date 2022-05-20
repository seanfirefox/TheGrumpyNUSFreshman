package com.example.plannus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class ToDoListAdapter extends FirestoreRecyclerAdapter<ToDoTask, ToDoListAdapter.TaskHolder> {

    public ToDoListAdapter(@NonNull FirestoreRecyclerOptions<ToDoTask> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskHolder holder, int position, @NonNull ToDoTask model) {
        holder.taskName.setText(model.getTask());
        holder.tagName.setText(model.getModuleName());
        holder.status.setText(model.getStatus());
        holder.dueDate.setText(model.getDeadLineDate());
        holder.dueTime.setText(model.getDeadLineTime());
        holder.plannedDate.setText(model.getPlannedDate());
        holder.plannedTime.setText(model.getPlannedTime());
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todotask, parent, false);
        return new TaskHolder(v);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView tagName;
        TextView status;
        TextView dueDate;
        TextView dueTime;
        TextView plannedDate;
        TextView plannedTime;

        public TaskHolder(View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.toDoTaskName);
            tagName = itemView.findViewById(R.id.tagName);
            status = itemView.findViewById(R.id.statusInt);
            dueDate = itemView.findViewById(R.id.deadlineDate);
            dueTime = itemView.findViewById(R.id.deadlineTime);
            plannedDate = itemView.findViewById(R.id.PlannedDate);
            plannedTime = itemView.findViewById(R.id.PlannedTime);
        }
    }
}

//public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {
//
//    Context context;
//
//    ArrayList<ToDoTask> list;
//
//    public ToDoListAdapter(Context context, ArrayList<ToDoTask> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.todotask, parent, false);
//        return new MyViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        ToDoTask toDoTask = list.get(position);
//        holder.moduleName.setText(toDoTask.getModuleName());
//        holder.task.setText(toDoTask.getTask());
//        holder.status.setText(toDoTask.getStatus() + "%");
//        holder.deadlineDate.setText(toDoTask.getDeadLineDate());
//        holder.deadlineTime.setText(toDoTask.getDeadLineTime());
//        holder.plannedDate.setText(toDoTask.getPlannedDate());
//        holder.plannedTime.setText(toDoTask.getPlannedTime());
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView moduleName, task, status, deadlineDate, deadlineTime, plannedDate, plannedTime;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            moduleName = itemView.findViewById(R.id.moduleNameAnnouncements);
//            task = itemView.findViewById(R.id.toDoTaskAnnouncements);
//            status = itemView.findViewById(R.id.status);
//            deadlineDate = itemView.findViewById(R.id.deadlineDateAnnouncements);
//            deadlineTime = itemView.findViewById(R.id.deadlineTimeAnnouncements);
//            plannedDate = itemView.findViewById(R.id.plannedDate);
//            plannedTime = itemView.findViewById(R.id.plannedTime);
//        }
//    }
//}
