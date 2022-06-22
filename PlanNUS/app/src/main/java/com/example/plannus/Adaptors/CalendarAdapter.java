package com.example.plannus.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        String moduleName, classType, slot, start, end, classes, timing;
        moduleName = model.getModule();
        classType = model.getClassType();
        slot = model.getSlot();
        start = model.getStart();
        end = model.getEnd();
        classes = classType + " " + slot;
        timing = start + " - " + end;

        holder.module.setText(moduleName);
        holder.classes.setText(classes);
        holder.timing.setText(timing);
    }

    @NonNull
    @Override
    public CalendarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nus_class, parent, false);
        return CalendarHolder(v);
    }

    public class CalendarHolder extends RecyclerView.ViewHolder {
        private TextView module, classes, timing;

        public CalendarHolder(View itemView) {
            super(itemView);
            module = itemView.findViewById(R.id.nusCLassModule);
            classes = itemView.findViewById(R.id.nusCLassClass);
            timing = itemView.findViewById(R.id.nusCLassTiming);
        }

    }
}
