package com.example.taskmaster;


import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.allOf;


import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EspressoAppTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void assertTextChanged() {
        // type text and then press change text button
        onView(withId(R.id.edit_myTask)).perform(typeText("Hello"), closeSoftKeyboard());
        onView(withId(R.id.buttonMain_addTask)).perform(click());
        onView(withId(R.id.buttonMain_allTask)).perform(click());


        // check that the text was changed when the button was clicked
        onView(withId(R.id.edit_myTask)).check(matches(withText("Hello")));
    }

    @Test
    public void goFromAddToMainActivity() {
        onView(withId(R.id.edit_myTask)).perform(typeText("Good Bye"), closeSoftKeyboard());
        onView(withId(R.id.button_addTask)).perform(click());

        // assert the data went over correctly and was displayed
        onView(withId(R.id.text_myTasks)).check(matches(withText("Good Bye")));
    }
}