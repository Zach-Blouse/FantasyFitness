package com.zblouse.fantasyfitness.combat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AbilityType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;

import java.util.Arrays;
import java.util.List;

public class AbilityViewAdapter extends RecyclerView.Adapter<AbilityViewAdapter.ViewHolder> {

    private List<Ability> abilities;
    private CombatFragment combatFragment;
    private CombatCardModel cardAttachedTo;
    private boolean inDetailedView;

    public AbilityViewAdapter(CombatCardModel cardAttachedTo, CombatFragment combatFragment, boolean inDetailedView){
        if(cardAttachedTo.getCardType().equals(CardType.CHARACTER)) {
            this.abilities = cardAttachedTo.getAbilities();
        } else {
            this.abilities = Arrays.asList(cardAttachedTo.getAbility());
        }
        this.combatFragment = combatFragment;
        this.cardAttachedTo = cardAttachedTo;
        this.inDetailedView = inDetailedView;
    }

    @NonNull
    @Override
    public AbilityViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ability_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbilityViewAdapter.ViewHolder holder, int position) {
        if(!cardAttachedTo.isPlayerCard() && (!cardAttachedTo.isPlayed() || combatFragment.isInitialSetup())){
            holder.abilityCard.setVisibility(View.GONE);
        }
        Ability ability = abilities.get(position);
        if(cardAttachedTo.isPlayed()){
            if(ability.getAbilityType().equals(AbilityType.DAMAGE) || ability.getAbilityType().equals(AbilityType.HEAL)){
                holder.abilityNameTextView.setText(ability.getAbilityName());
                holder.abilityImpactTextView.setText(getImpactText(ability));
                if(inDetailedView) {
                    holder.abilityCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!cardAttachedTo.hasUsedAbilityThisTurn()) {
                                combatFragment.abilityUsed(cardAttachedTo, ability);
                            }
                        }
                    });
                }
            }
        } else {
            if(cardAttachedTo.getCardType().equals(CardType.CHARACTER)) {
                if (ability.getAbilityType().equals(AbilityType.DAMAGE) || ability.getAbilityType().equals(AbilityType.HEAL)) {
                    holder.abilityNameTextView.setText(ability.getAbilityName());
                    if(inDetailedView) {
                        holder.abilityImpactTextView.setText(getImpactText(ability));
                    }
                }
            } else if(cardAttachedTo.getCardType().equals(CardType.ITEM)){
                holder.abilityNameTextView.setText("");
                if(ability.getAbilityType().equals(AbilityType.HEAL)){
                    holder.abilityImpactTextView.setText(getImpactText(ability));
                }else if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
                    DamageAbility damageAbility = (DamageAbility)ability;
                    String damageItemImpactString = "Gives the card this is attached to the ability to make a "
                            + damageAbility.getAttackType().toString().toLowerCase() + " attack for "
                            + damageAbility.getDamageAmount() + " " + damageAbility.getDamageType().toString().toLowerCase() + " damage to ";
                    if(ability.getAbilityTarget().equals(AbilityTarget.SINGLE_ENEMY)){
                        damageItemImpactString += " one enemy";
                    } else if(ability.getAbilityTarget().equals(AbilityTarget.ROW_ENEMY)){
                        damageItemImpactString += " a line of enemies";
                    }else if(ability.getAbilityTarget().equals(AbilityTarget.ALL_ENEMY)){
                        damageItemImpactString += " all enemies";
                    }
                    holder.abilityImpactTextView.setText(damageItemImpactString);
                } else if(ability.getAbilityType().equals(AbilityType.BUFF)){
                    BuffAbility buffAbility = (BuffAbility) ability;
                    String buffItemImpactString = "Gives the card this is attached to ";
                    if(buffAbility.getBuffType().equals(BuffType.HEALTH)){
                        buffItemImpactString += buffAbility.getBuffAmount() + " extra max HP";
                    } else if(buffAbility.getBuffType().equals(BuffType.ATTACK)){
                        buffItemImpactString += buffAbility.getBuffAmount() + "extra damage on each attack";
                    }

                    holder.abilityImpactTextView.setText(buffItemImpactString);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return abilities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView abilityCard;
        public TextView abilityNameTextView;
        public TextView abilityImpactTextView;

        public ViewHolder(View itemView){
            super(itemView);
            abilityCard = itemView.findViewById(R.id.ability_card);
            abilityNameTextView = itemView.findViewById(R.id.ability_name);
            abilityImpactTextView = itemView.findViewById(R.id.ability_impact);
            if(abilityNameTextView == null){
                Log.e("AbilityViewAdapter","name view null");
            }
            if(abilityImpactTextView == null){
                Log.e("AbilityViewAdapter","impact view null");
            }
        }
    }

    private String getImpactText(Ability ability){
        String impactText = "";
        if(ability.getAbilityType().equals(AbilityType.HEAL)){

            impactText+="Heals " + ((HealAbility)ability).getHealAmount() + " HP";
        }else if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
            impactText+= "Does " + ((DamageAbility)ability).getDamageAmount() + " " + ((DamageAbility)ability).getDamageType().toString().toLowerCase() + " damage ";
        }
        if(ability.getAbilityTarget().equals(AbilityTarget.ROW_ALLY)){
            impactText += " to a full line of your cards";
        } else if(ability.getAbilityTarget().equals(AbilityTarget.ROW_ENEMY)){
            impactText += " to a full line of your opponent's cards";
        } else if(ability.getAbilityTarget().equals(AbilityTarget.SINGLE_ALLY)){
            impactText += " to a single one of your cards";
        } else if(ability.getAbilityTarget().equals(AbilityTarget.SINGLE_ENEMY)){
            impactText += " to a single opponent card";
        } else if(ability.getAbilityTarget().equals(AbilityTarget.ALL_ALLY)){
            impactText += " to all of your cards";
        } else if(ability.getAbilityTarget().equals(AbilityTarget.ALL_ENEMY)){
            impactText += " to all opponent's cards";
        } else if(ability.getAbilityTarget().equals(AbilityTarget.SELF)){
            impactText += " to this card";
        }
        return impactText;
    }
}
