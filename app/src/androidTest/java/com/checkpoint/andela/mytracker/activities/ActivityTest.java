package com.checkpoint.andela.mytracker.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.checkpoint.andela.mytracker.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ActivityTest {

    @Rule
    public ActivityTestRule<Home> mActivityRule = new ActivityTestRule(Home.class);

    @Test
    public void checksForView() {
        onView(withId(R.id.home_view));
    }

    @Test
    public void initialTimerState() {
        onView(withText("MaryJane")).check((doesNotExist()));
        onView(withText("Start Tracking")).check(matches(isDisplayed()));
    }
}