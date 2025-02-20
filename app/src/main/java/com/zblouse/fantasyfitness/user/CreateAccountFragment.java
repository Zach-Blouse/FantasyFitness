package com.zblouse.fantasyfitness.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;
import com.zblouse.fantasyfitness.home.UserHomeFragment;

public class CreateAccountFragment extends AuthenticationRequiredFragment implements EventListener {

    public CreateAccountFragment(MainActivity mainActivity){
        super(R.layout.create_account_fragment, mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        //Check if the user already has an account
        mainActivity.getUserService().userExistCheck(mainActivity.getCurrentUser().getUid());
        mainActivity.hideNavigation();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.create_account_fragment,container,false);

        EditText usernameEditText = layout.findViewById(R.id.username_edit_text);
        Button createAccountButton = layout.findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameEditText.getText().toString().isEmpty()){
                    Toast.makeText(mainActivity,"Username field is required",Toast.LENGTH_SHORT).show();
                } else {
                    String username = usernameEditText.getText().toString();
                    mainActivity.getUserService().registerUser(((MainActivity)getActivity()).getCurrentUser().getUid(),username);
                }
            }
        });

        Button cancelButton = layout.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).logout();
            }
        });
        return layout;
    }

    @Override
    public void publishEvent(Event event) {
        if(event.getEventType().equals(EventType.CREATE_ACCOUNT_RESPONSE)){
            if(((CreateAccountResponseEvent)event).isAccountCreated()){
                Toast.makeText(getActivity(),"Account Created",Toast.LENGTH_SHORT).show();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new UserHomeFragment(mainActivity)).commit();
            } else {
                Toast.makeText(getActivity(),"Failed to create account",Toast.LENGTH_SHORT).show();
            }
        } else if(event.getEventType().equals(EventType.USER_EXIST_EVENT)){
            if(((UserExistEvent)(event)).exists()){
                Toast.makeText(getActivity(),"Account already exists",Toast.LENGTH_SHORT).show();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new UserHomeFragment(mainActivity)).commit();
            }
        }
    }
}
