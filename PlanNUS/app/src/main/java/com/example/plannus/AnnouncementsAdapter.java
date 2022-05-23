package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class AnnouncementsAdapter extends FirestoreRecyclerAdapter<ToDoTask, AnnouncementsAdapter.AnnouncementsHolder> {


    public AnnouncementsAdapter(@NonNull FirestoreRecyclerOptions<ToDoTask> options) {
        super(options);
    }

    @NonNull
    @Override
    public AnnouncementsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_todolist, parent, false);
        return new AnnouncementsHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnnouncementsAdapter.AnnouncementsHolder holder, int position, @NonNull ToDoTask model) {
        holder.taskName.setText(model.getTask());
        holder.dueDuration.setText(getDuration(model.getDeadLineDate(), model.getDeadLineTime()));
        holder.typeName.setText(model.getModuleName());
    }

    public String getDuration(String date, String time) {
        // date is in the form DD/MM/YYYY
        // time is in the form HH/MM
        // output is Hrs Mins if date is the same
        // or just Days Hrs if date is diff
        int day = Integer.valueOf(date.substring(0,2));
        int month = Integer.valueOf(date.substring(3,5));
        int year = Integer.valueOf(date.substring(6,10));
        int hour = Integer.valueOf(time.substring(0,2));
        int min = Integer.valueOf(time.substring(3,5));

        DateTime current = DateTime.now();
        DateTime deadline = new DateTime(year, month, day, hour, min);
        int days = Days.daysBetween(current, deadline).getDays();
        if (days == 0) {
            return String.format("%d hrs, %d mins", Math.abs(hour - current.getHourOfDay()) % 24, min);
        } else if (days > 0) {
            return String.format("%d days, %d hrs", days, Math.abs(hour - current.getHourOfDay()) % 24);
        } else {
            return "TASK EXPIRED";
        }
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
