package com.zblouse.fantasyfitness.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.zblouse.fantasyfitness.combat.CombatCardModel;
import com.zblouse.fantasyfitness.combat.CombatCardStateViewAdapter;
import com.zblouse.fantasyfitness.combat.CombatFragment;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;
import com.zblouse.fantasyfitness.dialog.BaseDialogFetchEvent;
import com.zblouse.fantasyfitness.dialog.Dialog;
import com.zblouse.fantasyfitness.dialog.DialogFetchEvent;
import com.zblouse.fantasyfitness.dialog.DialogSelectedEvent;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.quest.QuestFetchResponseEvent;
import com.zblouse.fantasyfitness.quest.QuestObjective;
import com.zblouse.fantasyfitness.quest.QuestObjectiveViewAdapter;
import com.zblouse.fantasyfitness.quest.QuestViewAdapter;
import com.zblouse.fantasyfitness.user.UserExistEvent;
import com.zblouse.fantasyfitness.user.UserGameState;
import com.zblouse.fantasyfitness.user.UserGameStateFetchResponseEvent;
import com.zblouse.fantasyfitness.workout.WorkoutRecordsFragment;
import com.zblouse.fantasyfitness.workout.WorkoutUpdateEvent;
import com.zblouse.fantasyfitness.world.GameLocation;
import com.zblouse.fantasyfitness.world.GameLocationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private String locationDisplayed;

    private CardView questsCardView;
    private RecyclerView questsRecyclerView;
    private List<Quest> questsList;
    private QuestViewAdapter questViewAdapter;
    private CardView questDetailsCardView;
    private List<QuestObjective> questObjectives;
    private QuestObjectiveViewAdapter questObjectiveViewAdapter;
    private RecyclerView detailedQuestObjectivesRecyclerView;
    private TextView detailedQuestNameTextView;
    private TextView detailedQuestDescriptionTextView;
    private ScrollView verticalScrollView;
    private HorizontalScrollView horizontalScrollView;

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

        //Implementing scrolling both directions at once, since vertical is the parent, the touch is implemented there
        verticalScrollView = layout.findViewById(R.id.world_map_vertical);
        horizontalScrollView = layout.findViewById(R.id.world_map_horizontal);
        horizontalScrollView.setOnTouchListener(getOnTouchListener(verticalScrollView,horizontalScrollView));

        verticalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return horizontalScrollView.onTouchEvent(motionEvent);
            }
        });

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
        questsList = new ArrayList<>();
        questsCardView = layout.findViewById(R.id.quests_view);
        questsRecyclerView = layout.findViewById(R.id.quest_recyclerView);
        questViewAdapter = new QuestViewAdapter(this, questsList);
        questsRecyclerView.setAdapter(questViewAdapter);
        LinearLayoutManager questLayoutManager = new LinearLayoutManager(mainActivity);
        questLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        questsRecyclerView.setLayoutManager(questLayoutManager);
        Button closeQuestViewButton = layout.findViewById(R.id.close_quests_button);
        closeQuestViewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                questsCardView.setVisibility(View.GONE);
            }
        });
        questsCardView.setVisibility(View.GONE);

        FloatingActionButton floatingQuestButton = layout.findViewById(R.id.quests_button);
        floatingQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getQuestService().fetchQuests(new HashMap<>());
                questsCardView.setVisibility(View.VISIBLE);
            }
        });


        questDetailsCardView = layout.findViewById(R.id.quest_detail_view);
        detailedQuestNameTextView = layout.findViewById(R.id.quest_name_label);
        detailedQuestDescriptionTextView = layout.findViewById(R.id.quest_description_label);
        detailedQuestObjectivesRecyclerView = layout.findViewById(R.id.quest_objective_recyclerView);
        questObjectives = new ArrayList<>();
        questObjectiveViewAdapter = new QuestObjectiveViewAdapter(questObjectives);
        detailedQuestObjectivesRecyclerView.setAdapter(questObjectiveViewAdapter);
        LinearLayoutManager questObjectivesLayoutManager = new LinearLayoutManager(mainActivity);
        questObjectivesLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        detailedQuestObjectivesRecyclerView.setLayoutManager(questObjectivesLayoutManager);
        Button closeQuestDetailButton = layout.findViewById(R.id.close_quest_detail_button);
        closeQuestDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questDetailsCardView.setVisibility(View.GONE);
            }
        });
        questDetailsCardView.setVisibility(View.GONE);

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
                        mainActivity.getDialogService().fetchBaseDialog(((DialogActionResult)actionResult).getInitialDialogReferenceId(), ((DialogActionResult)actionResult).isQuestDialog(), new HashMap<>());

                    } else if(actionResult.getActionResultType().equals(ActionResultType.COMBAT)){
                        CombatActionResult combatActionResult = (CombatActionResult)actionResult;
                        mainActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new CombatFragment(mainActivity, combatActionResult.getEncounterName(), combatActionResult.getCombatLocation(), combatActionResult.getCombatBuilding())).commit();
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
        } else if(event.getEventType().equals(EventType.QUEST_FETCH_RESPONSE_EVENT)){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    QuestFetchResponseEvent questFetchResponseEvent = (QuestFetchResponseEvent) event;
                    if(questFetchResponseEvent.getMetadata().containsKey(ExploreActionService.EXPLORE_ACTION_FETCH_QUESTS)){
                        mainActivity.getExploreActionService().exploreAction(questFetchResponseEvent.getQuests(), questFetchResponseEvent.getMetadata());
                    } else {
                        questsList.clear();
                        questsList.addAll(questFetchResponseEvent.getQuests());
                        questViewAdapter.notifyDataSetChanged();
                    }
                }
            });
        } else if(event.getEventType().equals(EventType.DIALOG_FETCH_EVENT)){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogFetchEvent dialogFetchEvent = (DialogFetchEvent) event;
                    dialogOptionFetchResponse(dialogFetchEvent.getDialogOption1(), dialogFetchEvent.getDialogOption2(), dialogFetchEvent.getDialogOption3(), dialogFetchEvent.getDialogOption4());
                }
            });
        } else if(event.getEventType().equals(EventType.BASE_DIALOG_FETCH_EVENT)){
            Log.e("UserHomeFragment","GOT BASE DIALOG FETCH EVENT");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BaseDialogFetchEvent baseDialogFetchEvent = (BaseDialogFetchEvent) event;
                    loadDialogs(baseDialogFetchEvent.getDialog());
                }
            });

        }
    }

    private void loadDialogs(Dialog baseDialog){
        dialogFlavorText.setText(baseDialog.getFlavorText());
        mainActivity.getDialogService().fetchDialogOptions(baseDialog,new HashMap<>());
        dialogCardView.setVisibility(View.VISIBLE);
    }

    private void dialogOptionFetchResponse(Dialog dialogOption1, Dialog dialogOption2, Dialog dialogOption3, Dialog dialogOption4){
        if(dialogOption1 != null){
            dialogOption1Button.setText(dialogOption1.getOptionText());
            dialogOption1Button.setVisibility(View.VISIBLE);
            dialogOption1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getDialogService().selectDialogOption(dialogOption1, locationDisplayed, new HashMap<>());
                }
            });
        } else {
            dialogOption1Button.setVisibility(View.GONE);
        }
        if(dialogOption2 != null){
            dialogOption2Button.setText(dialogOption2.getOptionText());
            dialogOption2Button.setVisibility(View.VISIBLE);
            dialogOption2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getDialogService().selectDialogOption(dialogOption2, locationDisplayed, new HashMap<>());
                }
            });
        } else {
            dialogOption2Button.setVisibility(View.GONE);
        }
        if(dialogOption3 != null){
            dialogOption3Button.setText(dialogOption3.getOptionText());
            dialogOption3Button.setVisibility(View.VISIBLE);
            dialogOption3Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getDialogService().selectDialogOption(dialogOption3, locationDisplayed, new HashMap<>());
                }
            });
        } else {
            dialogOption3Button.setVisibility(View.GONE);
        }
        if(dialogOption4 != null){
            dialogOption4Button.setText(dialogOption4.getOptionText());
            dialogOption4Button.setVisibility(View.VISIBLE);
            dialogOption4Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getDialogService().selectDialogOption(dialogOption4, locationDisplayed, new HashMap<>());
                }
            });
        } else {
            dialogOption4Button.setVisibility(View.GONE);
        }
    }


    private void loadLocationUi(String currentGameLocation){
        locationDisplayed = currentGameLocation;
        switch(currentGameLocation){
            case GameLocationService.BRIDGETON: {
                viewStub.setLayoutResource(R.layout.bridgeton_layout);
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
                Button blacksmithButton = layout.findViewById(R.id.blacksmith_button);
                blacksmithButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!actionResultDisplayed()) {
                            Map<String, Object> metadata = new HashMap<>();
                            metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, currentGameLocation);
                            metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.blacksmith_button);
                            mainActivity.getExploreActionService().exploreAction(metadata);
                        }
                    }
                });
                break;
            }
            case GameLocationService.FAOLYN: {
                viewStub.setLayoutResource(R.layout.faolyn_layout);
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
                Button blacksmithButton = layout.findViewById(R.id.blacksmith_button);
                blacksmithButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!actionResultDisplayed()) {
                            Map<String, Object> metadata = new HashMap<>();
                            metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, currentGameLocation);
                            metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.blacksmith_button);
                            mainActivity.getExploreActionService().exploreAction(metadata);
                        }
                    }
                });
                break;
            }
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

    public void displayQuestDetails(Quest quest){
        detailedQuestNameTextView.setText(quest.getQuestName());
        detailedQuestDescriptionTextView.setText(quest.getQuestDescription());
        questObjectives.clear();
        questObjectives.addAll(quest.getQuestObjectives());
        questObjectiveViewAdapter.notifyDataSetChanged();

        questsCardView.setVisibility(View.GONE);
        questDetailsCardView.setVisibility(View.VISIBLE);
    }

    private View.OnTouchListener getOnTouchListener(ScrollView verticalScrollView, HorizontalScrollView horizontalScrollView){
        return new View.OnTouchListener() {

            private float mx, my, curX, curY;
            private boolean started = false;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                curX = event.getX();
                curY = event.getY();
                int dx = (int) (mx - curX);
                int dy = (int) (my - curY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (started) {
                            verticalScrollView.scrollBy(0, dy);
                            horizontalScrollView.scrollBy(dx, 0);
                        } else {
                            started = true;
                        }
                        mx = curX;
                        my = curY;
                        break;
                    case MotionEvent.ACTION_UP:
                        verticalScrollView.scrollBy(0, dy);
                        horizontalScrollView.scrollBy(dx, 0);
                        started = false;
                        break;
                }
                return false;
            }
        };
    }
}
