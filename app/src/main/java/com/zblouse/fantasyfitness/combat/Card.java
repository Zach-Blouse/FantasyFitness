package com.zblouse.fantasyfitness.combat;

import java.util.UUID;

public abstract class Card {

    protected String userId;
    protected String cardUuid;
    protected CardType cardType;
    protected String cardName;
    protected String cardDescription;

    public Card(){
        //used by firebase
    }

    public Card(String userId, String cardUuid, CardType cardType, String cardName, String cardDescription){
        this.userId = userId;
        this.cardUuid = cardUuid;
        this.cardType = cardType;
        this.cardName = cardName;
        this.cardDescription = cardDescription;
    }

    public CardType getCardType(){
        return this.cardType;
    }

    public String getCardName(){
        return this.cardName;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getCardDescription(){
        return this.cardDescription;
    }

    public String getCardUuid(){
        return this.cardUuid;
    }
}
