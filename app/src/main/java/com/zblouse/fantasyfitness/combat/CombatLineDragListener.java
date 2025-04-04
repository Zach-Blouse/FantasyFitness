package com.zblouse.fantasyfitness.combat;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

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
                }
            }
        }
        return true;
    }
}
