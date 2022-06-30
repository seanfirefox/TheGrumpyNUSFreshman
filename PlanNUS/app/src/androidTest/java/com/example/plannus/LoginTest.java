package com.example.plannus;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.ComponentName;
import android.content.Context;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import com.example.plannus.Activities.ContentMainActivity;
import com.example.plannus.Activities.MainActivity;
import com.example.plannus.Activities.RegisterUser;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private String email = "admin2@gmail.com";
    private String password = "1234567";
    private View decorView;

    @Before
    public void setUp() throws Exception{
        Intents.init();
        activityRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
    }

    @Test
    public void checkLoginPageDisplayed() {
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.emailAddress)).check(matches(isDisplayed()));
        onView(withId(R.id.passWord)).check(matches(isDisplayed()));
        onView(withId(R.id.register)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView2)).check(matches(isDisplayed()));
        onView(withId(R.id.emailIcon)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRegisterButton() {
        onView(withId(R.id.register)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(RegisterUser.class.getName()));
    }

//    @Test
//    public void failedLoginCheck() {
//        String expectedWarning = "Invalid Credentials";
//        onView(withId(R.id.loginButton)).perform(ViewActions.scrollTo(), ViewActions.click());
//        onView(withText(expectedWarning))
//                .inRoot(withDecorView(not(decorView)))
//                .check(matches(isDisplayed()));
//    }

    @Test
    public void successfulLoginCheck() {
        onView(withId(R.id.emailAddress)).perform(ViewActions.typeText(email));
        onView(withId(R.id.passWord)).perform(ViewActions.typeText(password));
        onView(withId(R.id.loginButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(ContentMainActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception{
        Intents.release();
    }
}