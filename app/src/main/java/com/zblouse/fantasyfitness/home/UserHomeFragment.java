package com.zblouse.fantasyfitness.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;
import com.zblouse.fantasyfitness.user.UserExistEvent;

public class UserHomeFragment extends AuthenticationRequiredFragment implements EventListener {

    public UserHomeFragment(){
        super(R.layout.user_home_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        ((MainActivity)getActivity()).getUserService().userExistCheck(((MainActivity)getActivity()).getCurrentUser().getUid());
        ((MainActivity)getActivity()).showNavigation();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.user_home_fragment,container,false);


        return layout;
    }

    @Override
    public void publishEvent(Event event) {
        if(event.getEventType().equals(EventType.USER_EXIST_EVENT)){
            Log.e(this.getClass().getName(),"USER EXIST EVENT: USER EXISTS:" + ((UserExistEvent)(event)).exists());
            if(!((UserExistEvent)(event)).exists()){
                Toast.makeText(getActivity(),"Account does not exist",Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).logout();
            }
        }
    }
}
