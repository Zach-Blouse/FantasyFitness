package com.zblouse.fantasyfitness.combat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.combat.cards.CardType;

import java.util.List;

public class CombatCardStateViewAdapter extends RecyclerView.Adapter<CombatCardStateViewAdapter.ViewHolder>{

    private List<CombatCardModel> combatCardModelList;
    private boolean inHand;

    public CombatCardStateViewAdapter(List<CombatCardModel> combatCardModelList, boolean inHand){
        this.combatCardModelList = combatCardModelList;
        this.inHand = inHand;
    }

    @NonNull
    @Override
    public CombatCardStateViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CombatCardStateViewAdapter.ViewHolder holder, int position) {
        CombatCardModel combatCardModel = combatCardModelList.get(position);

        holder.cardNameTextView.setText(combatCardModel.getCardName());
        holder.cardDescriptionTextView.setText(combatCardModel.getCardDescription());
        if(combatCardModel.getCardType().equals(CardType.CHARACTER)){
            holder.cardHealthTextView.setText(combatCardModel.getCurrentHealth() + "/" + combatCardModel.getMaxHealth());
        }
    }

    @Override
    public int getItemCount() {
        return combatCardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardNameTextView;
        public TextView cardHealthTextView;
        public TextView cardDescriptionTextView;
        public RecyclerView abilitiesRecyclerView;

        public ViewHolder(View itemView){
            super(itemView);
            cardNameTextView = itemView.findViewById(R.id.card_name);
            cardHealthTextView = itemView.findViewById(R.id.card_health);
            cardDescriptionTextView = itemView.findViewById(R.id.card_description);
            abilitiesRecyclerView = itemView.findViewById(R.id.ability_recycler_view);

        }
    }
}
