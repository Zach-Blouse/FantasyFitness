package com.zblouse.fantasyfitness.combat.cards;

import com.zblouse.fantasyfitness.core.Repository;

import java.util.Map;

public class DeckRepository implements Repository<Deck> {

    private DeckFirestoreDatabase deckFirestoreDatabase;
    private DeckService deckService;

    public DeckRepository(DeckService deckService){
        this.deckService = deckService;
        this.deckFirestoreDatabase = new DeckFirestoreDatabase();
    }

    public void writeDeck(Deck deck, Map<String, Object> metadata){
        deckFirestoreDatabase.writeDeck(deck, this, metadata);
    }

    public void fetchDeck(String userId, String deckName, Map<String, Object> metadata){
        deckFirestoreDatabase.readDeck(userId, deckName, this, metadata);
    }

    @Override
    public void readCallback(Deck deck, Map<String, Object> metadata) {
        if(deck != null){
            deckService.repositoryResponse(deck, metadata);
        }
    }

    @Override
    public void writeCallback(Deck object, Map<String, Object> metadata) {

    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {

    }
}
