package com.example.plannus.Fragments.ChildFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plannus.R;

public class SundayClassFragment extends Fragment {
//    private SessionManager sessionManager;
//    private String userID;
//    private CalendarAdapter adapter;
//    private RecyclerView recyclerView;

    public SundayClassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sunday_class, container, false);
//        initVars(view);
//        setupRecyclerView(view);
//        return view;
    }

//    private void initVars(View view) {
//        sessionManager = SessionManager.get();
//        userID = sessionManager.getUserID();
//        recyclerView = view.findViewById(R.id.sundayRecyclerVIew);
//    }
//
//    private void setupRecyclerView(View view) {
//        Query query = sessionManager.getTimetableQuery(userID, "sundayClass");
//        FirestoreRecyclerOptions<NUSClass> options = new FirestoreRecyclerOptions.Builder<NUSClass>()
//                .setQuery(query, NUSClass.class)
//                .build();
//        adapter = new CalendarAdapter(options);
//        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));
//        recyclerView.setAdapter(adapter);
//    }
//
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}