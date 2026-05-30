package com.example.firstapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.firstapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class AppNavigationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainScreen_isDisplayed() {
        onView(withText("The Game of Life"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickSingleplayer_opensSingleplayerScreen() {
        onView(withText("Singleplayer"))
                .perform(click());

        onView(withText("Singleplayer"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickMultiplayer_opensMultiplayerScreen() {
        onView(withText("Multiplayer"))
                .perform(click());

        onView(withText("Multiplayer"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void singleplayerScreen_hasStartButton() {
        onView(withText("Singleplayer"))
                .perform(click());

        onView(withText("Start"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void multiplayerScreen_showsLobbyPlaceholder() {
        onView(withText("Multiplayer"))
                .perform(click());

        onView(withText("Lobby"))
                .check(matches(isDisplayed()));
    }
}