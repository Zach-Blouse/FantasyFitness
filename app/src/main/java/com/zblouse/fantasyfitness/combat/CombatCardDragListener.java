package com.zblouse.fantasyfitness.combat;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.CardType;

public class CombatCardDragListener implements View.OnDragListener {

    private final CombatFragment combatFragment;
    private final CombatCardModel thisCard;
    private final boolean inHand;

    public CombatCardDragListener(CombatFragment combatFragment, CombatCardModel thisCard, boolean inHand){
        this.combatFragment = combatFragment;
        this.thisCard = thisCard;
        this.inHand = inHand;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {

        if(!inHand) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DROP: {
                    View draggedView = (View) dragEvent.getLocalState();
                    CombatCardModel combatCardModel = (CombatCardModel) draggedView.getTag();
                    if (combatCardModel.getCardType().equals(CardType.ITEM) && !combatCardModel.getAbility().getAbilityTarget().equals(AbilityTarget.ALL_ALLY) && !combatCardModel.getAbility().getAbilityTarget().equals(AbilityTarget.ROW_ALLY)) {
                        Log.e("DRAGLISTENER","ITEM DROP");
                        view.setBackgroundColor(ContextCompat.getColor(combatFragment.getActivity(), R.color.fantasy_fitness_white));
                        combatFragment.reportCardDrop(combatCardModel, thisCard);
                    } else {
                        Log.e("DRAGLISTENER",combatCardModel.getCardType().toString());
                    }
                    return true;
                }
                case DragEvent.ACTION_DRAG_ENTERED: {
                    View draggedView = (View) dragEvent.getLocalState();
                    CombatCardModel combatCardModel = (CombatCardModel) draggedView.getTag();
                    if (combatCardModel.getCardType().equals(CardType.ITEM) && !combatCardModel.getAbility().getAbilityTarget().equals(AbilityTarget.ALL_ALLY) && !combatCardModel.getAbility().getAbilityTarget().equals(AbilityTarget.ROW_ALLY)) {
                        view.setBackgroundColor(ContextCompat.getColor(combatFragment.getActivity(), R.color.fantasy_fitness_orange));
                    }
                    return true;
                }
                case DragEvent.ACTION_DRAG_EXITED: {
                    View draggedView = (View) dragEvent.getLocalState();
                    CombatCardModel combatCardModel = (CombatCardModel) draggedView.getTag();
                    if (combatCardModel.getCardType().equals(CardType.ITEM) && !combatCardModel.getAbility().getAbilityTarget().equals(AbilityTarget.ALL_ALLY) && !combatCardModel.getAbility().getAbilityTarget().equals(AbilityTarget.ROW_ALLY)) {
                        view.setBackgroundColor(ContextCompat.getColor(combatFragment.getActivity(), R.color.fantasy_fitness_white));
                    }
                    return true;
                }
            }
        }
        return true;
    }
}
