package com.zblouse.fantasyfitness.world;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;

public class GameWorldFragment extends AuthenticationRequiredFragment {

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
        verticalScrollView.setOnTouchListener(new View.OnTouchListener() {

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
        });

        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });


        Button town1Button = layout.findViewById(R.id.town1Button);
        town1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Town 1",Toast.LENGTH_SHORT).show();
            }
        });

        Button town2Button = layout.findViewById(R.id.town2Button);
        town2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Town 2",Toast.LENGTH_SHORT).show();
            }
        });

        return layout;
    }

}
