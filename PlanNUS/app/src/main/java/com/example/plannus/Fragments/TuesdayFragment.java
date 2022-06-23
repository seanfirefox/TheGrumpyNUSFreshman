package com.example.plannus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plannus.Adaptors.CalendarAdapter;
import com.example.plannus.Objects.NUSClass;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.example.plannus.WrapContentLinearLayoutManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class TuesdayFragment extends Fragment {
    private SessionManager sessionManager;
    private String userID;
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;

    public TuesdayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuesday, container, false);
        initVars(view);
        setupRecyclerView(view);
        return view;
    }

    private void initVars(View view) {
        sessionManager = SessionManager.get();
        userID = sessionManager.getUserID();
        recyclerView = view.findViewById(R.id.tuesdayRecyclerVIew);
    }

    private void setupRecyclerView(View view) {
        Query query = sessionManager.getTimetableQuery(userID, "tuesdayClass");
        FirestoreRecyclerOptions<NUSClass> options = new FirestoreRecyclerOptions.Builder<NUSClass>()
                .setQuery(query, NUSClass.class)
                .build();
        adapter = new CalendarAdapter(options);
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
}