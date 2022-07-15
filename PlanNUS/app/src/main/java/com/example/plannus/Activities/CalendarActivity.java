package com.example.plannus.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.plannus.Adaptors.ViewPagerAdapter;
import com.example.plannus.Fragments.FridayFragment;
import com.example.plannus.Fragments.MondayFragment;
import com.example.plannus.Fragments.SaturdayFragment;
import com.example.plannus.Fragments.SundayFragment;
import com.example.plannus.Fragments.ThursdayFragment;
import com.example.plannus.Fragments.TuesdayFragment;
import com.example.plannus.Fragments.WednesdayFragment;
import com.example.plannus.R;
import com.example.plannus.utils.DateTimeDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
//    private TextView calendarHeader;
    int dayOfWeek;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initVars();
    }
    public void initVars() {
        tabLayout = findViewById(R.id.tabLayoutMain);
        viewPager = findViewById(R.id.fragmentContainerView);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        addDaysToAdaptor(viewPagerAdapter);
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Monday");
                    tab.setIcon(R.drawable.ic_monday);
                    tab.setTag("monTabMain");
                    break;
                case 1:
                    tab.setText("Tuesday");
                    tab.setIcon(R.drawable.ic_tuesday);
                    tab.setTag("tueTabMain");
                    break;
                case 2:
                    tab.setText("Wednesday");
                    tab.setIcon(R.drawable.ic_wednesday);
                    tab.setTag("wedTabMain");
                    break;
                case 3:
                    tab.setText("Thursday");
                    tab.setIcon(R.drawable.ic_thursday);
                    tab.setTag("thursTabMain");
                    break;
                case 4:
                    tab.setText("Friday");
                    tab.setIcon(R.drawable.ic_friday);
                    tab.setTag("friTabMain");
                    break;
                case 5:
                    tab.setText("Saturday");
                    tab.setIcon(R.drawable.ic_saturday);
                    tab.setTag("satTabMain");
                    break;
                case 6:
                    tab.setText("Sunday");
                    tab.setIcon(R.drawable.ic_sunday);
                    tab.setTag("sunTabMain");
                    break;
            }
        }).attach();

//        calendarHeader = findViewById(R.id.calendarHeader);
        dayOfWeek = DateTimeDialog.getInstance().getDayOfWeek();
        changeFragment(dayOfWeek);
    }

    public void changeFragment(int dayOfWeek) {
        tabLayout.selectTab(tabLayout.getTabAt(dayOfWeek > 1 ? dayOfWeek - 2 : 6));
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
//        calendarHeader.setText(dateFormat.format(DateTimeDialog.getInstance().getTime()));
    }

    public void addDaysToAdaptor(ViewPagerAdapter adpt) {
        adpt.addFragment(new MondayFragment(), "Monday");
        adpt.addFragment(new TuesdayFragment(), "Tuesday");
        adpt.addFragment(new WednesdayFragment(), "Wednesday");
        adpt.addFragment(new ThursdayFragment(), "Thursday");
        adpt.addFragment(new FridayFragment(), "Friday");
        adpt.addFragment(new SaturdayFragment(), "Saturday");
        adpt.addFragment(new SundayFragment(), "Sunday");
    }

}