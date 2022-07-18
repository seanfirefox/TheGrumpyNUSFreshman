package com.example.plannus.Adaptors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.plannus.utils.DateTimeUtils;
import com.example.plannus.R;
import com.example.plannus.Objects.ToDoTask;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.joda.time.DateTime;

public class AnnouncementsAdapter extends FirestoreRecyclerAdapter<ToDoTask, AnnouncementsAdapter.AnnouncementsHolder> {
    private ViewGroup view;

    public AnnouncementsAdapter(@NonNull FirestoreRecyclerOptions<ToDoTask> options) {
        super(options);
    }

    @NonNull
    @Override
    public AnnouncementsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_todolist, parent, false);
        view = parent;
        return new AnnouncementsHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnnouncementsAdapter.AnnouncementsHolder holder, int position, @NonNull ToDoTask model) {
        String status = model.getStatus();
        if (status.equals("100")) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            param.height = 0;
            param.bottomMargin = 0;
            param.topMargin = 0;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            holder.itemView.setVisibility(View.VISIBLE);
            return;
        }

        String currentTime = DateTime.now().toString("yyyyMMddHHmm");
        String time = model.getDeadLineDateTime();
        if (currentTime.compareTo(time) == 1) {
            time = "EXPIRED TASK";
        }
        String processedTime = time.equals("EXPIRED TASK")
                                ? "EXPIRED TASK"
                                : DateTimeUtils.getDuration(time);

        holder.taskName.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,
                processedTime.contains("EXPIRED")
                        ? R.drawable.ic_baseline_warning_amber_24
                        : processedTime.contains("days")
                        ? R.drawable.ic_happyface
                        : R.drawable.ic_alarmclocksquare, 0);
        holder.taskName.setText(model.getTask());
        holder.dueDuration.setText(processedTime);
        holder.typeName.setText(model.getModuleName());



    }

    public class AnnouncementsHolder extends RecyclerView.ViewHolder {
        private TextView taskName, typeName, dueDuration;

        public AnnouncementsHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.toDoTaskAnnouncements);
            typeName = itemView.findViewById(R.id.moduleNameAnnouncements);
            dueDuration = itemView.findViewById(R.id.countdownAnnouncements);
        }
    }
}
