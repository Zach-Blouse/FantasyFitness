package com.zblouse.fantasyfitness.world;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.HashMap;

public class GameWorldFragment extends AuthenticationRequiredFragment implements EventListener {

    private CardView locationInfoCardView;
    private TextView locationNameTextView;
    private TextView locationDescriptionTextView;
    private TextView locationConnectionsTextView;

    public GameWorldFragment(){
        super(R.layout.game_world_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.game_world_fragment,container,false);
        ((MainActivity)getActivity()).showNavigation();
        //Implementing scrolling both directions at once, since vertical is the parent, the touch is implemented there
        ScrollView verticalScrollView = layout.findViewById(R.id.world_map_vertical);
        HorizontalScrollView horizontalScrollView = layout.findViewById(R.id.world_map_horizontal);
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
        Button closeLocationInfoCardButton = layout.findViewById(R.id.close_location_info_button);
        closeLocationInfoCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeLocationInfoPopup();
            }
        });

        Button valleyOfMonstersButton = layout.findViewById(R.id.valley_of_monsters);
        valleyOfMonstersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getGameLocationService().fetchLocation(GameLocationService.VALLEY_OF_MONSTERS,new HashMap<>());
            }
        });
        Button towerButton = layout.findViewById(R.id.tower);
        towerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getGameLocationService().fetchLocation(GameLocationService.LAST_TOWER,new HashMap<>());
            }
        });
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

    @Override
    public void publishEvent(Event event) {
        Log.e("GAMEWORLDFRAGMENT", "EVENT PUBLISHED " + event.getEventType());
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
                Log.e("GAMEWORLDFRAGMENT", "LOCATION CARD SHOULD BE VISIBLE " + event.getEventType());
            }
        }
    }
}
