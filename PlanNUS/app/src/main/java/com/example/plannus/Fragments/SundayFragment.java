package com.example.plannus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.plannus.Adaptors.ViewPagerAdapter;
import com.example.plannus.Fragments.ChildFragments.SundayClassFragment;
import com.example.plannus.Fragments.ChildFragments.SundayTaskFragment;
import com.example.plannus.R;
import com.example.plannus.utils.DateTimeDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SundayFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public SundayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunday, container, false);
        addFragment(view);
        return view;
    }

    private void addFragment(View view) {
        tabLayout = view.findViewById(R.id.tabSunday);
        viewPager = view.findViewById(R.id.pagerSunday);
        ViewPagerAdapter adaptor = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        adaptor.addFragment(new SundayClassFragment(), "Sunday Class");
        adaptor.addFragment(new SundayTaskFragment(), "Sunday Task");
        viewPager.setAdapter(adaptor);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Class" : "Tasks");
        }).attach();
    }

    @Override
    public void onStart() {
        super.onStart();
        setHeader();
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeader();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void setHeader() {
        int dayOfWeek = DateTimeDialog.getInstance().getDayOfWeek();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        String date = "Today: " + dateFormat.format(DateTimeDialog.getInstance().getTime());
        if (dayOfWeek == Calendar.SUNDAY) {
            ((TextView)getActivity().findViewById(R.id.calendarHeader)).setText(date);
        } else {
            Calendar c = getDateAfter(dayOfWeek, Calendar.SUNDAY);
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