package com.zblouse.fantasyfitness.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.zblouse.fantasyfitness.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;

public class CreateAccountFragment extends AuthenticationRequiredFragment {

    public CreateAccountFragment(){
        super(R.layout.create_account_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.create_account_fragment,container,false);

        EditText usernameEditText = layout.findViewById(R.id.username_edit_text);
        Button createAccountButton = layout.findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Username field is required",Toast.LENGTH_SHORT).show();
                } else {
                    String username = usernameEditText.getText().toString();
                    //TODO need to figure out how to handle events in general and load the User Home if this succeeds
                    ((MainActivity)getActivity()).getUserService().registerUser(((MainActivity)getActivity()).getCurrentUser().getUid(),username);
                }
            }
        });

        //TODO probably going to get rid of this button and replace it with an auto check to see if the user already has an account in the database, and if so load it
        Button cancelButton = layout.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment()).commit();
            }
        });
        return layout;
    }
}
