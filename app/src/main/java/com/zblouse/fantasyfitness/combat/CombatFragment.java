package com.zblouse.fantasyfitness.combat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.actions.CombatActionResult;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.Deck;
import com.zblouse.fantasyfitness.combat.cards.DeckFetchEvent;
import com.zblouse.fantasyfitness.combat.encounter.Encounter;
import com.zblouse.fantasyfitness.combat.encounter.EncounterFetchEvent;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CombatFragment extends AuthenticationRequiredFragment implements EventListener {

    private ConstraintLayout layout;
    private RecyclerView enemyHand;
    private List<CombatCardModel> enemyHandList;
    private CombatCardStateViewAdapter enemyHandCombatCardStateViewAdapter;
    private RecyclerView enemyBackLine;
    private List<CombatCardModel> enemyBackLineList;
    private CombatCardStateViewAdapter enemyBackLineCombatCardStateViewAdapter;
    private RecyclerView enemyFrontLine;
    private List<CombatCardModel> enemyFrontLineList;
    private CombatCardStateViewAdapter enemyFrontLineCombatCardStateViewAdapter;
    private RecyclerView playerFrontLine;
    private List<CombatCardModel> playerFrontLineList;
    private CombatCardStateViewAdapter playerFrontLineCombatCardStateViewAdapter;
    private RecyclerView playerBackLine;
    private List<CombatCardModel> playerBackLineList;
    private CombatCardStateViewAdapter playerBackLineCombatCardStateViewAdapter;
    private RecyclerView playerHand;
    private List<CombatCardModel> playerHandList;
    private CombatCardStateViewAdapter playerHandCombatCardStateViewAdapter;

    private CardView detailedCardView;
    private TextView detailedCardName;
    private TextView detailedCardHealth;
    private TextView detailedCardDescription;
    private RecyclerView detailedAbilitiesRecycler;

    private Button endPlayerTurnButton;

    public CombatFragment(){
        super(R.layout.combat_fragment);
    }

    public CombatFragment(MainActivity mainActivity, String encounter){
        super(R.layout.combat_fragment, mainActivity);
        mainActivity.getEncounterService().fetchEncounter(encounter, new HashMap<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        mainActivity.hideNavigation();
        layout = (ConstraintLayout) inflater.inflate(R.layout.combat_fragment, container, false);
        enemyHand = layout.findViewById(R.id.enemyHand);
        enemyHandList = new ArrayList<>();
        enemyHandCombatCardStateViewAdapter = new CombatCardStateViewAdapter(enemyHandList, true, this);
        enemyHand.setAdapter(enemyHandCombatCardStateViewAdapter);
        LinearLayoutManager enemyHandLayoutManager = new LinearLayoutManager(mainActivity);
        enemyHandLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        enemyHand.setLayoutManager(enemyHandLayoutManager);
        enemyBackLine = layout.findViewById(R.id.enemyBackLine);
        enemyBackLineList = new ArrayList<>();
        enemyBackLineCombatCardStateViewAdapter = new CombatCardStateViewAdapter(enemyBackLineList, false, this);
        enemyBackLine.setAdapter(enemyBackLineCombatCardStateViewAdapter);
        LinearLayoutManager enemyBackLineLayoutManager = new LinearLayoutManager(mainActivity);
        enemyBackLineLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        enemyBackLine.setLayoutManager(enemyBackLineLayoutManager);
        enemyFrontLine = layout.findViewById(R.id.enemyFrontLine);
        enemyFrontLineList = new ArrayList<>();
        enemyFrontLineCombatCardStateViewAdapter = new CombatCardStateViewAdapter(enemyFrontLineList, false, this);
        enemyFrontLine.setAdapter(enemyFrontLineCombatCardStateViewAdapter);
        LinearLayoutManager enemyFrontLineLayoutManager = new LinearLayoutManager(mainActivity);
        enemyFrontLineLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        enemyFrontLine.setLayoutManager(enemyFrontLineLayoutManager);
        playerFrontLine = layout.findViewById(R.id.playerFrontLine);
        playerFrontLineList = new ArrayList<>();
        playerFrontLineCombatCardStateViewAdapter = new CombatCardStateViewAdapter(playerFrontLineList, false, this);
        playerFrontLine.setAdapter(playerFrontLineCombatCardStateViewAdapter);
        LinearLayoutManager playerFrontLineLayoutManager = new LinearLayoutManager(mainActivity);
        playerFrontLineLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        playerFrontLine.setLayoutManager(playerFrontLineLayoutManager);
        playerBackLine = layout.findViewById(R.id.playerBackLine);
        playerFrontLine.setTag("playerFrontLine");
        playerFrontLine.setOnDragListener(new CombatLineDragListener(this, CombatLine.PLAYER_FRONT_LINE));
        playerBackLineList = new ArrayList<>();
        playerBackLineCombatCardStateViewAdapter = new CombatCardStateViewAdapter(playerBackLineList, false, this);
        playerBackLine.setAdapter(playerBackLineCombatCardStateViewAdapter);
        LinearLayoutManager playerBackLineLayoutManager = new LinearLayoutManager(mainActivity);
        playerBackLineLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        playerBackLine.setLayoutManager(playerBackLineLayoutManager);
        playerBackLine.setTag("playerBackLine");
        playerBackLine.setOnDragListener(new CombatLineDragListener(this,CombatLine.PLAYER_BACK_LINE));
        playerHand = layout.findViewById(R.id.playerHand);
        playerHandList = new ArrayList<>();
        playerHandCombatCardStateViewAdapter = new CombatCardStateViewAdapter(playerHandList, true, this);
        playerHand.setAdapter(playerHandCombatCardStateViewAdapter);
        LinearLayoutManager playerHandLayoutManager = new LinearLayoutManager(mainActivity);
        playerHandLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        playerHand.setLayoutManager(playerHandLayoutManager);

        endPlayerTurnButton = layout.findViewById(R.id.end_turn_button);
        endPlayerTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endPlayerTurnButton.setClickable(false);
                endPlayerTurnButton.setText(getString(R.string.enemy_turn));
                mainActivity.getCombatService().endPlayerTurn();
            }
        });

        detailedCardView = layout.findViewById(R.id.detailed_card);
        detailedCardName = layout.findViewById(R.id.detailed_card_name);
        detailedCardHealth = layout.findViewById(R.id.detailedcard_health);
        detailedCardDescription = layout.findViewById(R.id.detailed_card_description);
        detailedAbilitiesRecycler = layout.findViewById(R.id.detailed_ability_recycler_view);
        detailedCardView.setVisibility(View.GONE);

        Button closeDetailViewButton = layout.findViewById(R.id.close_detailed_card_button);
        closeDetailViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailedCardView.setVisibility(View.GONE);
            }
        });

        mainActivity.getCombatService().initializeCombat();
        return layout;
    }

    public void reportLineDrop(CombatCardModel combatCardModel, CombatLine combatLine){
        mainActivity.getCombatService().cardDroppedOnLine(combatCardModel,combatLine);
    }

    public void reportCardDrop(CombatCardModel droppedCard, CombatCardModel targetCard){
        mainActivity.getCombatService().cardDroppedOnCard(droppedCard, targetCard);
    }

    @Override
    public void publishEvent(Event event) {
        if (event.getEventType().equals(EventType.DECK_FETCH_EVENT)) {
            Deck userDeck = ((DeckFetchEvent)event).getDeck();
            mainActivity.getCombatService().deckFetchReturned(userDeck);
        } else if(event.getEventType().equals(EventType.ENCOUNTER_FETCH_EVENT)){
            Encounter encounter = ((EncounterFetchEvent)event).getEncounter();
            mainActivity.getCombatService().encounterFetchReturned(encounter);
        }else if(event.getEventType().equals(EventType.ENEMY_TURN_COMPLETE_EVENT)){
            endPlayerTurnButton.setClickable(true);
            endPlayerTurnButton.setText(getString(R.string.end_turn));
        }else if(event.getEventType().equals(EventType.COMBAT_STATE_UPDATE_EVENT)){
            CombatStateUpdateEvent combatStateUpdateEvent = (CombatStateUpdateEvent) event;
            CombatStateModel combatStateModel = combatStateUpdateEvent.getCombatStateModel();
            playerHandList.clear();
            playerHandList.addAll(combatStateModel.getPlayerHand());
            playerHandCombatCardStateViewAdapter.notifyDataSetChanged();

            playerBackLineList.clear();
            playerBackLineList.addAll(combatStateModel.getPlayerBackLine());
            playerBackLineCombatCardStateViewAdapter.notifyDataSetChanged();

            playerFrontLineList.clear();
            playerFrontLineList.addAll(combatStateModel.getPlayerFrontLine());
            playerFrontLineCombatCardStateViewAdapter.notifyDataSetChanged();

            enemyFrontLineList.clear();
            enemyFrontLineList.addAll(combatStateModel.getEnemyFrontLine());
            enemyFrontLineCombatCardStateViewAdapter.notifyDataSetChanged();

            enemyBackLineList.clear();
            enemyBackLineList.addAll(combatStateModel.getEnemyBackLine());
            enemyBackLineCombatCardStateViewAdapter.notifyDataSetChanged();

            enemyHandList.clear();
            enemyHandList.addAll(combatStateModel.getEnemyHand());
            enemyHandCombatCardStateViewAdapter.notifyDataSetChanged();
        }
    }

    public boolean isPlayerTurn(){
        return mainActivity.getCombatService().isPlayerTurn();
    }

    public void cardHeld(CombatCardModel cardHeld){
        Log.e("CombatFragment", cardHeld.getCardName() + " has been held");
        detailedCardName.setText(cardHeld.getCardName());
        if(cardHeld.getCardType().equals(CardType.CHARACTER)){
            detailedCardHealth.setText(cardHeld.getCurrentHealth() + "/" + cardHeld.getMaxHealth());
        } else {
            detailedCardHealth.setText("");
        }
        detailedCardDescription.setText(cardHeld.getCardDescription());
        detailedAbilitiesRecycler.setAdapter(new AbilityViewAdapter(cardHeld,this, true));
        LinearLayoutManager abilitiesViewLayoutManager = new LinearLayoutManager(mainActivity);
        abilitiesViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        detailedAbilitiesRecycler.setLayoutManager(abilitiesViewLayoutManager);
        detailedCardView.setVisibility(View.VISIBLE);
    }

    public void abilityUsed(Ability ability){

    }

    public boolean isInitialSetup(){
        return mainActivity.getCombatService().isInSetup();
    }
}
