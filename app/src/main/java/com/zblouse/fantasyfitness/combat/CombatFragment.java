package com.zblouse.fantasyfitness.combat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.Deck;
import com.zblouse.fantasyfitness.combat.cards.DeckFetchEvent;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.ArrayList;
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

    public CombatFragment(){
        super(R.layout.combat_fragment);
    }

    public CombatFragment(MainActivity mainActivity){
        super(R.layout.combat_fragment, mainActivity);
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
        } else if(event.getEventType().equals(EventType.COMBAT_STATE_UPDATE_EVENT)){
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
}
