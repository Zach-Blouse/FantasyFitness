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
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.zblouse.fantasyfitness.actions.ActionResult;
import com.zblouse.fantasyfitness.actions.ActionResultType;
import com.zblouse.fantasyfitness.actions.CombatActionResult;
import com.zblouse.fantasyfitness.actions.DialogActionResult;
import com.zblouse.fantasyfitness.actions.ExploreActionEvent;
import com.zblouse.fantasyfitness.actions.ExploreActionService;
import com.zblouse.fantasyfitness.actions.NothingFoundActionResult;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.LocationForegroundDeviceService;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.ToastDeviceService;
import com.zblouse.fantasyfitness.combat.CombatFragment;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;
import com.zblouse.fantasyfitness.dialog.Dialog;
import com.zblouse.fantasyfitness.dialog.DialogSelectedEvent;
import com.zblouse.fantasyfitness.user.UserExistEvent;
import com.zblouse.fantasyfitness.user.UserGameState;
import com.zblouse.fantasyfitness.user.UserGameStateFetchResponseEvent;
import com.zblouse.fantasyfitness.workout.WorkoutRecordsFragment;
import com.zblouse.fantasyfitness.workout.WorkoutUpdateEvent;
import com.zblouse.fantasyfitness.world.GameLocation;
import com.zblouse.fantasyfitness.world.GameLocationService;

import java.util.HashMap;
import java.util.Map;


public class UserHomeFragment extends AuthenticationRequiredFragment implements EventListener {

    private LayoutInflater layoutInflater;
    private FrameLayout frameLayout;
    private ViewStub viewStub;
    private CardView nothingFoundCardView;
    private TextView nothingFoundFlavorText;
    private CardView dialogCardView;
    private TextView dialogFlavorText;
    private Button dialogOption1Button;
    private Button dialogOption2Button;
    private Button dialogOption3Button;
    private Button dialogOption4Button;

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
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.user_home_fragment,container,false);
        mainActivity.getUserGameStateService().fetchUserGameState(mainActivity.getCurrentUser().getUid(), new HashMap<>());
        viewStub = layout.findViewById(R.id.location_view_container);
        nothingFoundCardView = layout.findViewById(R.id.nothing_found_card_view);
        nothingFoundFlavorText = layout.findViewById(R.id.nothing_found_flavor_text);
        Button nothingFoundDismissButton = layout.findViewById(R.id.nothing_found_dismiss_button);
        nothingFoundDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nothingFoundCardView.setVisibility(View.GONE);
            }
        });
        nothingFoundCardView.setVisibility(View.GONE);

        dialogCardView = layout.findViewById(R.id.dialog_card_view);
        dialogFlavorText = layout.findViewById(R.id.dialog_flavor_text);
        dialogOption1Button = layout.findViewById(R.id.dialog_option_1_button);
        dialogOption2Button = layout.findViewById(R.id.dialog_option_2_button);
        dialogOption3Button = layout.findViewById(R.id.dialog_option_3_button);
        dialogOption4Button = layout.findViewById(R.id.dialog_option_4_button);
        Button leaveDialogButton = layout.findViewById(R.id.dialog_exit_button);
        leaveDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCardView.setVisibility(View.GONE);
            }
        });

        dialogCardView.setVisibility(View.GONE);
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
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UserGameState userGameState = ((UserGameStateFetchResponseEvent)event).getUserGameState();
                    if(userGameState != null) {
                        loadLocationUi(userGameState.getCurrentGameLocationName());
                    }
                }
            });

        } else if(event.getEventType().equals(EventType.EXPLORE_ACTION_EVENT)){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActionResult actionResult = ((ExploreActionEvent)event).getExploreActionResult();
                    if(actionResult.getActionResultType().equals(ActionResultType.NOTHING)){
                        nothingFoundFlavorText.setText(((NothingFoundActionResult)actionResult).getFlavorText());
                        nothingFoundCardView.setVisibility(View.VISIBLE);
                    } else if(actionResult.getActionResultType().equals(ActionResultType.DIALOG)){
                        Dialog baseDialog = ((DialogActionResult)actionResult).getInitialDialog();
                        loadDialogs(baseDialog);
                    } else if(actionResult.getActionResultType().equals(ActionResultType.COMBAT)){
                        CombatActionResult combatActionResult = (CombatActionResult)actionResult;
                        mainActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new CombatFragment(mainActivity, combatActionResult.getEncounterName())).commit();
                    }
                }
            });

        } else if(event.getEventType().equals(EventType.DIALOG_SELECTED_EVENT)){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadDialogs(((DialogSelectedEvent)event).getNewDialog());
                }
            });
        }
    }

    private void loadDialogs(Dialog baseDialog){
        dialogFlavorText.setText(baseDialog.getFlavorText());
        if(baseDialog.getDialogOption1() != null){
            Dialog dialogOption1 = mainActivity.getDialogService().fetchDialogOption(baseDialog.getDialogOption1());
            dialogOption1Button.setText(dialogOption1.getOptionText());
            dialogOption1Button.setVisibility(View.VISIBLE);
            dialogOption1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getDialogService().selectDialogOption(baseDialog.getDialogOption1(), new HashMap<>());
                }
            });
        } else {
            dialogOption1Button.setVisibility(View.GONE);
        }
        if(baseDialog.getDialogOption2() != null){
            Dialog dialogOption2 = mainActivity.getDialogService().fetchDialogOption(baseDialog.getDialogOption2());
            dialogOption2Button.setText(dialogOption2.getOptionText());
            dialogOption2Button.setVisibility(View.VISIBLE);
            dialogOption2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getDialogService().selectDialogOption(baseDialog.getDialogOption2(), new HashMap<>());
                }
            });
        } else {
            dialogOption2Button.setVisibility(View.GONE);
        }
        if(baseDialog.getDialogOption3() != null){
            Dialog dialogOption3 = mainActivity.getDialogService().fetchDialogOption(baseDialog.getDialogOption3());
            dialogOption3Button.setText(dialogOption3.getOptionText());
            dialogOption3Button.setVisibility(View.VISIBLE);
            dialogOption3Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getDialogService().selectDialogOption(baseDialog.getDialogOption3(), new HashMap<>());
                }
            });
        } else {
            dialogOption3Button.setVisibility(View.GONE);
        }
        if(baseDialog.getDialogOption4() != null){
            Dialog dialogOption4 = mainActivity.getDialogService().fetchDialogOption(baseDialog.getDialogOption4());
            dialogOption4Button.setText(dialogOption4.getOptionText());
            dialogOption4Button.setVisibility(View.VISIBLE);
            dialogOption4Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getDialogService().selectDialogOption(baseDialog.getDialogOption4(), new HashMap<>());
                }
            });
        } else {
            dialogOption4Button.setVisibility(View.GONE);
        }
        dialogCardView.setVisibility(View.VISIBLE);
    }

    private void loadLocationUi(String currentGameLocation){
        switch(currentGameLocation){
            case GameLocationService.FARMLANDS: {
                viewStub.setLayoutResource(R.layout.farmlands);
                View layout = viewStub.inflate();
                Button innButton = layout.findViewById(R.id.inn_button);
                innButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!actionResultDisplayed()) {
                            Map<String, Object> metadata = new HashMap<>();
                            metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, currentGameLocation);
                            metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.inn_button);
                            mainActivity.getExploreActionService().exploreAction(metadata);
                        }
                    }
                });
                break;
            }
            case GameLocationService.THANADEL_VILLAGE: {
                viewStub.setLayoutResource(R.layout.thanadel_village_layout);
                View layout = viewStub.inflate();
                Button innButton = layout.findViewById(R.id.inn_button);
                innButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!actionResultDisplayed()) {
                            Map<String, Object> metadata = new HashMap<>();
                            metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, currentGameLocation);
                            metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.inn_button);
                            mainActivity.getExploreActionService().exploreAction(metadata);
                        }
                    }
                });
                Button generalStoreButton = layout.findViewById(R.id.general_store_button);
                generalStoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!actionResultDisplayed()) {
                            Map<String, Object> metadata = new HashMap<>();
                            metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, currentGameLocation);
                            metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.general_store_button);
                            mainActivity.getExploreActionService().exploreAction(metadata);
                        }
                    }
                });
                break;
            }
            case GameLocationService.WOODLANDS: {
                viewStub.setLayoutResource(R.layout.woodlands_layout);
                View layout = viewStub.inflate();
                Button darkForestButton = layout.findViewById(R.id.dark_forest_button);
                darkForestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!actionResultDisplayed()) {
                            Map<String, Object> metadata = new HashMap<>();
                            metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, currentGameLocation);
                            metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.dark_forest_button);
                            mainActivity.getExploreActionService().exploreAction(metadata);
                        }
                    }
                });
                Button cavesButton = layout.findViewById(R.id.cave_button);
                cavesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!actionResultDisplayed()) {
                            Map<String, Object> metadata = new HashMap<>();
                            metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, currentGameLocation);
                            metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.cave_button);
                            mainActivity.getExploreActionService().exploreAction(metadata);
                        }
                    }
                });
                Button marshlandsButton = layout.findViewById(R.id.marsh_button);
                marshlandsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!actionResultDisplayed()) {
                            Map<String, Object> metadata = new HashMap<>();
                            metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, currentGameLocation);
                            metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.marsh_button);
                            mainActivity.getExploreActionService().exploreAction(metadata);
                        }
                    }
                });
                break;
            }
            default:
                break;
        }
    }

    public boolean actionResultDisplayed(){
        return (View.VISIBLE == dialogCardView.getVisibility()) || (View.VISIBLE == nothingFoundCardView.getVisibility());
    }
}
