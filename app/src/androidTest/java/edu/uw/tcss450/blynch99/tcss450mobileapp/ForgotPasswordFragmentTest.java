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
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.AndroidJUnitRunner;
import androidx.test.runner.lifecycle.ApplicationLifecycleCallback;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.signin.ForgotPasswordFragment;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.signin.SignInFragment;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ForgotPasswordFragmentTest {

    @Test
    public void testNavigateToSignInFromForgotPassword() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());

        Bundle args = new Bundle();
        args.putString("email", "default");
        args.putString("password", "default");

        FragmentScenario<ForgotPasswordFragment> forgotPasswordScenario =
                FragmentScenario.launchInContainer(ForgotPasswordFragment.class, args);
        forgotPasswordScenario.onFragment(fragment -> {
                    navController.setGraph(R.navigation.auth_graph);
                    Navigation.setViewNavController(fragment.requireView(), navController);
                });

        onView(withId(R.id.button_back_to_signin)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.forgotPasswordFragment); // haven't moved
    }
}