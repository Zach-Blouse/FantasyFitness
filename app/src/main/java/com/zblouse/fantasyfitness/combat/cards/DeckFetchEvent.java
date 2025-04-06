package com.zblouse.fantasyfitness.combat.cards;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class DeckFetchEvent extends Event {

    private final Deck deck;

    public DeckFetchEvent(Deck deck, Map<String, Object> metadata){
        super(EventType.DECK_FETCH_EVENT, metadata);
        this.deck = deck;
    }

    public Deck getDeck(){
        return this.deck;
    }
}
