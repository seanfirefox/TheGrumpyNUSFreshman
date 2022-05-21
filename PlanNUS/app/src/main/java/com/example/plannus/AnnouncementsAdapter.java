package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

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
        holder.typeName.setText("(" + model.getModuleName() + ")");
        holder.dueDate.setText(model.getDeadLineDate());
        holder.dueTime.setText(" " + model.getDeadLineTime());
    }


    public class AnnouncementsHolder extends RecyclerView.ViewHolder {
        private TextView taskName, typeName, dueDate, dueTime;

        public AnnouncementsHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.toDoTaskAnnouncements);
            typeName = itemView.findViewById(R.id.moduleNameAnnouncements);

            dueDate = itemView.findViewById(R.id.deadlineDateAnnouncements);
            dueTime = itemView.findViewById(R.id.deadlineTimeAnnouncements);
        }
    }
}
