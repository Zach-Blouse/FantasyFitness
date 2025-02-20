package com.zblouse.fantasyfitness.user;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
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
    public void publishEventTest(){
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
}
