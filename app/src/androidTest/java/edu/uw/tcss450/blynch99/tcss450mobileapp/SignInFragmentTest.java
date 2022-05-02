package edu.uw.tcss450.blynch99.tcss450mobileapp;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.AndroidJUnitRunner;
import androidx.test.runner.lifecycle.ApplicationLifecycleCallback;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.signin.ForgotPasswordFragment;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.signin.SignInFragment;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SignInFragmentTest {
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("edu.uw.tcss450.blynch99.tcss450mobileapp", appContext.getPackageName());
//    }

    @Test
    public void testNavigateToRegister() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());

        Bundle args = new Bundle();
        args.putString("email", "default");
        args.putString("password", "default");

        FragmentScenario<SignInFragment> signInScenario =
                FragmentScenario.launchInContainer(SignInFragment.class, args);
        signInScenario.onFragment(fragment -> {
                    navController.setGraph(R.navigation.auth_graph);
                    Navigation.setViewNavController(fragment.requireView(), navController);
                });

        onView(withId(R.id.button_to_register)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.registerFragment);
    }

    @Test
    public void testNavigateToForgotPassword() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());

        Bundle args = new Bundle();
        args.putString("email", "default");

        FragmentScenario<SignInFragment> signInScenario =
                FragmentScenario.launchInContainer(SignInFragment.class, args);
        signInScenario.onFragment(fragment -> {
                    navController.setGraph(R.navigation.auth_graph);
                    Navigation.setViewNavController(fragment.requireView(), navController);
                });

        onView(withId(R.id.button_for_forgot_password)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.forgotPasswordFragment);
    }
}