package com.zblouse.fantasyfitness.home;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.ToastDeviceService;
import com.zblouse.fantasyfitness.settings.SettingsFragment;
import com.zblouse.fantasyfitness.user.CreateAccountFragment;
import com.zblouse.fantasyfitness.user.UserExistEvent;
import com.zblouse.fantasyfitness.user.UserGameStateService;
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

import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
public class UserHomeFragmentTest {

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
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);
    }

    @Test
    public void publishUserExistEventNotExistsTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        ToastDeviceService mockToastDeviceService = Mockito.mock(ToastDeviceService.class);
        when(mockMainActivity.getDeviceService(eq(DeviceServiceType.TOAST))).thenReturn(mockToastDeviceService);
        UserHomeFragment testedFragment = new UserHomeFragment(mockMainActivity);
        testedFragment.publishEvent(new UserExistEvent(false,new HashMap<>()));
        verify(mockMainActivity).logout();
        verify(mockToastDeviceService).sendToast(eq("Account does not exist"));
    }
}
