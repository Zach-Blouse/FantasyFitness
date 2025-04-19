package com.zblouse.fantasyfitness.merchant;

import com.zblouse.fantasyfitness.combat.cards.Card;

import java.util.Map;

public class Merchant {

    private final String merchantTag;
    private final Map<Card, Integer> cardPriceMap;

    public Merchant(String merchantTag, Map<Card, Integer> cardPriceMap){
        this.merchantTag = merchantTag;
        this.cardPriceMap = cardPriceMap;
    }

    public String getMerchantTag(){
        return this.merchantTag;
    }

    public Map<Card, Integer> getCardPriceMap(){
        return this.cardPriceMap;
    }
}
