package com.zblouse.fantasyfitness.user;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.home.UserHomeFragment;

import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment {

    public LoginFragment(){
        super(R.layout.login_fragment);
    }

    //ActivityResultLauncher that is used when an existing user is logging in
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new UserHomeFragment()).commit();
                    } else {
                        Toast.makeText(getActivity(),"Failed to Authenticate",Toast.LENGTH_SHORT);
                    }
                }
            }
    );

    //ActivityResultLauncher that is used when the user is creating an account
    private final ActivityResultLauncher<Intent> createAccountLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new CreateAccountFragment()).commit();
                    } else {
                        Toast.makeText(getActivity(),"Failed to Authenticate",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.login_fragment,container,false);

        Button loginButton = layout.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFirebaseAuthentication(false);
            }
        });
        Button createAccountButton = layout.findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFirebaseAuthentication(true);
            }
        });

        return layout;
    }

    private void launchFirebaseAuthentication(boolean newAccount){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        if(newAccount){
            createAccountLauncher.launch(signInIntent);
        } else {
            signInLauncher.launch(signInIntent);
        }
    }

}
