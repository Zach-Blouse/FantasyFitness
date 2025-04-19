package com.zblouse.fantasyfitness.merchant;

import android.content.Context;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.Map;

public class MerchantRepository implements Repository<Merchant> {

    private MerchantAbilityRepository merchantAbilityRepository;
    private MerchantCardSqlDatabase merchantCardSqlDatabase;

    public MerchantRepository(Context context){
        merchantAbilityRepository = new MerchantAbilityRepository(context);
        merchantCardSqlDatabase = new MerchantCardSqlDatabase(context);
    }

    public Merchant getMerchantByTag(String tag){
        return merchantCardSqlDatabase.getMerchantByTag(tag, merchantAbilityRepository);
    }

    public void writeMerchant(Merchant merchant){

        for(Card card: merchant.getCardPriceMap().keySet()){
            merchantAbilityRepository.writeMerchantAbility(((ItemCard)card).getAbility());
        }
        merchantCardSqlDatabase.write(merchant);
    }

    @Override
    public void readCallback(Merchant object, Map<String, Object> metadata) {

    }

    @Override
    public void writeCallback(Merchant object, Map<String, Object> metadata) {

    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {

    }
}
