package com.zblouse.fantasyfitness.merchant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.combat.AbilityViewAdapter;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.home.UserHomeFragment;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.quest.QuestViewAdapter;

import java.util.List;

public class MerchantViewAdaper extends RecyclerView.Adapter<MerchantViewAdaper.ViewHolder>{

    private List<Card> cards;
    private Merchant merchant;
    private MerchantService merchantService;
    private int lastKnownUserGold;
    private UserHomeFragment userHomeFragment;

    public MerchantViewAdaper(List<Card> cards, MerchantService merchantService, UserHomeFragment userHomeFragment){
        this.cards = cards;
        this.merchantService = merchantService;
        this.userHomeFragment = userHomeFragment;
    }

    public void setMerchant(Merchant merchant){
        this.merchant = merchant;
    }

    public void setLastKnownUserGold(int lastKnownUserGold){
        this.lastKnownUserGold = lastKnownUserGold;
    }

    @NonNull
    @Override
    public MerchantViewAdaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_view_layout,parent,false);
        return new MerchantViewAdaper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchantViewAdaper.ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.cardName.setText(card.getCardName());
        holder.cardPrice.setText("Price: " + merchant.getCardPriceMap().get(card) + " gold");
        holder.cardAbility.setText(AbilityViewAdapter.getImpactText(((ItemCard)card).getAbility()));
        holder.cardDescription.setText(card.getCardDescription());
        if(merchant.getCardPriceMap().get(card) <= lastKnownUserGold) {
            holder.itemCard.setClickable(true);
            holder.itemCard.setCardBackgroundColor(ContextCompat.getColor(userHomeFragment.getMainActivity(), R.color.fantasy_fitness_white));
            holder.itemCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    merchantService.buyCard(merchant.getMerchantTag(), card);
                }
            });
        } else {
            holder.itemCard.setClickable(false);
            holder.itemCard.setCardBackgroundColor(ContextCompat.getColor(userHomeFragment.getMainActivity(), R.color.fantasy_fitness_gray));
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView itemCard;
        public TextView cardName;
        public TextView cardPrice;
        public TextView cardDescription;
        public TextView cardAbility;

        public ViewHolder(View itemView){
            super(itemView);
            itemCard = itemView.findViewById(R.id.shop_card);
            cardName = itemView.findViewById(R.id.card_name);
            cardPrice = itemView.findViewById(R.id.card_price);
            cardDescription = itemView.findViewById(R.id.card_description);
            cardAbility = itemView.findViewById(R.id.ability_impact);
        }
    }
}
