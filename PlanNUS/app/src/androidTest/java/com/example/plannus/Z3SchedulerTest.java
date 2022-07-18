package com.example.plannus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.plannus.Activities.LoginRegister.ContentMainActivity;
import com.example.plannus.Activities.LoginRegister.MainActivity;
import com.example.plannus.Activities.TimetableGenerator.GenerateTimetableActivity;
import com.example.plannus.Activities.TimetableGenerator.TimetableSettingsActivity;

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
public class Z3SchedulerTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private String fullName = "DEADBEEF";
    private String age = "12";
    private String email = "deadbeef@gmail.com";
    private String password = "deadbeef1234";

    @Before
    public void setUp() throws Exception{
        Intents.init();
        // Login First
        Thread.sleep(1000);
        try {
            onView(withId(R.id.logoutButton)).check(matches(isDisplayed()))
                    .perform(ViewActions.click());
            Thread.sleep(2000);
        } catch (NoMatchingViewException e) {
            System.out.println("NO MATCHING VIEW EXCEPTION");
        } catch (InterruptedException e) {
            System.out.println("THREAD INTERRUPTED");
        }
        onView(withId(R.id.emailAddress)).perform(ViewActions.typeText(email));
        onView(withId(R.id.passWord)).perform(ViewActions.scrollTo(), ViewActions.typeText(password));
        onView(withId(R.id.loginButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(ContentMainActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.logoutButton)).check(matches(isDisplayed()));
        onView(withId(R.id.hiName)).check(matches(withText("Hi " + fullName + " !")));

        // Go to Generator Activity
        onView(withId(R.id.hiName)).check(matches(withText("Hi " + fullName + " !")));
        onView(withId(R.id.generateTimetableButton)).perform(ViewActions.click());
        intending(hasComponent(GenerateTimetableActivity.class.getName()));
        Thread.sleep(1000);

        onView(withId(R.id.nothingCard)).check(matches(withText("No Timetable Generated Yet")));

        // Go to Settings Activity
        onView(withId(R.id.settingsButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(TimetableSettingsActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.settingsPage)).check(matches(isDisplayed()));

    }

    @Test
    public void A_CanAccessActivity() throws Exception {
        onView(withId(R.id.settingsPage)).check(matches(isDisplayed()));
    }

    @Test
    public void B_EmptySettingsTimetable() throws Exception {
        // Go into settings and zero out the settings
        Thread.sleep(1000);
        onView(withId(R.id.saveTimetableSettingsButton)).perform(ViewActions.closeSoftKeyboard(), ViewActions.click());

        // Click on generate and it fails
        intended(hasComponent(GenerateTimetableActivity.class.getName()));
        onView(withId(R.id.generateButton)).check(matches(isDisplayed()));
        onView(withId(R.id.generateButton)).perform(ViewActions.click());
        Thread.sleep(1000);
        onView(withId(R.id.nothingCard)).check(matches(withText("Settings page empty")));
    }


    @Test
    public void C_Generate_SAT_5Modules_TimetableTest() throws Exception {

        onView(withId(R.id.aySpinner)).perform(ViewActions.click());
        onView(withText("2021-2022")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.aySpinner)).check(matches(withText(containsString("2021-2022"))));

        onView(withId(R.id.semesterSpinner)).perform(ViewActions.click());
        onView(withText("1")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.semesterSpinner)).check(matches(withText(containsString("1"))));

        onView(withId(R.id.moduleCode1)).perform(ViewActions.typeText("CS1101S"))
                .check(matches(withText("CS1101S")));
        onView(withId(R.id.moduleCode2)).perform(ViewActions.typeText("CS1231S"))
                .check(matches(withText("CS1231S")));
        onView(withId(R.id.moduleCode3)).perform(ViewActions.typeText("MA2001"))
                .check(matches(withText("MA2001")));
        onView(withId(R.id.moduleCode4)).perform(ViewActions.typeText("MA1521"))
                .check(matches(withText("MA1521")));
        onView(withId(R.id.moduleCode5)).perform(ViewActions.typeText("GEC1030"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("GEC1030")));

        ViewActions.closeSoftKeyboard();

        onView(withId(R.id.saveTimetableSettingsButton)).perform(ViewActions.click());
        intending(hasComponent(GenerateTimetableActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.nothingCard)).check(matches(withText("No Timetable Generated Yet")));

        onView(withId(R.id.generateButton)).perform(ViewActions.click());
        Thread.sleep(20000);
        onView(withId(R.id.mondayClass)).check(matches(withSubstring("MONDAY")));
    }

    @Test
    public void D_Generate_SAT_5Modules_TimetableTestWithConstraints() throws Exception {

        onView(withId(R.id.aySpinner)).perform(ViewActions.click());
        onView(withText("2021-2022")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.aySpinner)).check(matches(withText(containsString("2021-2022"))));

        onView(withId(R.id.semesterSpinner)).perform(ViewActions.click());
        onView(withText("1")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.semesterSpinner)).check(matches(withText(containsString("1"))));

        onView(withId(R.id.moduleCode1)).perform(ViewActions.typeText("CS1101S"))
                .check(matches(withText("CS1101S")));
        onView(withId(R.id.moduleCode2)).perform(ViewActions.typeText("CS1231S"))
                .check(matches(withText("CS1231S")));
        onView(withId(R.id.moduleCode3)).perform(ViewActions.typeText("MA2001"))
                .check(matches(withText("MA2001")));
        onView(withId(R.id.moduleCode4)).perform(ViewActions.typeText("MA1521"))
                .check(matches(withText("MA1521")));
        onView(withId(R.id.moduleCode5)).perform(ViewActions.typeText("GEC1030"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("GEC1030")));

        onView(withId(R.id.oneFreeDay)).check(matches(isNotChecked()))
                .perform(ViewActions.click())
                .check(matches(isChecked()));

        onView(withId(R.id.no8amLessons)).check(matches(isNotChecked()))
                .perform(ViewActions.click())
                .check(matches(isChecked()))
                .perform(ViewActions.click())
                .check(matches(isNotChecked()));

        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.saveTimetableSettingsButton)).perform(ViewActions.click());
        intending(hasComponent(GenerateTimetableActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.nothingCard)).check(matches(withText("No Timetable Generated Yet")));

        onView(withId(R.id.generateButton)).perform(ViewActions.click());
        Thread.sleep(20000);
        onView(withId(R.id.mondayClass)).check(matches(withSubstring("MONDAY")));
        onView(withId(R.id.tuesdayClass)).check(matches(withSubstring("NO CLASS TODAY")));
    }

    @Test
    public void E_Generate_SAT_6Modules_TimetableTest() throws Exception {

        onView(withId(R.id.aySpinner)).perform(ViewActions.click());
        onView(withText("2021-2022")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.aySpinner)).check(matches(withText(containsString("2021-2022"))));

        onView(withId(R.id.semesterSpinner)).perform(ViewActions.click());
        onView(withText("2")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.semesterSpinner)).check(matches(withText(containsString("2"))));

        onView(withId(R.id.moduleCode1)).perform(ViewActions.typeText("CS1101S"))
                .check(matches(withText("CS1101S")));
        onView(withId(R.id.moduleCode2)).perform(ViewActions.typeText("CS1231S"))
                .check(matches(withText("CS1231S")));
        onView(withId(R.id.moduleCode3)).perform(ViewActions.typeText("MA2001"))
                .check(matches(withText("MA2001")));
        onView(withId(R.id.moduleCode4)).perform(ViewActions.typeText("MA1521"))
                .check(matches(withText("MA1521")));
        onView(withId(R.id.moduleCode5)).perform(ViewActions.typeText("GEC1030"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("GEC1030")));
        onView(withId(R.id.addRow)).perform(ViewActions.click());
        Thread.sleep(1000);
        onView(withTagValue(is("moduleCode6")))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("GEA1000"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("GEA1000")));
        Thread.sleep(1000);
        onView(withId(R.id.saveTimetableSettingsButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(GenerateTimetableActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.nothingCard)).check(matches(withText("No Timetable Generated Yet")));
        onView(withId(R.id.generateButton)).perform(ViewActions.click());
        Thread.sleep(7000);
        onView(withId(R.id.mondayClass)).check(matches(withSubstring("MONDAY")));
    }

    @Test
    public void F_Generate_SAT_7Modules_TimetableTest() throws Exception {

        onView(withId(R.id.aySpinner)).perform(ViewActions.click());
        onView(withText("2021-2022")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.aySpinner)).check(matches(withText(containsString("2021-2022"))));

        onView(withId(R.id.semesterSpinner)).perform(ViewActions.click());
        onView(withText("2")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.semesterSpinner)).check(matches(withText(containsString("2"))));

        onView(withId(R.id.moduleCode1)).perform(ViewActions.typeText("CS2030S"))
                .check(matches(withText("CS2030S")));
        onView(withId(R.id.moduleCode2)).perform(ViewActions.typeText("CS2040S"))
                .check(matches(withText("CS2040S")));
        onView(withId(R.id.moduleCode3)).perform(ViewActions.typeText("SP1541"))
                .check(matches(withText("SP1541")));
        onView(withId(R.id.moduleCode4)).perform(ViewActions.typeText("IS1103"))
                .check(matches(withText("IS1103")));
        onView(withId(R.id.moduleCode5)).perform(ViewActions.typeText("HSS1000"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("HSS1000")));
        onView(withId(R.id.addRow)).perform(ViewActions.click());
        onView(withId(R.id.addRow)).perform(ViewActions.click());
        Thread.sleep(1000);
        onView(withTagValue(is("moduleCode6")))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("ST2131"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("ST2131")));
        Thread.sleep(1000);
        Thread.sleep(1000);
        onView(withTagValue(is("moduleCode7")))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("MA2104"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("MA2104")));
        Thread.sleep(1000);

        onView(withId(R.id.saveTimetableSettingsButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(GenerateTimetableActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.nothingCard)).check(matches(withText("No Timetable Generated Yet")));

        onView(withId(R.id.generateButton)).perform(ViewActions.click());
        Thread.sleep(7000);
        onView(withId(R.id.mondayClass)).check(matches(withSubstring("MONDAY")));
        onView(withId(R.id.saturdayClass)).perform(ViewActions.scrollTo())
                .check(matches(withSubstring("SATURDAY")));
    }

    @Test
    public void G_UNSAT_TimetableTest() throws Exception {

        onView(withId(R.id.aySpinner)).perform(ViewActions.click());
        onView(withText("2021-2022")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.aySpinner)).check(matches(withText(containsString("2021-2022"))));

        onView(withId(R.id.semesterSpinner)).perform(ViewActions.click());
        onView(withText("1")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.semesterSpinner)).check(matches(withText(containsString("1"))));

        onView(withId(R.id.moduleCode1)).perform(ViewActions.typeText("CS1101S"))
                .check(matches(withText("CS1101S")));
        onView(withId(R.id.moduleCode2)).perform(ViewActions.typeText("CS1231S"))
                .check(matches(withText("CS1231S")));
        onView(withId(R.id.moduleCode3)).perform(ViewActions.typeText("MA2001"))
                .check(matches(withText("MA2001")));
        onView(withId(R.id.moduleCode4)).perform(ViewActions.typeText("MA1521"))
                .check(matches(withText("MA1521")));
        onView(withId(R.id.moduleCode5)).perform(ViewActions.typeText("GEC1030"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("GEC1030")));

        onView(withId(R.id.no8amLessons)).check(matches(isNotChecked()))
                .perform(ViewActions.click())
                .check(matches(isChecked()));

        onView(withId(R.id.oneFreeDay)).check(matches(isNotChecked()))
                .perform(ViewActions.click())
                .check(matches(isChecked()))
                .perform(ViewActions.click())
                .check(matches(isNotChecked()));

        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.saveTimetableSettingsButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(GenerateTimetableActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.nothingCard)).check(matches(withText("No Timetable Generated Yet")));

        onView(withId(R.id.generateButton)).perform(ViewActions.click());
        Thread.sleep(7000);
        onView(withId(R.id.nothingCard)).check(matches(withSubstring("No Feasible Timetable!")));
    }

    @Test
    public void H_AnotherTimetable_5ModuleTest() throws Exception {

        onView(withId(R.id.aySpinner)).perform(ViewActions.click());
        onView(withText("2021-2022")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.aySpinner)).check(matches(withText(containsString("2021-2022"))));

        onView(withId(R.id.semesterSpinner)).perform(ViewActions.click());
        onView(withText("1")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.semesterSpinner)).check(matches(withText(containsString("1"))));

        onView(withId(R.id.moduleCode1)).perform(ViewActions.typeText("CS1101S"))
                .check(matches(withText("CS1101S")));
        onView(withId(R.id.moduleCode2)).perform(ViewActions.typeText("CS1231S"))
                .check(matches(withText("CS1231S")));
        onView(withId(R.id.moduleCode3)).perform(ViewActions.typeText("MA2001"))
                .check(matches(withText("MA2001")));
        onView(withId(R.id.moduleCode4)).perform(ViewActions.typeText("MA1521"))
                .check(matches(withText("MA1521")));
        onView(withId(R.id.moduleCode5)).perform(ViewActions.typeText("GEC1030"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("GEC1030")));

        ViewActions.closeSoftKeyboard();

        onView(withId(R.id.saveTimetableSettingsButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(GenerateTimetableActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.nothingCard)).check(matches(withText("No Timetable Generated Yet")));

        onView(withId(R.id.generateButton)).perform(ViewActions.click());
        Thread.sleep(7000);
        onView(withId(R.id.mondayClass)).check(matches(withSubstring("MONDAY")));
        onView(withId(R.id.mondayClass)).check(matches(withSubstring("MA2001 TUT 2")));

        onView(withId(R.id.nextButton)).perform(ViewActions.click());
        Thread.sleep(7000);
        onView(withId(R.id.mondayClass)).check(matches(withSubstring("MONDAY")));
        onView(withId(R.id.mondayClass)).check(matches(withSubstring("MA2001 TUT 1")));
    }

    @Test
    public void I_Already_UNSAT_AnotherTimetable_FAIL_CHECK() throws Exception {

        onView(withId(R.id.aySpinner)).perform(ViewActions.click());
        onView(withText("2021-2022")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.aySpinner)).check(matches(withText(containsString("2021-2022"))));

        onView(withId(R.id.semesterSpinner)).perform(ViewActions.click());
        onView(withText("1")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        onView(withId(R.id.semesterSpinner)).check(matches(withText(containsString("1"))));

        onView(withId(R.id.moduleCode1)).perform(ViewActions.typeText("CS1101S"))
                .check(matches(withText("CS1101S")));
        onView(withId(R.id.moduleCode2)).perform(ViewActions.typeText("CS1231S"))
                .check(matches(withText("CS1231S")));
        onView(withId(R.id.moduleCode3)).perform(ViewActions.typeText("MA2001"))
                .check(matches(withText("MA2001")));
        onView(withId(R.id.moduleCode4)).perform(ViewActions.typeText("MA1521"))
                .check(matches(withText("MA1521")));
        onView(withId(R.id.moduleCode5)).perform(ViewActions.typeText("GEC1030"), ViewActions.closeSoftKeyboard())
                .check(matches(withText("GEC1030")));

        onView(withId(R.id.no8amLessons)).check(matches(isNotChecked()))
                .perform(ViewActions.click())
                .check(matches(isChecked()));

        onView(withId(R.id.oneFreeDay)).check(matches(isNotChecked()))
                .perform(ViewActions.click())
                .check(matches(isChecked()))
                .perform(ViewActions.click())
                .check(matches(isNotChecked()));

        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.saveTimetableSettingsButton)).perform(ViewActions.scrollTo(), ViewActions.click());
        intending(hasComponent(GenerateTimetableActivity.class.getName()));
        Thread.sleep(1000);
        onView(withId(R.id.nothingCard)).check(matches(withText("No Timetable Generated Yet")));

        onView(withId(R.id.generateButton)).perform(ViewActions.click());
        Thread.sleep(7000);
        onView(withId(R.id.nothingCard)).check(matches(withSubstring("No Feasible Timetable!")));

        onView(withId(R.id.nextButton)).perform(ViewActions.click());
        Thread.sleep(7000);
        onView(withId(R.id.nothingCard)).check(matches(withSubstring("Invalid Combination!")));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}