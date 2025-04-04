package com.zblouse.fantasyfitness.combat.cards;

public class EffectCard extends Card {

    public static final String ABILITY_FIELD = "ability";

    private Ability ability;

    public EffectCard(String userId, String cardUuid, String cardName, String cardDescription, Ability ability){
        super(userId, cardUuid, CardType.EFFECT, cardName, cardDescription);
        this.ability = ability;
    }

    public Ability getAbility(){
        return this.ability;
    }
}
