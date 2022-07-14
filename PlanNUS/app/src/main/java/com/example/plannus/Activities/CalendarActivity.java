package com.example.plannus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.plannus.Fragments.FridayFragment;
import com.example.plannus.Fragments.MondayFragment;
import com.example.plannus.Fragments.ThursdayFragment;
import com.example.plannus.Fragments.TuesdayFragment;
import com.example.plannus.Fragments.WednesdayFragment;
import com.example.plannus.R;
import com.example.plannus.utils.DateTimeDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView bottomNavigationView;
    private TextView calendarHeader;
    HashMap<Integer, Class<? extends Fragment>> hashMap = new HashMap<>();
    int dayOfWeek, daySelected;
    String[] header = {"Your Classes For Monday", "Your Classes For Tuesday", "Your Classes For Wednesday", "Your Classes For Thursday", "Your Classes For Friday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initVars();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.monButton) {
            Log.d("mon", "selected");
            changeFragment(MondayFragment.class, 0);
        } else if (item.getItemId() == R.id.tueButton) {
            Log.d("tue", "selected");
            changeFragment(TuesdayFragment.class, 1);
        } else if (item.getItemId() == R.id.wedButton) {
            Log.d("wed", "selected");
            changeFragment(WednesdayFragment.class, 2);
        } else if (item.getItemId() == R.id.thursButton) {
            changeFragment(ThursdayFragment.class, 3);
        } else if (item.getItemId() == R.id.friButton) {
            changeFragment(FridayFragment.class, 4);
        } else {
            Log.d("fail", "nothing");
        }

        return true;
    }


    public void initVars() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        calendarHeader = findViewById(R.id.calendarHeader);


        int[] idDays = {R.id.monButton, R.id.tueButton, R.id.wedButton, R.id.thursButton, R.id.friButton};
        dayOfWeek = DateTimeDialog.getInstance().getDayOfWeek();
        daySelected = dayOfWeek > 1 && dayOfWeek < 7
                ? dayOfWeek - 2
                : 0;
        bottomNavigationView.setSelectedItemId(idDays[daySelected]);
        hashMap.put(2, MondayFragment.class);
        hashMap.put(3, TuesdayFragment.class);
        hashMap.put(4, WednesdayFragment.class);
        hashMap.put(5, ThursdayFragment.class);
        hashMap.put(6, FridayFragment.class);
        hashMap.put(7, MondayFragment.class);
        hashMap.put(1, MondayFragment.class);
        changeFragment(hashMap.get(dayOfWeek), daySelected);


    }

    public void changeFragment(Class<? extends Fragment> fragment, int daySelectedForFrag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment, null)
                .commit();
//        if (daySelected == daySelectedForFrag) {
//            calendarHeader.setText("Your Classes For Today");
//        } else {
//            calendarHeader.setText(header[daySelectedForFrag]);
//        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        calendarHeader.setText(dateFormat.format(DateTimeDialog.getInstance().getTime()));
    }

}