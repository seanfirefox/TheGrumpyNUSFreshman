package com.example.plannus.Fragments.ChildFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plannus.Adaptors.CalendarTaskAdapter;
import com.example.plannus.Objects.ToDoTask;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.example.plannus.WrapContentLinearLayoutManager;
import com.example.plannus.utils.DateTimeDialog;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FridayTaskFragment extends Fragment {

    private SessionManager sessionManager;
    private String userID;
    private CalendarTaskAdapter adapter;
    private RecyclerView recyclerView;
    CollectionReference taskRef;

    public FridayTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_friday_task, container, false);
       initVars(view);
       setupRecyclerView(view);
       return view;
    }

    private void initVars(View view) {
        sessionManager = SessionManager.get();
        userID = sessionManager.getUserID();
        recyclerView = view.findViewById(R.id.fridayTaskRecyclerVIew);
        taskRef = sessionManager.getColRef(userID, "Tasks");
    }

    private void setupRecyclerView(View view) {
        int dayOfWeek = DateTimeDialog.getInstance().getDayOfWeek();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar c = getDateAfter(dayOfWeek, Calendar.FRIDAY);
        String plannedDateString = dateFormat.format(c.getTime());
        Query query = taskRef.orderBy("plannedDate", Query.Direction.ASCENDING)
                .orderBy("plannedDateTime", Query.Direction.ASCENDING)
                .whereEqualTo("plannedDate", plannedDateString);
        FirestoreRecyclerOptions<ToDoTask> options = new FirestoreRecyclerOptions.Builder<ToDoTask>()
                .setQuery(query, ToDoTask.class)
                .build();
        adapter = new CalendarTaskAdapter(options);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public Calendar getDateAfter(int dayOfWeek, int currentDay) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, dayOfWeek < currentDay
                ? currentDay - dayOfWeek
                : 7 - dayOfWeek + currentDay);
        return c;
    }


}