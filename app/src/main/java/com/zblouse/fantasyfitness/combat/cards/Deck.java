package com.zblouse.fantasyfitness.combat.cards;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards;
    private List<String> cardUuids;
    private String userId;
    private String deckName;

    public Deck(String userId, String deckName){
        cards = new ArrayList<>();
        cardUuids = new ArrayList<>();
        this.userId = userId;
        this.deckName = deckName;
    }

    public Deck(String userId, String deckName, List<String> cardUuids){
        this.cardUuids = cardUuids;
        this.cards = new ArrayList<>();
        this.userId = userId;
        this.deckName = deckName;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getDeckName(){
        return this.deckName;
    }

    public List<String> getCardUuids(){
        return this.cardUuids;
    }

    public void setCards(List<Card> cards){
        this.cards = cards;
    }

    public List<Card> getCards(){
        return this.cards;
    }

    public boolean isEmpty(){
        return cardUuids.isEmpty() && cards.isEmpty();
    }

    public boolean needToLoadCards(){
        return !cardUuids.isEmpty() && cards.isEmpty();
    }

}
