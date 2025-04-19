package com.zblouse.fantasyfitness.merchant;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.HashMap;

public class MerchantDisplayEvent extends Event {

    private final Merchant merchant;

    public MerchantDisplayEvent(Merchant merchant){
        super(EventType.MERCHANT_DISPLAY_EVENT, new HashMap<>());
        this.merchant = merchant;
    }

    public Merchant getMerchant(){
        return this.merchant;
    }
}
