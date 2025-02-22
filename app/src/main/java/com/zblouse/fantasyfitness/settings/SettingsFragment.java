package com.zblouse.fantasyfitness.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;

public class SettingsFragment  extends AuthenticationRequiredFragment {

    public SettingsFragment(MainActivity mainActivity){
        super(R.layout.settings_fragment, mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.settings_fragment,container,false);
        mainActivity.showNavigation();
        Button logoutButton = layout.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.logout();
            }
        });

        return layout;
    }
}
