package com.zblouse.fantasyfitness.core;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.user.LoginFragment;

public class AuthenticationRequiredFragment extends Fragment {

    protected MainActivity mainActivity;

    protected AuthenticationRequiredFragment(int id, MainActivity mainActivity){
        super(id);
        this.mainActivity = mainActivity;
    }

    protected void OnCreateView(){
        FirebaseUser currentUser = ((MainActivity)getActivity()).getCurrentUser();
        if(currentUser == null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment(mainActivity)).commit();
        }
    }
}
