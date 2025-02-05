package com.zblouse.fantasyfitness.user;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.MainActivity;
import com.zblouse.fantasyfitness.R;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class CreateAccountTest {

    @BeforeClass
    public static void setupFirebase() {
        String localHost = "10.0.2.2";

        FirebaseAuth.getInstance().useEmulator(localHost, 9099);
        FirebaseFirestore.getInstance().useEmulator(localHost, 8080);
    }

    @After
    public void tearDownFirebase() {
        FirebaseAuth.getInstance().signOut();
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void createAccountTest(){
        onView(withId(R.id.app_title)).check(matches(withText("Fantasy Fitness")));
        onView(withId(R.id.create_account_button)).perform(click());
        onView(withId(R.id.create_account_title)).check(matches(withText("Create Account")));
    }


}
