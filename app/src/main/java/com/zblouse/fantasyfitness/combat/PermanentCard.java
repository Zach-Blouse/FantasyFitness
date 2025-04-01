package com.zblouse.fantasyfitness.combat;

public class PermanentCard extends Card {

    private final Ability ability;

    public PermanentCard(String cardName, String cardDescription, Ability ability){
        super(CardType.PERMANENT, cardName, cardDescription);
        this.ability = ability;
    }

    public Ability getAbility(){
        return this.ability;
    }
}
