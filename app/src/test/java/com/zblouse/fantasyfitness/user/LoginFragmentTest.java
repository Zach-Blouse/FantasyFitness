package com.zblouse.fantasyfitness.user;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.ToastDeviceService;
import com.zblouse.fantasyfitness.home.UserHomeFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class LoginFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void onCreateTest(){
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        LoginFragment testedLoginFragment = new LoginFragment(mainActivity);
        View returnedView = testedLoginFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView.findViewById(R.id.login_button));
        assertNotNull(returnedView.findViewById(R.id.create_account_button));
    }

    @Test
    public void publishUserExistEventExistsTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        LoginFragment testedLoginFragment = new LoginFragment(mockMainActivity);
        FragmentManager mockManager = Mockito.mock(FragmentManager.class);
        when(mockMainActivity.getSupportFragmentManager()).thenReturn(mockManager);
        FragmentTransaction mockTransaction = Mockito.mock(FragmentTransaction.class);
        when(mockManager.beginTransaction()).thenReturn(mockTransaction);
        when(mockTransaction.replace(eq(R.id.fragment_container),any())).thenReturn(mockTransaction);

        testedLoginFragment.publishEvent(new UserExistEvent(true,new HashMap<>()));
        ArgumentCaptor<UserHomeFragment> fragmentArgumentCaptor = ArgumentCaptor.forClass(UserHomeFragment.class);
        verify(mockTransaction).replace(eq(R.id.fragment_container), fragmentArgumentCaptor.capture());
        assertNotNull(fragmentArgumentCaptor.getValue());
    }

    @Test
    public void publishUserExistsEventNotExistsTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        LoginFragment testedFragment = new LoginFragment(mockMainActivity);
        ToastDeviceService mockToastDeviceService = Mockito.mock(ToastDeviceService.class);
        when(mockMainActivity.getDeviceService(eq(DeviceServiceType.TOAST))).thenReturn(mockToastDeviceService);
        FragmentManager mockManager = Mockito.mock(FragmentManager.class);
        when(mockMainActivity.getSupportFragmentManager()).thenReturn(mockManager);
        FragmentTransaction mockTransaction = Mockito.mock(FragmentTransaction.class);
        when(mockManager.beginTransaction()).thenReturn(mockTransaction);
        when(mockTransaction.replace(eq(R.id.fragment_container),any())).thenReturn(mockTransaction);

        testedFragment.publishEvent(new UserExistEvent(false,new HashMap<>()));
        ArgumentCaptor<CreateAccountFragment> fragmentArgumentCaptor = ArgumentCaptor.forClass(CreateAccountFragment.class);
        verify(mockTransaction).replace(eq(R.id.fragment_container), fragmentArgumentCaptor.capture());
        assertNotNull(fragmentArgumentCaptor.getValue());
        verify(mockToastDeviceService).sendToast(eq("You need to register your account"));
    }

    @Test
    public void onCreateUserNotNullTest(){
        String mockUserId = "mockUser1";
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        LoginFragment testedLoginFragment = new LoginFragment(mainActivity);
        testedLoginFragment.onCreateView(layoutInflater, null, mockBundle);
        shadowOf(Looper.getMainLooper()).idle();
        Fragment activeFragement = mainActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assert(activeFragement instanceof UserHomeFragment);
    }
}
