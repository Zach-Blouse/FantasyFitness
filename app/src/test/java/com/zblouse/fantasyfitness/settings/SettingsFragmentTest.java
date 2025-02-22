package com.zblouse.fantasyfitness.settings;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.user.CreateAccountFragment;
import com.zblouse.fantasyfitness.user.UserService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SettingsFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void onCreateTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        SettingsFragment testedFragment = new SettingsFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView.findViewById(R.id.logout_button));
    }

    @Test
    public void logoutTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        SettingsFragment testedFragment = new SettingsFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        returnedView.findViewById(R.id.logout_button).performClick();

        verify(mockAuth).signOut();
    }
}