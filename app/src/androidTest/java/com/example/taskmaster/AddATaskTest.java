package com.example.taskmaster;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddATaskTest {


    @Rule
    public ActivityScenarioRule<AddATask> addActivityRule =
            new ActivityScenarioRule<>(AddATask.class);

    @Test
    public void testAddTaskHeader() {
        onView(withId(R.id.text_addTask)).check(matches(withText("Add Task")));

    }

    @Test
    public void testAddTaskText() {
        onView(withId(R.id.text_taskTitle)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.text_taskDescription)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.edit_myTask)).perform(typeText("Hello"), closeSoftKeyboard());
        onView(withId(R.id.edit_doSomething)).check(matches(withText("Do Something")));
    }

     @Test
    public void testSpinner() {
        onView(withId(R.id.spinner)).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText("new")));
    }

    @Test
    public void testRadioButtons() {
        onView(withId(R.id.radioGroup)).perform(click());
        onView(withId(R.id.radioButton_team1)).perform(click());
        onView(withId(R.id.radioButton_team1)).check(matches(withText("team1")));

        onView(withId(R.id.radioButton_team2)).perform(click());
        onView(withId(R.id.radioButton_team2)).check(matches(withText("team2")));

        onView(withId(R.id.radioButton_team3)).perform(click());
        onView(withId(R.id.radioButton_team3)).check(matches(withText("team3")));
    }
}