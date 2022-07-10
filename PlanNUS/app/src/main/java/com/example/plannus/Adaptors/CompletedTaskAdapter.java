package com.example.plannus.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private static double randDouble = Math.random();

    @Override
    protected void onBindViewHolder(@NonNull CompletedTaskAdapter.FinishedTaskHolder holder, int position, @NonNull ToDoTask model) {
        String moduleName, task, deadlineDate, deadlineTime, plannedDate, plannedTime;

        moduleName = model.getModuleName();
        task = model.getTask();
        deadlineDate = DateFormatter.numberToDate(model.getDeadLineDateTime().substring(0, 8));
        deadlineTime = TimeFormatter.numberToTime(model.getDeadLineDateTime().substring(8, 12));
        plannedDate = DateFormatter.numberToDate(model.getPlannedDateTime().substring(0, 8));
        plannedTime = TimeFormatter.numberToTime(model.getPlannedDateTime().substring(8, 12));

        double luckyNumber = Math.random();

        holder.taskName.setText(task);
        holder.imageReward.setImageResource(
                luckyNumber < 0.333 ? R.drawable.ic_ribbon_sticker_svgrepo_com :
                        luckyNumber < 0.666 ? R.drawable.ic_medal_svgrepo_com :
                                R.drawable.ic_trophy_svgrepo_com
        );
        holder.tagName.setText(moduleName);
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

        private TextView taskName, tagName, dueDate, dueTime, plannedDate, plannedTime;
        private ImageView imageReward;

        public FinishedTaskHolder(View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.toDoTaskNameCompleted);
            tagName = itemView.findViewById(R.id.tagNameCompleted);
            imageReward = itemView.findViewById(R.id.completedReward);
            dueDate = itemView.findViewById(R.id.deadlineDateCompleted);
            dueTime = itemView.findViewById(R.id.deadlineTimeCompleted);
            plannedDate = itemView.findViewById(R.id.PlannedDateCompleted);
            plannedTime = itemView.findViewById(R.id.PlannedTimeCompleted);

        }
    }
}
