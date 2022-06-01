package com.example.plannus.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannus.utils.DateFormatter;
import com.example.plannus.R;
import com.example.plannus.utils.TimeFormatter;
import com.example.plannus.Objects.ToDoTask;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CompletedTaskAdapter extends FirestoreRecyclerAdapter<ToDoTask, CompletedTaskAdapter.FinishedTaskHolder> {
    public CompletedTaskAdapter(@NonNull FirestoreRecyclerOptions<ToDoTask> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CompletedTaskAdapter.FinishedTaskHolder holder, int position, @NonNull ToDoTask model) {
        String moduleName, task, status, deadlineDate, deadlineTime, plannedDate, plannedTime;

        moduleName = model.getModuleName();
        task = model.getTask();
        status = model.getStatus();
        deadlineDate = DateFormatter.numberToDate(model.getDeadLineDateTime().substring(0, 8));
        deadlineTime = TimeFormatter.numberToTime(model.getDeadLineDateTime().substring(8, 12));
        plannedDate = DateFormatter.numberToDate(model.getPlannedDateTime().substring(0, 8));
        plannedTime = TimeFormatter.numberToTime(model.getPlannedDateTime().substring(8, 12));

        holder.taskName.setText(task);
        holder.tagName.setText(moduleName);
        holder.status.setText(status + "%");
        holder.dueDate.setText(deadlineDate);
        holder.dueTime.setText(deadlineTime);
        holder.plannedDate.setText(plannedDate);
        holder.plannedTime.setText(plannedTime);
    }

    @NonNull
    @Override
    public FinishedTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completedtask, parent, false);
        return new FinishedTaskHolder(v);
    }

    public static class FinishedTaskHolder extends RecyclerView.ViewHolder {

        TextView taskName;
        TextView tagName;
        TextView status;
        TextView dueDate;
        TextView dueTime;
        TextView plannedDate;
        TextView plannedTime;

        public FinishedTaskHolder(View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.toDoTaskNameCompleted);
            tagName = itemView.findViewById(R.id.tagNameCompleted);
            status = itemView.findViewById(R.id.statusIntCompleted);
            dueDate = itemView.findViewById(R.id.deadlineDateCompleted);
            dueTime = itemView.findViewById(R.id.deadlineTimeCompleted);
            plannedDate = itemView.findViewById(R.id.PlannedDateCompleted);
            plannedTime = itemView.findViewById(R.id.PlannedTimeCompleted);

        }
    }
}
