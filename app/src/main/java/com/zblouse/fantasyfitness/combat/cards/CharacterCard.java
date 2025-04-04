package com.zblouse.fantasyfitness.combat.cards;

import java.util.ArrayList;
import java.util.List;

public class CharacterCard extends Card {

    public static final String MAX_HEALTH_FIELD = "maxHealth";
    public static final String CURRENT_HEALTH_FIELD = "currentHealth";
    public static final String ABILITIES_FIELD = "abilities";
    public static final String PERMANENT_CARDS_AFFIXED_NAMES_FIELD = "permanentCardsAffixedNames";

    private int maxHealth;
    private int currentHealth;
    private List<Ability> abilities;
    private List<String> permanentCardsAffixedNames;

    public CharacterCard(){
        //used by firebase
    }

    public CharacterCard(String userId, String cardUuid, String cardName, String cardDescription, Ability baseAbility, int maxHealth){
        super(userId, cardUuid, CardType.CHARACTER, cardName, cardDescription);
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.abilities = new ArrayList<>();
        abilities.add(baseAbility);
        this.permanentCardsAffixedNames = new ArrayList<>();
    }

    public CharacterCard(String userId, String cardUuid, String cardName, String cardDescription, int maxHealth, int currentHealth,  List<Ability> abilities, List<String> permanentCardsAffixedNames){
        super(userId, cardUuid, CardType.CHARACTER, cardName, cardDescription);
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.abilities = abilities;
        this.permanentCardsAffixedNames = permanentCardsAffixedNames;
    }

    public int getMaxHealth(){
        return this.maxHealth;
    }

    public int getCurrentHealth(){
        return this.currentHealth;
    }

    public int affectHealth(int affectAmount){
        this.currentHealth+=affectAmount;
        return currentHealth;
    }

    public void fullHeal(){
        this.currentHealth = maxHealth;
    }

    public List<Ability> getAbilities(){
        return this.abilities;
    }

    public void addAbility(Ability ability){
        abilities.add(ability);
    }

    public void addPermanantCardName(String name){
        this.permanentCardsAffixedNames.add(name);
    }
}
