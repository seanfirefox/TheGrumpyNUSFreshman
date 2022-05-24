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
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        holder.dueDuration.setText(getDuration(model.getDeadLineDateTime()));
        holder.typeName.setText(model.getModuleName());
    }

    public String getDuration(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            Date deadLineD = format.parse(dateTime);
            DateTime deadLine = new DateTime(deadLineD);

            DateTime current = DateTime.now();
            int days = Days.daysBetween(current, deadLine).getDays();
            int hours = Hours.hoursBetween(current, deadLine).getHours() % 24;
            int minutes = Minutes.minutesBetween(current, deadLine).getMinutes() % 60;
            if (days == 0) {
                return hours + " hrs, " + minutes + " mins";
            } else if (days > 0) {
                return days + " days, " + hours + " hrs";
            } else {
                return "TASK EXPIRED";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "TASK EXPIRED";
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
