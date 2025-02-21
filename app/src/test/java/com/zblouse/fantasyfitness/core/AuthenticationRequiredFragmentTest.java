package com.zblouse.fantasyfitness.core;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.home.UserHomeFragment;
import com.zblouse.fantasyfitness.user.LoginFragment;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class AuthenticationRequiredFragmentTest {

    @Test
    public void notAuthenticatedTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(null);
        FragmentManager mockManager = Mockito.mock(FragmentManager.class);
        when(mockMainActivity.getSupportFragmentManager()).thenReturn(mockManager);
        FragmentTransaction mockTransaction = Mockito.mock(FragmentTransaction.class);
        when(mockManager.beginTransaction()).thenReturn(mockTransaction);
        when(mockTransaction.replace(eq(R.id.fragment_container),any())).thenReturn(mockTransaction);


        AuthenticationRequiredFragment testedFragment = new AuthenticationRequiredFragment(7, mockMainActivity);
        testedFragment.OnCreateView();
        ArgumentCaptor<LoginFragment> fragmentArgumentCaptor = ArgumentCaptor.forClass(LoginFragment.class);
        verify(mockTransaction).replace(eq(R.id.fragment_container), fragmentArgumentCaptor.capture());
        assertNotNull(fragmentArgumentCaptor.getValue());
    }
}
