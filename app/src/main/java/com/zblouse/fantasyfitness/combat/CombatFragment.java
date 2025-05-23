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
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
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
import com.zblouse.fantasyfitness.home.UserHomeFragment;
import com.zblouse.fantasyfitness.workout.WorkoutRecordsFragment;

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

    private CardView victoryView;
    private TextView victoryText;

    private String combatGameLocation;
    private int combatGameBuilding;

    public CombatFragment(){
        super(R.layout.combat_fragment);
    }

    public CombatFragment(MainActivity mainActivity, String encounter, String combatLocation, int combatBuildingId){
        super(R.layout.combat_fragment, mainActivity);
        mainActivity.getEncounterService().fetchEncounter(encounter, new HashMap<>());
        this.combatGameBuilding = combatBuildingId;
        this.combatGameLocation = combatLocation;
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
                setupComplete();
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

        victoryView = layout.findViewById(R.id.victory_screen);
        victoryText = layout.findViewById(R.id.victory_screen_text);
        Button closeVictoryButton = layout.findViewById(R.id.close_combat_button);
        closeVictoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new UserHomeFragment(mainActivity)).commit();
            }
        });
        victoryView.setVisibility(View.GONE);

        mainActivity.getCombatService().initializeCombat(combatGameLocation, combatGameBuilding);
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
            startPlayerTurn();
        }else if(event.getEventType().equals(EventType.COMBAT_STATE_UPDATE_EVENT)){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
            });
        } else if(event.getEventType().equals(EventType.PLAYER_VICTORY_EVENT)){
            playerWins();
        } else if(event.getEventType().equals(EventType.ENEMY_VICTORY_EVENT)){
            enemyWins();
        }
    }

    public boolean isPlayerTurn(){
        return mainActivity.getCombatService().isPlayerTurn();
    }

    public void cardHeld(CombatCardModel cardHeld){
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

    public void abilityUsed(CombatCardModel cardUsingAbility, Ability ability){
        detailedCardView.setVisibility(View.GONE);
        mainActivity.getCombatService().abilityUsed(cardUsingAbility, ability);
    }

    public void attemptCardAbilityTarget(CombatCardModel combatCardModel){
        mainActivity.getCombatService().attemptCardAbilityTarget(combatCardModel);
    }

    public boolean isWaitingForAbilityTargeting(){
        return mainActivity.getCombatService().isWaitingForAbilityTargeting();
    }

    public boolean isInitialSetup(){
        return mainActivity.getCombatService().isInSetup();
    }

    private void setupComplete(){
        boolean playerTurn = mainActivity.getCombatService().endSetup();
        if(playerTurn){
            startPlayerTurn();
        } else {
            endPlayerTurnButton.setClickable(false);
            endPlayerTurnButton.setText(mainActivity.getString(R.string.enemy_turn));
        }
    }

    public void startPlayerTurn(){
        endPlayerTurnButton.setClickable(true);
        endPlayerTurnButton.setText(mainActivity.getString(R.string.end_turn));
        endPlayerTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endPlayerTurn();
            }
        });
    }

    private void endPlayerTurn(){
        endPlayerTurnButton.setClickable(false);
        endPlayerTurnButton.setText(mainActivity.getString(R.string.enemy_turn));
        mainActivity.getCombatService().endPlayerTurn();
    }

    public void playerWins(){
        victoryText.setText("VICTORY");
        victoryView.setVisibility(View.VISIBLE);
    }

    public void enemyWins(){
        victoryText.setText("DEFEAT");
        victoryView.setVisibility(View.VISIBLE);
    }

    public MainActivity getMainActivity(){
        return this.mainActivity;
    }

    public boolean isCombatScreenCovered(){
        return (victoryView.getVisibility() == View.VISIBLE || detailedCardView.getVisibility() == View.VISIBLE);
    }
}
