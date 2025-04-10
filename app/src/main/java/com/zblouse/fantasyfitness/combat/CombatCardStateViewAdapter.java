package com.zblouse.fantasyfitness.combat;

import android.content.ClipData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
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

import java.time.Instant;
import java.util.List;

public class CombatCardStateViewAdapter extends RecyclerView.Adapter<CombatCardStateViewAdapter.ViewHolder> {

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

        ContextCompat.getColor(combatFragment.getMainActivity(), R.color.fantasy_fitness_white);
        if(combatCardModel.isPlayerCard() || (combatCardModel.isPlayed() && !combatFragment.isInitialSetup())) {
            holder.cardNameTextView.setText(combatCardModel.getCardName());
            holder.cardDescriptionTextView.setText(combatCardModel.getCardDescription());
            if (combatCardModel.getCardType().equals(CardType.CHARACTER)) {
                holder.cardHealthTextView.setText(combatCardModel.getCurrentHealth() + "/" + combatCardModel.getMaxHealth());
            } else {
                holder.cardHealthTextView.setText("");
            }
            holder.card.setCardBackgroundColor(ContextCompat.getColor(combatFragment.getMainActivity(), R.color.fantasy_fitness_white));
        } else {
            holder.card.setCardBackgroundColor(ContextCompat.getColor(combatFragment.getMainActivity(), R.color.fantasy_fitness_red));
        }
        if(combatCardModel.isPlayerCard()){
            holder.card.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                holder.timeTouchStarted = Instant.now();
                                if (inHand) {
                                    if (holder.velocityTracker == null) {
                                        holder.velocityTracker = VelocityTracker.obtain();
                                    } else {
                                        holder.velocityTracker.clear();
                                    }
                                } else {
                                    if(combatFragment.isWaitingForAbilityTargeting()){
                                        combatFragment.attemptCardAbilityTarget(combatCardModel);
                                    }
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (!combatFragment.isCombatScreenCovered()) {
                                    if (holder.velocityTracker == null) {
                                        holder.velocityTracker = VelocityTracker.obtain();
                                    }
                                    holder.velocityTracker.addMovement(motionEvent);
                                    holder.velocityTracker.computeCurrentVelocity(100);
                                    Log.e("VELOCITY", "X VELOCITY: " + holder.velocityTracker.getXVelocity());
                                    Log.e("VELOCITY", "Y VELOCITY: " + holder.velocityTracker.getYVelocity());
                                    if(Math.abs(holder.velocityTracker.getYVelocity()) > Math.abs(holder.velocityTracker.getXVelocity()) && inHand && combatFragment.isPlayerTurn()) {
                                        ClipData data = ClipData.newPlainText("", "");
                                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                                        view.startDragAndDrop(data, shadowBuilder, view, 0);
                                    } else if(Instant.now().isAfter(holder.timeTouchStarted.plusMillis(350))){
                                        combatFragment.cardHeld(combatCardModel);
                                    }
                                }
                        }


                    return true;
                }

            });
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
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CombatCardStateViewAdapter", combatCardModel.getCardName() + " has been clicked");
                if(combatFragment.isWaitingForAbilityTargeting() && !inHand){
                    combatFragment.attemptCardAbilityTarget(combatCardModel);
                }
            }
        });
        holder.card.setOnDragListener(new CombatCardDragListener(combatFragment, combatCardModel, inHand));
        holder.card.setTag(combatCardModel);

    }

    @Override
    public int getItemCount() {
        return combatCardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView cardNameTextView;
        public TextView cardHealthTextView;
        public TextView cardDescriptionTextView;
        public VelocityTracker velocityTracker;
        public Instant timeTouchStarted;

        public ViewHolder(View itemView){
            super(itemView);
            card = itemView.findViewById(R.id.card);
            cardNameTextView = itemView.findViewById(R.id.card_name);
            cardHealthTextView = itemView.findViewById(R.id.card_health);
            cardDescriptionTextView = itemView.findViewById(R.id.card_description);
            velocityTracker = null;
        }
    }
}
