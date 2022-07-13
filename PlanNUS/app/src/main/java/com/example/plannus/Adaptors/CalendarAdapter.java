package com.example.plannus.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannus.Objects.NUSClass;
import com.example.plannus.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CalendarAdapter extends FirestoreRecyclerAdapter<NUSClass, CalendarAdapter.CalendarHolder> {

    public CalendarAdapter(@NonNull FirestoreRecyclerOptions<NUSClass> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull CalendarAdapter.CalendarHolder holder, int position, @NonNull NUSClass model) {
        String moduleName, classType, slot, classes, timing;
        int start, end;
        moduleName = model.getModule();
        classType = model.getClassType();
        slot = model.getSlot();
        start = model.getStart();
        int startTiming = start;
        end = model.getEnd();
        classes = classType + " " + slot;
        timing = start + " - " + end;

        holder.module.setText(moduleName);
        holder.classes.setText(classes);
        holder.timing.setText(timing);
        if (startTiming < 1200) {
            holder.iconTiming.setImageResource(R.drawable.ic_morning_am_adobe_express);
        } else if (startTiming < 1600) {
            holder.iconTiming.setImageResource(R.drawable.ic_afternoon_am_adobe_express);
        } else if (startTiming < 1800) {
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
        private TextView module, classes, timing;
        private ImageView iconTiming;

        public CalendarHolder(View itemView) {
            super(itemView);
            module = itemView.findViewById(R.id.nusCLassModule);
            classes = itemView.findViewById(R.id.nusCLassClass);
            timing = itemView.findViewById(R.id.nusCLassTiming);
            iconTiming = itemView.findViewById(R.id.dayTiming);
        }

    }
}
