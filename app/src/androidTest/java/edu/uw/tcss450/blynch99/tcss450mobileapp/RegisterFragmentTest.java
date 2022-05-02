package edu.uw.tcss450.blynch99.tcss450mobileapp;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.AndroidJUnitRunner;
import androidx.test.runner.lifecycle.ApplicationLifecycleCallback;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.register.EmailVerificationFragment;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.register.RegisterFragment;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.signin.ForgotPasswordFragment;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.signin.SignInFragment;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RegisterFragmentTest {

    @Test
    public void testNavigateToVerificationNoFirstName() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());

        Bundle args = new Bundle();
        args.putString("email", "default");
        args.putString("password", "default");

        FragmentScenario<RegisterFragment> registerScenario =
                FragmentScenario.launchInContainer(RegisterFragment.class, args);
        registerScenario.onFragment(fragment -> {
                    navController.setGraph(R.navigation.auth_graph);
                    Navigation.setViewNavController(fragment.requireView(), navController);
                });

        onView(withId(R.id.button_register)).perform(click());
        onView(withId(R.id.edit_first)).check(matches(hasErrorText("Please enter a first name.")));
    }

    @Test
    public void testNavigateToVerificationNoLastName() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());

        Bundle args = new Bundle();
        args.putString("email", "default");
        args.putString("password", "default");

        FragmentScenario<RegisterFragment> registerScenario =
                FragmentScenario.launchInContainer(RegisterFragment.class, args);
        registerScenario.onFragment(fragment -> {
            navController.setGraph(R.navigation.auth_graph);
            Navigation.setViewNavController(fragment.requireView(), navController);
        });

        onView(withId(R.id.edit_first)).perform(replaceText("Firstname"));
        onView(withId(R.id.button_register)).perform(click());
        onView(withId(R.id.edit_last)).check(matches(hasErrorText("Please enter a last name.")));
    }
}