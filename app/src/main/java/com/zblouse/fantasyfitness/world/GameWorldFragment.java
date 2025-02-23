package com.zblouse.fantasyfitness.world;

import static androidx.core.content.ContextCompat.getColor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;
import com.zblouse.fantasyfitness.user.UserGameState;
import com.zblouse.fantasyfitness.user.UserGameStateFetchResponseEvent;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserGameStateUpdateEvent;

import java.util.HashMap;
import java.util.Map;

public class GameWorldFragment extends AuthenticationRequiredFragment implements EventListener {

    private ConstraintLayout layout;
    private CardView locationInfoCardView;
    private TextView locationNameTextView;
    private TextView locationDescriptionTextView;
    private TextView locationConnectionsTextView;
    private Button travelButton;
    private String lastKnownGameLocation;
    private Double lastKnownSavedDistanceMeters;
    private GameLocationPaths lastKnownGameLocationPaths;
    private ScrollView verticalScrollView;
    private HorizontalScrollView horizontalScrollView;

    private Map<String, Button> locationButtonMap;

    public GameWorldFragment(MainActivity mainActivity){
        super(R.layout.game_world_fragment, mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        layout = (ConstraintLayout) inflater.inflate(R.layout.game_world_fragment,container,false);
        mainActivity.showNavigation();
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

        locationInfoCardView = layout.findViewById(R.id.location_info_view);
        locationInfoCardView.setVisibility(View.INVISIBLE);
        locationNameTextView = layout.findViewById(R.id.location_info_name);
        locationDescriptionTextView = layout.findViewById(R.id.location_info_description);
        locationConnectionsTextView = layout.findViewById(R.id.location_info_connected_locations);
        travelButton = layout.findViewById(R.id.travel_button);

        Button closeLocationInfoCardButton = layout.findViewById(R.id.close_location_info_button);
        closeLocationInfoCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeLocationInfoPopup();
            }
        });

        locationButtonMap = new HashMap<>();

        Button valleyOfMonstersButton = layout.findViewById(R.id.valley_of_monsters);
        valleyOfMonstersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.VALLEY_OF_MONSTERS,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.VALLEY_OF_MONSTERS, valleyOfMonstersButton);
        Button towerButton = layout.findViewById(R.id.tower);
        towerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.LAST_TOWER,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.LAST_TOWER, towerButton);
        Button arduwynButton = layout.findViewById(R.id.arduwyn);
        arduwynButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.ARDUWYN,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.ARDUWYN, arduwynButton);
        Button monastaryButton = layout.findViewById(R.id.monastary);
        monastaryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.MONASTARY,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.MONASTARY, monastaryButton);
        Button northRoadButton = layout.findViewById(R.id.north_road);
        northRoadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.NORTH_ROAD,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.NORTH_ROAD, northRoadButton);
        Button faolynButton = layout.findViewById(R.id.faolyn);
        faolynButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.FAOLYN,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.FAOLYN, faolynButton);
        Button riverlandsButton = layout.findViewById(R.id.riverlands);
        riverlandsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.RIVERLANDS,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.RIVERLANDS, riverlandsButton);
        Button bridgetonButton = layout.findViewById(R.id.bridgeton);
        bridgetonButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.BRIDGETON,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.BRIDGETON, bridgetonButton);
        Button mountainPassButton = layout.findViewById(R.id.mountain_pass);
        mountainPassButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.MOUNTAIN_PASS,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.MOUNTAIN_PASS, mountainPassButton);
        Button woodlandsButton = layout.findViewById(R.id.woodlands);
        woodlandsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.WOODLANDS,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.WOODLANDS, woodlandsButton);
        Button thanadelButton = layout.findViewById(R.id.thanadel_village);
        thanadelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.THANADEL_VILLAGE,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.THANADEL_VILLAGE, thanadelButton);
        Button farmlandsButton = layout.findViewById(R.id.farmlands);
        farmlandsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.FARMLANDS,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.FARMLANDS, farmlandsButton);
        Button hillsButton = layout.findViewById(R.id.hills);
        hillsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getGameLocationService().fetchLocation(GameLocationService.HILLS,new HashMap<>());
            }
        });
        locationButtonMap.put(GameLocationService.HILLS, hillsButton);
        mainActivity.getUserGameStateService().fetchUserGameState(mainActivity.getCurrentUser().getUid(), new HashMap<>());
        return layout;
    }

    public void closeLocationInfoPopup(){
        locationInfoCardView.setVisibility(View.INVISIBLE);
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

    private void currentGameLocationUpdate(String lastKnownGameLocation){
        locationInfoCardView.setVisibility(View.INVISIBLE);
        this.lastKnownGameLocation = lastKnownGameLocation;
        for(Button button: locationButtonMap.values()){
            button.setBackgroundColor(getColor(getContext(), R.color.fantasy_fitness_green));
        }

        locationButtonMap.get(lastKnownGameLocation).setBackgroundColor(getColor(getContext(), R.color.fantasy_fitness_red));
        int buttonTop = locationButtonMap.get(lastKnownGameLocation).getTop();
        int buttonBottom = locationButtonMap.get(lastKnownGameLocation).getBottom();
        int scrollHeight = verticalScrollView.getBottom();
        verticalScrollView.smoothScrollTo(0, ((buttonTop + buttonBottom - scrollHeight) / 2));

        int buttonLeft = locationButtonMap.get(lastKnownGameLocation).getLeft();
        int buttonRight = locationButtonMap.get(lastKnownGameLocation).getRight();
        int horizontalScrollWidth = horizontalScrollView.getWidth();
        horizontalScrollView.smoothScrollTo(((buttonLeft + buttonRight - horizontalScrollWidth) / 2), 0);
    }

    @Override
    public void publishEvent(Event event) {
        if(event.getEventType().equals(EventType.LOCATION_FETCH_EVENT)){
            GameLocationFetchEvent gameLocationFetchEvent = ((GameLocationFetchEvent)event);
            GameLocation gameLocation = gameLocationFetchEvent.getLocation();
            if(gameLocation != null) {
                locationNameTextView.setText(gameLocation.getLocationName());
                locationDescriptionTextView.setText(gameLocation.getLocationDescription());
                String connectionsString = "Can Travel To:";
                for(GameLocation location: gameLocation.getConnectedLocations().keySet()){
                    connectionsString += "\n" + location.getLocationName() + " " + gameLocation.getConnectedLocations().get(location);
                }
                locationConnectionsTextView.setText(connectionsString);
                locationInfoCardView.setVisibility(View.VISIBLE);
                if(gameLocation.getLocationName().equals(lastKnownGameLocation)){
                    if(((ViewGroup)locationInfoCardView).findViewById(travelButton.getId()) != null) {
                        ((ViewGroup)locationInfoCardView).removeView(travelButton);
                    }
                }else{
                    if(((ViewGroup)locationInfoCardView).findViewById(travelButton.getId()) == null) {
                        ((ViewGroup) locationInfoCardView).addView(travelButton);
                    }
                    if(lastKnownGameLocationPaths.getPath(gameLocation.getLocationName()).getDistanceKm() <= (lastKnownSavedDistanceMeters/1000)) {
                        travelButton.setClickable(true);
                        travelButton.setBackgroundColor(getColor(getContext(), R.color.fantasy_fitness_green));
                        travelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mainActivity.getGameLocationService().travel(gameLocation);
                            }
                        });
                    } else {
                        travelButton.setClickable(false);
                        travelButton.setBackgroundColor(getColor(getContext(), R.color.fantasy_fitness_gray));
                    }
                }

            }
        }else if(event.getEventType().equals(EventType.USER_GAME_STATE_FETCH_RESPONSE_EVENT)){
            UserGameState userGameState = ((UserGameStateFetchResponseEvent)event).getUserGameState();
            lastKnownSavedDistanceMeters = userGameState.getSavedWorkoutDistanceMeters();
            currentGameLocationUpdate(userGameState.getCurrentGameLocationName());
            lastKnownGameLocationPaths = mainActivity.getGameLocationService().generatePaths(userGameState.getCurrentGameLocationName());
        }else if(event.getEventType().equals(EventType.USER_GAME_STATE_UPDATE_EVENT)){
            if(event.getMetadata().containsKey(UserGameStateService.NEW_LOCATION)){
                mainActivity.getUserGameStateService().fetchUserGameState(mainActivity.getCurrentUser().getUid(),new HashMap<>());
            }
        }
    }
}
