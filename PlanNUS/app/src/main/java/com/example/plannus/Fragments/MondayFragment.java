package com.example.plannus.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plannus.Adaptors.CalendarAdapter;
import com.example.plannus.Adaptors.ViewPagerAdapter;
import com.example.plannus.Fragments.ChildFragments.MondayClassFragment;
import com.example.plannus.Fragments.ChildFragments.MondayTaskFragment;
import com.example.plannus.Objects.NUSClass;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.example.plannus.WrapContentLinearLayoutManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.Query;

public class MondayFragment extends Fragment {
    private SessionManager sessionManager;
    private String userID;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;
    public MondayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_monday, container, false);
        View view = inflater.inflate(R.layout.fragment_monday_task, container, false);
        initVars(view);
//        setupRecyclerView(view);
        return view;
    }

    private void initVars(View view) {
        sessionManager = SessionManager.get();
        userID = sessionManager.getUserID();
        tabLayout = view.findViewById(R.id.tabMonday);
        viewPager = view.findViewById(R.id.pagerMonday);
        System.out.println(viewPager);
        ViewPagerAdapter adaptor = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        System.out.println(adaptor);
        adaptor.addFragment(new MondayClassFragment(), "Monday Class");
        adaptor.addFragment(new MondayTaskFragment(), "Monday Task");
        viewPager.setAdapter(adaptor);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(position == 1 ? "Classes" : "Tasks");
            }
        }).attach();

//        recyclerView = view.findViewById(R.id.mondayRecyclerVIew);
    }

//    private void setupRecyclerView(View view) {
//        Query query = sessionManager.getTimetableQuery(userID, "mondayClass");
//        FirestoreRecyclerOptions<NUSClass> options = new FirestoreRecyclerOptions.Builder<NUSClass>()
//                .setQuery(query, NUSClass.class)
//                .build();
//        adapter = new CalendarAdapter(options);
//        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));
//        recyclerView.setAdapter(adapter);
//    }

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