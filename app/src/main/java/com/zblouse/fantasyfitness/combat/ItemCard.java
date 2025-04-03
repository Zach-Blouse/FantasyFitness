package com.zblouse.fantasyfitness.combat;

public class ItemCard extends Card {

    private Ability ability;
    private ItemType itemType;

    public ItemCard(){
    }

    public ItemCard(String userId, String cardUuid, String cardName, String cardDescription, ItemType itemType, Ability ability){
        super(userId, cardUuid, CardType.ITEM, cardName, cardDescription);
        this.ability = ability;
        this.itemType = itemType;
    }

    public Ability getAbility(){
        return this.ability;
    }

    public ItemType getItemType(){
        return this.itemType;
    }
}
