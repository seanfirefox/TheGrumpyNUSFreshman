package com.example.plannus.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.plannus.Adaptors.CalendarAdapter;
import com.example.plannus.Adaptors.ViewPagerAdapter;
import com.example.plannus.Fragments.ChildFragments.FridayClassFragment;
import com.example.plannus.Fragments.ChildFragments.FridayTaskFragment;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FridayFragment extends Fragment {
    private SessionManager sessionManager;
    private String userID;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;
    public FridayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_monday, container, false);
        View view = inflater.inflate(R.layout.fragment_friday, container, false);
        sessionManager = SessionManager.get();
        userID = sessionManager.getUserID();
        addFragment(view);
        return view;
    }

    private void addFragment(View view) {
        tabLayout = view.findViewById(R.id.tabFriday);
        viewPager = view.findViewById(R.id.pagerFriday);
        ViewPagerAdapter adaptor = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        adaptor.addFragment(new FridayClassFragment(), "Friday Class");
        adaptor.addFragment(new FridayTaskFragment(), "Friday Task");
        viewPager.setAdapter(adaptor);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Class" : "Tasks");
        }).attach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}