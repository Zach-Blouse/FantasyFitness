package com.zblouse.fantasyfitness.user;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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

import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
public class CreateAccountFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void onCreateTest(){
        String mockUserId = "mockUser1";
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockUser.getUid()).thenReturn(mockUserId);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CreateAccountFragment testedFragment = new CreateAccountFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView.findViewById(R.id.username_edit_text));
        assertNotNull(returnedView.findViewById(R.id.create_account_button));
        assertNotNull(returnedView.findViewById(R.id.cancel_button));
    }

    @Test
    public void cancelTest(){
        String mockUserId = "mockUser1";
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockUser.getUid()).thenReturn(mockUserId);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CreateAccountFragment testedFragment = new CreateAccountFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        returnedView.findViewById(R.id.cancel_button).performClick();

        verify(mockAuth).signOut();
    }

    @Test
    public void createAccountEmptyUsernameTest(){
        String mockUserId = "mockUser1";
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockUser.getUid()).thenReturn(mockUserId);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        ToastDeviceService mockToastDeviceService = Mockito.mock(ToastDeviceService.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setDeviceService(DeviceServiceType.TOAST, mockToastDeviceService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CreateAccountFragment testedFragment = new CreateAccountFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        returnedView.findViewById(R.id.create_account_button).performClick();
        verify(mockToastDeviceService).sendToast(eq("Username field is required"));
    }

    @Test
    public void createAccountFilledUsernameTest(){
        String mockUserId = "mockUser1";
        String testUsername = "testUser1";
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockUser.getUid()).thenReturn(mockUserId);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        ToastDeviceService mockToastDeviceService = Mockito.mock(ToastDeviceService.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setDeviceService(DeviceServiceType.TOAST, mockToastDeviceService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CreateAccountFragment testedFragment = new CreateAccountFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        ((EditText)returnedView.findViewById(R.id.username_edit_text)).setText(testUsername);
        returnedView.findViewById(R.id.create_account_button).performClick();
        verify(mockUserService).registerUser(eq(mockUserId), eq(testUsername));
    }

    @Test
    public void publishUserExistEventExistsTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        CreateAccountFragment testedFragment = new CreateAccountFragment(mockMainActivity);
        ToastDeviceService mockToastDeviceService = Mockito.mock(ToastDeviceService.class);
        when(mockMainActivity.getDeviceService(eq(DeviceServiceType.TOAST))).thenReturn(mockToastDeviceService);
        FragmentManager mockManager = Mockito.mock(FragmentManager.class);
        when(mockMainActivity.getSupportFragmentManager()).thenReturn(mockManager);
        FragmentTransaction mockTransaction = Mockito.mock(FragmentTransaction.class);
        when(mockManager.beginTransaction()).thenReturn(mockTransaction);
        when(mockTransaction.replace(eq(R.id.fragment_container),any())).thenReturn(mockTransaction);

        testedFragment.publishEvent(new UserExistEvent(true,new HashMap<>()));
        ArgumentCaptor<UserHomeFragment> fragmentArgumentCaptor = ArgumentCaptor.forClass(UserHomeFragment.class);
        verify(mockTransaction).replace(eq(R.id.fragment_container), fragmentArgumentCaptor.capture());
        assertNotNull(fragmentArgumentCaptor.getValue());
        verify(mockToastDeviceService).sendToast(eq("Account already exists"));
    }

    @Test
    public void publishUserExistEventNotExistsTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        CreateAccountFragment testedFragment = new CreateAccountFragment(mockMainActivity);
        testedFragment.publishEvent(new UserExistEvent(false,new HashMap<>()));
        verify(mockMainActivity, times(0)).getSupportFragmentManager();
    }

    @Test
    public void publishCreateAccountResponseEventCreatedTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        CreateAccountFragment testedFragment = new CreateAccountFragment(mockMainActivity);
        ToastDeviceService mockToastDeviceService = Mockito.mock(ToastDeviceService.class);
        when(mockMainActivity.getDeviceService(eq(DeviceServiceType.TOAST))).thenReturn(mockToastDeviceService);
        FragmentManager mockManager = Mockito.mock(FragmentManager.class);
        when(mockMainActivity.getSupportFragmentManager()).thenReturn(mockManager);
        FragmentTransaction mockTransaction = Mockito.mock(FragmentTransaction.class);
        when(mockManager.beginTransaction()).thenReturn(mockTransaction);
        when(mockTransaction.replace(eq(R.id.fragment_container),any())).thenReturn(mockTransaction);

        testedFragment.publishEvent(new CreateAccountResponseEvent(true,new HashMap<>()));
        ArgumentCaptor<UserHomeFragment> fragmentArgumentCaptor = ArgumentCaptor.forClass(UserHomeFragment.class);
        verify(mockTransaction).replace(eq(R.id.fragment_container), fragmentArgumentCaptor.capture());
        assertNotNull(fragmentArgumentCaptor.getValue());
        verify(mockToastDeviceService).sendToast(eq("Account Created"));
    }

    @Test
    public void publishCreateAccountResponseEventNotCreatedTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        ToastDeviceService mockToastDeviceService = Mockito.mock(ToastDeviceService.class);
        when(mockMainActivity.getDeviceService(eq(DeviceServiceType.TOAST))).thenReturn(mockToastDeviceService);
        CreateAccountFragment testedFragment = new CreateAccountFragment(mockMainActivity);
        testedFragment.publishEvent(new CreateAccountResponseEvent(false,new HashMap<>()));
        verify(mockMainActivity, times(0)).getSupportFragmentManager();
        verify(mockToastDeviceService).sendToast(eq("Failed to create account"));
    }


}
