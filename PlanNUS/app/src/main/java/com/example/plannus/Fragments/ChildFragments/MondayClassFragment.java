package com.example.plannus.Fragments.ChildFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannus.Adaptors.CalendarAdapter;
import com.example.plannus.Objects.NUSClass;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.example.plannus.WrapContentLinearLayoutManager;
import com.example.plannus.utils.DateTimeDialog;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MondayClassFragment extends Fragment {

    private SessionManager sessionManager;
    private String userID;
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;

    public MondayClassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monday_class, container, false);
        initVars(view);
        setupRecyclerView(view);
        return view;
    }

    private void initVars(View view) {
        sessionManager = SessionManager.get();
        userID = sessionManager.getUserID();
        recyclerView = view.findViewById(R.id.mondayRecyclerVIew);
    }

    private void setupRecyclerView(View view) {
        Query query = sessionManager.getTimetableQuery(userID, "mondayClass");
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
        setHeader();
        adapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeader();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void setHeader() {
        int dayOfWeek = DateTimeDialog.getInstance().getDayOfWeek();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        if (dayOfWeek == Calendar.MONDAY) {
            ((TextView)getActivity().findViewById(R.id.calendarHeader)).setText("Today: " + dateFormat.format(DateTimeDialog.getInstance().getTime()));
        } else if (dayOfWeek < Calendar.MONDAY) {
            Calendar c = getDateAfter(dayOfWeek, Calendar.MONDAY);
            ((TextView)getActivity().findViewById(R.id.calendarHeader)).setText(dateFormat.format(c.getTime()));
        }
    }

    public Calendar getDateAfter(int dayOfWeek,int currentDay) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, dayOfWeek < currentDay
                ? currentDay - dayOfWeek
                : 7 - dayOfWeek + currentDay);
        return c;
    }


}