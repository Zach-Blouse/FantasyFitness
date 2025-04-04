package com.zblouse.fantasyfitness.combat.cards;

import android.util.Log;

import com.zblouse.fantasyfitness.core.Repository;

import java.util.List;
import java.util.Map;

public class CardRepository implements Repository<Card> {

    private CardService cardService;
    private CardFirestoreDatabase cardFirestoreDatabase;

    public CardRepository(CardService cardService){
        this.cardService = cardService;
        this.cardFirestoreDatabase = new CardFirestoreDatabase();
    }

    public void writeCard(Card card, Map<String, Object> metadata){
        cardFirestoreDatabase.write(card, this, metadata);
    }

    public void fetchCardList(String userId, List<String> cardUuids, Map<String, Object> metadata){
        Log.e("CardRepository", "Getting card list");
        cardFirestoreDatabase.fetchListOfCards(userId, cardUuids, this, metadata);
    }

    public void listReadCallback(List<Card> cardList, Map<String, Object> metadata){
        Log.e("CardRepository", "listReadCallBack list size: " + cardList.size());
        cardService.repositoryResponse(cardList, metadata);
    }

    @Override
    public void readCallback(Card card, Map<String, Object> metadata) {
        cardService.repositoryResponse(card, metadata);
    }

    @Override
    public void writeCallback(Card card, Map<String, Object> metadata) {
        cardService.repositoryResponse(card, metadata);
    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {
        if(success){
            cardService.repositoryResponse((Card)null, metadata);
        }
    }
}
