package com.zblouse.fantasyfitness.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.ToastDeviceService;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;
import com.zblouse.fantasyfitness.user.UserExistEvent;
import com.zblouse.fantasyfitness.user.UserGameState;
import com.zblouse.fantasyfitness.user.UserGameStateFetchResponseEvent;
import com.zblouse.fantasyfitness.world.GameLocation;
import com.zblouse.fantasyfitness.world.GameLocationService;

import java.util.HashMap;


public class UserHomeFragment extends AuthenticationRequiredFragment implements EventListener {

    private LayoutInflater layoutInflater;
    private FrameLayout frameLayout;
    private ViewStub viewStub;

    public UserHomeFragment(){
        super(R.layout.user_home_fragment);
    }

    public UserHomeFragment(MainActivity mainActivity){
        super(R.layout.user_home_fragment, mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        layoutInflater = inflater;
        mainActivity.getUserService().userExistCheck(mainActivity.getCurrentUser().getUid());
        mainActivity.showNavigation();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.user_home_fragment,container,false);
        mainActivity.getUserGameStateService().fetchUserGameState(mainActivity.getCurrentUser().getUid(), new HashMap<>());
        viewStub = layout.findViewById(R.id.location_view_container);
        return layout;
    }

    @Override
    public void publishEvent(Event event) {
        if(event.getEventType().equals(EventType.USER_EXIST_EVENT)){
            if(!((UserExistEvent)(event)).exists()){
                ((ToastDeviceService)mainActivity.getDeviceService(DeviceServiceType.TOAST)).sendToast("Account does not exist");
                mainActivity.logout();
            }
        }else if(event.getEventType().equals(EventType.USER_GAME_STATE_FETCH_RESPONSE_EVENT)){
            UserGameState userGameState = ((UserGameStateFetchResponseEvent)event).getUserGameState();
            if(userGameState != null) {
                loadLocationUi(userGameState.getCurrentGameLocationName());
            }
        }
    }

    private void loadLocationUi(String currentGameLocation){
        Log.e("USER_HOME_FRAGMENT", "CURRENT LOCATION IS: " + currentGameLocation);
        switch(currentGameLocation){
            case GameLocationService.THANADEL_VILLAGE:
                Log.e("USER_HOME_FRAGMENT", "IN THANADEL VILLAGE");
                viewStub.setLayoutResource(R.layout.thanadel_village_layout);
                View layout = viewStub.inflate();
                Button innButton = layout.findViewById(R.id.inn_button);
                innButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ToastDeviceService)mainActivity.getDeviceService(DeviceServiceType.TOAST)).sendToast("Clicked the Inn");
                    }
                });
                Button generalStoreButton = layout.findViewById(R.id.general_store_button);
                generalStoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ToastDeviceService)mainActivity.getDeviceService(DeviceServiceType.TOAST)).sendToast("Clicked the General Store");
                    }
                });
                break;
            default:
                break;
        }
    }
}
