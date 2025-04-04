package com.zblouse.fantasyfitness.combat;

import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.CardType;

import java.util.ArrayList;
import java.util.List;

public class CombatCardModel {
    private final String cardName;
    private final String cardDescription;
    private final CardType cardType;
    private int maxHealth;
    private int currentHealth;
    private List<Ability> abilities;
    private Ability ability;
    private boolean played;

    public CombatCardModel(String cardName, String cardDescription, CardType cardType, boolean played){
        this.cardName = cardName;
        this.cardDescription = cardDescription;
        this.cardType = cardType;
        this.played = played;
    }

    public String getCardName(){
        return this.cardName;
    }

    public String getCardDescription(){
        return this.cardDescription;
    }

    public CardType getCardType(){
        return this.cardType;
    }

    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }

    public int getMaxHealth(){
        return this.maxHealth;
    }

    public int getCurrentHealth(){
        return this.currentHealth;
    }

    public void setCurrentHealth(int currentHealth){
        this.currentHealth = currentHealth;
    }

    public void setAbilities(List<Ability> abilities){
        this.abilities = abilities;
    }

    public List<Ability> getAbilities(){
        return this.abilities;
    }

    public void addAbility(Ability ability){
        if(abilities == null){
            abilities = new ArrayList<>();
        }
        abilities.add(ability);
    }

    public void setAbility(Ability ability){
        this.ability = ability;
    }

    public Ability getAbility(){
        return this.ability;
    }

    public boolean isPlayed(){
        return this.played;
    }

    public void setPlayed(boolean played){
        this.played = played;
    }
}
