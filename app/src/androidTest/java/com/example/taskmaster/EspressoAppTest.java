package com.example.taskmaster;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;



import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;

import androidx.test.espresso.contrib.RecyclerViewActions;


import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


@RunWith(AndroidJUnit4.class)
public class EspressoAppTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    
    @Test
    public void testAppHeader() {
        onView(withText("taskmaster")).check(matches(isDisplayed()));
    }

    @Test
    public void testMainHeader() {
        onView(withId(R.id.text_myTasks)).check(matches(withText("My Tasks")));
    }

    @Test
    public void testMainActivityButtons() {
        onView(withId(R.id.buttonMain_addTask)).perform(click());
        onView(withId(R.id.buttonMain_allTask)).perform(click());
        onView(withId(R.id.buttonMain_settings)).perform(click());
    }

    @Test(expected = PerformException.class)
    public void itemWithText_doesNotExist() {
        // Attempt to scroll to an item that contains the special text.
        onView(ViewMatchers.withId(R.id.recyclerView_task))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText("not in the list"))
                ));


        //Recycler View
        onView(withId(R.id.recyclerView_task)).perform(click());
    }

     @Test
    public void testUserName() {
        onView(withId(R.id.buttonMain_settings)).perform(click());
        onView(withId(R.id.edit_username)).perform(typeText("Anas"));
        onView(withId(R.id.saveButton)).perform(click());
        //onView(withId(R.id.textMain_username)).check(matches(withText("Anas")));
        onView(withId(R.id.textMain_teamName)).check(matches(withText("Anas's Team")));


    }
    
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