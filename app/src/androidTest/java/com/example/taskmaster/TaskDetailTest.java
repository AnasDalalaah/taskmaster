package com.example.taskmaster;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;


@RunWith(AndroidJUnit4.class)
public class TaskDetailTest {

    @Rule
    public ActivityScenarioRule<TaskDetail> detailActivityRule =
            new ActivityScenarioRule<>(TaskDetail.class);

    @Test
    public void testDetailActivity() {
        onView(withId(R.id.text_detail)).check(matches(withText("Task Details")));
        onView(withId(R.id.task_title)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.text_detailDescription)).perform(click()).check(matches(isDisplayed()));
    }

}
