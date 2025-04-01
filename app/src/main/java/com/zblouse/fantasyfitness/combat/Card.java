package com.zblouse.fantasyfitness.combat;

public abstract class Card {

    protected CardType cardType;
    protected String cardName;
    protected String cardDescription;

    public Card(CardType cardType, String cardName, String cardDescription){
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
}
