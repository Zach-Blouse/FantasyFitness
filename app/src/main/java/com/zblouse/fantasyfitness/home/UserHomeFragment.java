package com.zblouse.fantasyfitness.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.zblouse.fantasyfitness.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;

public class UserHomeFragment extends AuthenticationRequiredFragment {

    public UserHomeFragment(){
        super(R.layout.user_home_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.user_home_fragment,container,false);


        return layout;
    }
}
