package com.example.plannus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.plannus.utils.RecyclerViewChecker.hasItem;
import static org.hamcrest.CoreMatchers.not;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.plannus.Activities.ToDoList.AddTaskActivity;
import com.example.plannus.Activities.LoginRegister.ContentMainActivity;
import com.example.plannus.Activities.ToDoList.EditTaskActivity;
import com.example.plannus.Activities.LoginRegister.MainActivity;
import com.example.plannus.Activities.ToDoList.ToDoList;
import com.example.plannus.utils.ProgressBarSetter;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ToDoListTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private final String email = "deadbeef@gmail.com";
    private final String password = "deadbeef1234";
    private final String task = "Milestone 3";
    private final String tag = "CP2106";
    private final int status = 50;
    private final String newTask = "Assignment 1";
    private final String newTag = "MA2101";
    private final int newStatus = 90;

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void A_createTaskTest() throws Exception  {
        login();
        recyclerViewDisplayed();
        addTask();
    }

    @Test
    public void B_editTaskTest() throws Exception {
        login();
        recyclerViewDisplayed();
        editTask();
    }

    @Test
    public void C_deleteTaskTest() throws Exception {
        login();
        recyclerViewDisplayed();
        deleteTask();
    }

    @Test
    public void D_fullTestSequence() throws Exception {
        login();
        recyclerViewDisplayed();
        addTask();
        editTask();
        deleteTask();
    }

    public void login() throws Exception {
        onView(withId(R.id.emailAddress)).perform(ViewActions.typeText(email));
        onView(withId(R.id.passWord)).perform(ViewActions.scrollTo(), ViewActions.typeText(password));
        onView(withId(R.id.loginButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(ContentMainActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.checklistImgView)).perform(ViewActions.click());
        intending(hasComponent(ToDoList.class.getName()));
    }

    public void recyclerViewDisplayed() {
        onView(withId(R.id.myTasks)).check(matches(isDisplayed()));
        onView(withId(R.id.taskListAnnouncements)).check(matches(isDisplayed()));
        onView(withId(R.id.milestone)).check(matches(isDisplayed()));
        onView(withId(R.id.createTask)).check(matches(isDisplayed()));
    }

    public void addTask() throws Exception {
        onView(withId(R.id.createTask)).perform(ViewActions.click());
        intending(hasComponent(AddTaskActivity.class.getName()));
        onView(withId(R.id.taskDescEditText)).perform(ViewActions.typeText(task));
        onView(withId(R.id.taskDescEditStatus)).perform(ProgressBarSetter.scrubSeekBarAction(status));
        onView(withId(R.id.taskTypeEditText)).perform(ViewActions.typeText(tag), ViewActions.closeSoftKeyboard());

        // Test saving without defining date and time
        onView(withId(R.id.saveButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(withId(R.id.AddTasks)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));

        onView(withId(R.id.dueDateButton)).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 1, 2));
        onView(withId(android.R.id.button1)).perform(ViewActions.click());

        // Test saving without defining date and time
        onView(withId(R.id.saveButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(withId(R.id.AddTasks)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));

        onView(withId(R.id.dueTimeButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(0, 0));
        onView(withId(android.R.id.button1)).perform(ViewActions.click());

        // Test saving without defining date and time
        onView(withId(R.id.saveButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(withId(R.id.AddTasks)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));

        onView(withId(R.id.plannedDateButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 1, 1));
        onView(withId(android.R.id.button1)).perform(ViewActions.click());

        // Test saving without defining date and time
        onView(withId(R.id.saveButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(withId(R.id.AddTasks)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));

        onView(withId(R.id.plannedTimeButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.saveButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        Thread.sleep(2000);
        onView(withText(task)).check(matches(isDisplayed()))
                .perform(ViewActions.pressBack());
        intending(hasComponent(ContentMainActivity.class.getName()));
        onView(withText(task)).check(matches(isDisplayed()));
        onView(withId(R.id.checklistImgView)).perform(ViewActions.click());
        intending(hasComponent(ToDoList.class.getName()));

    }

    public void editTask() throws Exception {
        onView(withId(R.id.taskListAnnouncements)).perform(RecyclerViewActions.scrollTo(
                hasDescendant(withText(task))));
        onView(withText(task)).perform(ViewActions.click());
        intending(hasComponent(EditTaskActivity.class.getName()));
        onView(withId(R.id.editTaskDesc)).perform(ViewActions.clearText(), ViewActions.typeText(newTask));
        onView(withId(R.id.editStatusDesc)).perform(ProgressBarSetter.scrubSeekBarAction(newStatus));
        onView(withId(R.id.editTag)).perform(ViewActions.clearText(), ViewActions.typeText(newTag), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editDueDateButton)).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 10, 15));
        onView(withId(android.R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.editDueTimeButton)).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(23, 59));
        onView(withId(android.R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.editPlannedDateButton)).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 10, 1));
        onView(withId(android.R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.editPlannedTimeButton)).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(20, 0));
        onView(withId(android.R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.editButton)).perform(ViewActions.click());
        onView(withId(R.id.taskListAnnouncements)).perform(RecyclerViewActions.scrollTo(
                hasDescendant(withText(newTask))));
        Thread.sleep(2000);
        onView(withText(newTask)).check(matches(isDisplayed()));
        onView(withId(R.id.taskListAnnouncements)).perform(ViewActions.pressBack());
        intending(hasComponent(ContentMainActivity.class.getName()));
        onView(withText(newTask)).check(matches(isDisplayed()));
        onView(withId(R.id.checklistImgView)).perform(ViewActions.click());
        intending(hasComponent(ToDoList.class.getName()));

    }

    public void deleteTask() {
        onView(withText(newTask)).perform(ViewActions.swipeLeft());
        onView(withId(R.id.taskListAnnouncements)).check(matches(not(hasItem(hasDescendant(withText(newTask))))));
        onView(withId(R.id.taskListAnnouncements)).perform(ViewActions.pressBack());
        intending(hasComponent(ContentMainActivity.class.getName()));
        onView(withId(R.id.mainTaskListAnnouncements)).check(matches(not(hasItem(hasDescendant(withText(newTask))))));

    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
