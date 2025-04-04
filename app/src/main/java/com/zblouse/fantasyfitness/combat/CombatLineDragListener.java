package com.zblouse.fantasyfitness.combat;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.combat.cards.CardType;

public class CombatLineDragListener implements View.OnDragListener {

    private final CombatFragment combatFragment;
    private final CombatLine thisLine;

    public CombatLineDragListener(CombatFragment combatFragment, CombatLine thisLine){
        this.combatFragment = combatFragment;
        this.thisLine = thisLine;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {

        switch(dragEvent.getAction()){
            case DragEvent.ACTION_DROP:{
                View draggedView = (View)dragEvent.getLocalState();
                CombatCardModel combatCardModel = (CombatCardModel) draggedView.getTag();
                if(combatCardModel.getCardType().equals(CardType.CHARACTER)) {
                    combatFragment.reportLineDrop(combatCardModel, thisLine);
                    view.setBackgroundColor(ContextCompat.getColor(combatFragment.getActivity(),R.color.fantasy_fitness_white));
                }
                return true;
            }
            case DragEvent.ACTION_DRAG_ENTERED: {
                View draggedView = (View) dragEvent.getLocalState();
                CombatCardModel combatCardModel = (CombatCardModel) draggedView.getTag();
                if (combatCardModel.getCardType().equals(CardType.CHARACTER)) {
                    view.setBackgroundColor(ContextCompat.getColor(combatFragment.getActivity(),R.color.fantasy_fitness_orange));
                }
                return true;
            }
            case DragEvent.ACTION_DRAG_EXITED: {
                View draggedView = (View) dragEvent.getLocalState();
                CombatCardModel combatCardModel = (CombatCardModel) draggedView.getTag();
                if (combatCardModel.getCardType().equals(CardType.CHARACTER)) {
                    view.setBackgroundColor(ContextCompat.getColor(combatFragment.getActivity(),R.color.fantasy_fitness_white));
                }
                return true;
            }
        }
        return true;
    }
}
