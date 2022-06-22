package com.example.plannus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.plannus.Fragments.FridayFragment;
import com.example.plannus.Fragments.MondayFragment;
import com.example.plannus.Fragments.ThursdayFragment;
import com.example.plannus.Fragments.TuesdayFragment;
import com.example.plannus.Fragments.WednesdayFragment;
import com.example.plannus.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CalendarActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView bottomNavigationView;

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
            changeFragment(MondayFragment.class);
        } else if (item.getItemId() == R.id.tueButton) {
            Log.d("tue", "selected");
            changeFragment(TuesdayFragment.class);
        } else if (item.getItemId() == R.id.wedButton) {
            Log.d("wed", "selected");
            changeFragment(WednesdayFragment.class);
        } else if (item.getItemId() == R.id.thursButton) {
            changeFragment(ThursdayFragment.class);
        } else if (item.getItemId() == R.id.friButton) {
            changeFragment(FridayFragment.class);
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
    }

    public void changeFragment(Class<? extends Fragment> fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment, null)
                .commit();
    }

}