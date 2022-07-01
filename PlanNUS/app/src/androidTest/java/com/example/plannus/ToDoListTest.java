package com.example.plannus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;
import android.widget.DatePicker;

import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.plannus.Activities.AddTaskActivity;
import com.example.plannus.Activities.ContentMainActivity;
import com.example.plannus.Activities.MainActivity;
import com.example.plannus.Activities.ToDoList;
import com.example.plannus.utils.ProgressBarSetter;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ToDoListTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private String email = "admin2@gmail.com";
    private String password = "1234567";
    private String task = "Milestone 3";
    private String tag = "CP2106";
    private int status = 50;
    private String deadlineDate = "02/01/2023";
    private String deadlineTime = "00:00";
    private String plannedDate = "01/01/2023";
    private String plannedTime = "12:00";

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void somthing() throws InterruptedException {
        onView(withId(R.id.emailAddress)).perform(ViewActions.typeText(email));
        onView(withId(R.id.passWord)).perform(ViewActions.typeText(password));
        onView(withId(R.id.loginButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(ContentMainActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.checklistImgView)).perform(ViewActions.click());
        intending(hasComponent(ToDoList.class.getName()));
        recyclerViewDisplayed();
        addTask();
    }

    public void recyclerViewDisplayed() {
        onView(withId(R.id.myTasks)).check(matches(isDisplayed()));
        onView(withId(R.id.taskListAnnouncements)).check(matches(isDisplayed()));
        onView(withId(R.id.milestone)).check(matches(isDisplayed()));
        onView(withId(R.id.createTask)).check(matches(isDisplayed()));
    }

    public void addTask() throws InterruptedException {
        onView(withId(R.id.createTask)).perform(ViewActions.click());
        intending(hasComponent(AddTaskActivity.class.getName()));
        onView(withId(R.id.taskDescEditText)).perform(ViewActions.typeText(task));
        onView(withId(R.id.taskDescEditStatus)).perform(ProgressBarSetter.scrubSeekBarAction(status));
        Thread.sleep(1000);
        onView(withId(R.id.taskTypeEditText)).perform(ViewActions.typeText(tag), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.dueDateButton)).perform(ViewActions.click());
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 01, 02));

    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
