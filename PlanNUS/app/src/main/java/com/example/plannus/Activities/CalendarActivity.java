package com.example.plannus.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.plannus.Fragments.MondayFragment;
import com.example.plannus.Fragments.TuesdayFragment;
import com.example.plannus.R;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {
    private Button monButton, tueButton;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initVars();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mondayButton) {
            changeFragment(MondayFragment.class);
        } else if (v.getId() == R.id.tuesdayButton) {
            changeFragment(TuesdayFragment.class);
        }
    }

    public void initVars() {
        monButton = findViewById(R.id.mondayButton);
        monButton.setOnClickListener(this);
        tueButton = findViewById(R.id.tuesdayButton);
        tueButton.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
    }

    public void changeFragment(Class<? extends Fragment> fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, fragment, null)
                .commit();
    }
}