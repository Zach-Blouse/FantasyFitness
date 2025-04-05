package com.zblouse.fantasyfitness.combat;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.combat.cards.CardType;

import java.util.List;

public class CombatCardStateViewAdapter extends RecyclerView.Adapter<CombatCardStateViewAdapter.ViewHolder> implements View.OnTouchListener {

    private List<CombatCardModel> combatCardModelList;
    private CombatFragment combatFragment;
    private boolean inHand;

    public CombatCardStateViewAdapter(List<CombatCardModel> combatCardModelList, boolean inHand, CombatFragment combatFragment){
        this.combatCardModelList = combatCardModelList;
        this.inHand = inHand;
        this.combatFragment = combatFragment;
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
        ContextCompat.getColor(combatFragment.getActivity(), R.color.fantasy_fitness_white);
        if(combatCardModel.isPlayerCard() || (combatCardModel.isPlayed() && !combatFragment.isInitialSetup())) {
            holder.cardNameTextView.setText(combatCardModel.getCardName());
            holder.cardDescriptionTextView.setText(combatCardModel.getCardDescription());
            if (combatCardModel.getCardType().equals(CardType.CHARACTER)) {
                holder.cardHealthTextView.setText(combatCardModel.getCurrentHealth() + "/" + combatCardModel.getMaxHealth());
            } else {
                holder.cardHealthTextView.setText("");
            }
        } else {
            holder.card.setCardBackgroundColor(ContextCompat.getColor(combatFragment.getActivity(), R.color.fantasy_fitness_red));
        }
        if(combatCardModel.isPlayerCard()){
            holder.card.setOnTouchListener(this);
        }
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(combatCardModel.isPlayerCard() || (combatCardModel.isPlayed() && !combatFragment.isInitialSetup())) {
                    combatFragment.cardHeld(combatCardModel);
                }
                return true;
            }
        });
        holder.card.setOnDragListener(new CombatCardDragListener(combatFragment, combatCardModel, inHand));
        holder.card.setTag(combatCardModel);

        holder.abilitiesRecyclerView.setAdapter(new AbilityViewAdapter(combatCardModel, combatFragment, false));
        LinearLayoutManager abilitiesViewLayoutManager = new LinearLayoutManager(combatFragment.getContext());
        abilitiesViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.abilitiesRecyclerView.setLayoutManager(abilitiesViewLayoutManager);

    }

    @Override
    public int getItemCount() {
        return combatCardModelList.size();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(combatFragment.isPlayerTurn()) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (inHand) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        view.startDragAndDrop(data, shadowBuilder, view, 0);
                        return true;
                    }
            }
        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView cardNameTextView;
        public TextView cardHealthTextView;
        public TextView cardDescriptionTextView;
        public RecyclerView abilitiesRecyclerView;

        public ViewHolder(View itemView){
            super(itemView);
            card = itemView.findViewById(R.id.card);
            cardNameTextView = itemView.findViewById(R.id.card_name);
            cardHealthTextView = itemView.findViewById(R.id.card_health);
            cardDescriptionTextView = itemView.findViewById(R.id.card_description);
            abilitiesRecyclerView = itemView.findViewById(R.id.ability_recycler_view);
        }
    }
}
