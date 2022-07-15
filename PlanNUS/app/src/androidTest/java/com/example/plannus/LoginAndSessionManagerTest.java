package com.example.plannus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.plannus.Activities.LoginRegister.ContentMainActivity;
import com.example.plannus.Activities.LoginRegister.MainActivity;
import com.example.plannus.Activities.LoginRegister.RegisterUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginAndSessionManagerTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private String email = "admin2@gmail.com";
    private String password = "1234567";

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void checkLoginPageDisplayed() {
        onView(withId(R.id.emailAddress)).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.emailAddress)).check(matches(isDisplayed()));
        onView(withId(R.id.passWord)).check(matches(isDisplayed()));
        onView(withId(R.id.register)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRegisterButton() {
        onView(withId(R.id.register)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(RegisterUser.class.getName()));
    }

    @Test
    public void failedLoginCheck() throws Exception {
        Thread.sleep(1000);
        String expectedWarning = "Invalid Credentials";
        onView(withId(R.id.loginButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        Thread.sleep(1000);
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void successfulLoginAndSessionManagerSingletonStatusCheck() {
        onView(withId(R.id.emailAddress)).perform(ViewActions.typeText(email));
        onView(withId(R.id.passWord)).perform(ViewActions.scrollTo(), ViewActions.typeText(password));
        onView(withId(R.id.loginButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(ContentMainActivity.class.getName()));
        SessionManager a = SessionManager.get();
        SessionManager b = SessionManager.get();
        assertEquals(a, b);
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}