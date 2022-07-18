package com.example.plannus.Adaptors;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannus.Activities.ToDoList.EditTaskActivity;
import com.example.plannus.Objects.ToDoTask;
import com.example.plannus.R;
import com.example.plannus.utils.DateFormatter;
import com.example.plannus.utils.TimeFormatter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ToDoListAdapter extends FirestoreRecyclerAdapter<ToDoTask, ToDoListAdapter.TaskHolder> {

    public ToDoListAdapter(@NonNull FirestoreRecyclerOptions<ToDoTask> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskHolder holder, int position, @NonNull ToDoTask model) {

        String moduleName, task, status, deadlineDate, deadlineTime, plannedDate, plannedTime;
        String[] taskInfo;

        moduleName = model.getModuleName();
        task = model.getTask();
        status = model.getStatus();
        deadlineDate = DateFormatter.numberToDate(model.getDeadLineDateTime().substring(0, 8));
        deadlineTime = TimeFormatter.numberToTime(model.getDeadLineDateTime().substring(8, 12));
        plannedDate = DateFormatter.numberToDate(model.getPlannedDateTime().substring(0, 8));
        plannedTime = TimeFormatter.numberToTime(model.getPlannedDateTime().substring(8, 12));

        int statInt = Integer.parseInt(status);
        holder.taskName.setText(task);
        holder.tagName.setText(moduleName);
        holder.status.setProgress(statInt);
        holder.status.setProgressTintList(ColorStateList.valueOf(
                Color.rgb(statInt < 51 ? 255 : (255 - (statInt - 50) * 255 / 50),
                        statInt * 255 / 100,
                        0)));
        holder.statusText.setText(status +"%");
        holder.dueDate.setText(deadlineDate);
        holder.dueTime.setText(deadlineTime);
        holder.plannedDate.setText(plannedDate);
        holder.plannedTime.setText(plannedTime);

        taskInfo = new String[] {
                moduleName,
                task,
                status,
                deadlineDate,
                deadlineTime,
                plannedDate,
                plannedTime};

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), EditTaskActivity.class);
            intent.putExtra("taskInfo", taskInfo);

            holder.itemView.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todotask, parent, false);
        return new TaskHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private final TextView taskName;
        private final TextView tagName;
        private final TextView dueDate;
        private final TextView dueTime;
        private final TextView plannedDate;
        private final TextView plannedTime;
        private final TextView statusText;
        //private AnyChartView status;
        private final ProgressBar status;

        public TaskHolder(View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.toDoTaskName);
            tagName = itemView.findViewById(R.id.tagName);
            status = itemView.findViewById(R.id.statusProgress);
            statusText = itemView.findViewById(R.id.statusInt);
            dueDate = itemView.findViewById(R.id.deadlineDate);
            dueTime = itemView.findViewById(R.id.deadlineTime);
            plannedDate = itemView.findViewById(R.id.PlannedDate);
            plannedTime = itemView.findViewById(R.id.PlannedTime);
        }
    }
}
