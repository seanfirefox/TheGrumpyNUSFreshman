package com.example.plannus.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannus.Objects.ToDoTask;
import com.example.plannus.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CalendarTaskAdapter extends FirestoreRecyclerAdapter<ToDoTask, CalendarTaskAdapter.CalendarHolder> {

    public CalendarTaskAdapter(@NonNull FirestoreRecyclerOptions<ToDoTask> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull CalendarTaskAdapter.CalendarHolder holder, int position, @NonNull ToDoTask model) {
        String tag, task, timingString;
        int timing;
        timingString = model.getPlannedDateTime().substring(8, 12);
        timing = Integer.parseInt(timingString);
        tag = model.getModuleName();
        task = model.getTask();
        holder.tag.setText(tag);
        holder.task.setText(task);
        holder.timing.setText(timingString);
        if (timing < 1200) {
            holder.iconTiming.setImageResource(R.drawable.ic_morning_am_adobe_express);
        } else if (timing < 1600) {
            holder.iconTiming.setImageResource(R.drawable.ic_afternoon_am_adobe_express);
        } else if (timing < 1800) {
            holder.iconTiming.setImageResource(R.drawable.ic_evening_am_adobe_express);
        } else {
            holder.iconTiming.setImageResource(R.drawable.ic_night_am_adobe_express);
        }
    }

    @NonNull
    @Override
    public CalendarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nus_class, parent, false);
        return new CalendarHolder(v);
    }

    public class CalendarHolder extends RecyclerView.ViewHolder {
        private TextView tag, task, timing;
        private ImageView iconTiming;

        public CalendarHolder(View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.nusCLassModule);
            task = itemView.findViewById(R.id.nusCLassClass);
            timing = itemView.findViewById(R.id.nusCLassTiming);
            iconTiming = itemView.findViewById(R.id.dayTiming);
        }

    }
}
