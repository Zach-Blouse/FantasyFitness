package com.zblouse.fantasyfitness.combat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.Deck;
import com.zblouse.fantasyfitness.combat.cards.DeckFetchEvent;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;

public class CombatFragment extends AuthenticationRequiredFragment implements EventListener {

    ConstraintLayout layout;

    public CombatFragment(){
        super(R.layout.combat_fragment);
    }

    public CombatFragment(MainActivity mainActivity){
        super(R.layout.combat_fragment, mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        mainActivity.hideNavigation();
        layout = (ConstraintLayout) inflater.inflate(R.layout.combat_fragment, container, false);
        mainActivity.getCombatService().initializeCombat();
        return layout;
    }

    @Override
    public void publishEvent(Event event) {
        Log.e("combatFragment", "publishEventType: " + event.getEventType());
        if (event.getEventType().equals(EventType.DECK_FETCH_EVENT)) {
            Deck userDeck = ((DeckFetchEvent)event).getDeck();
            mainActivity.getCombatService().deckFetchReturned(userDeck);
        }
    }
}
