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
        enemyHandCombatCardStateViewAdapter = new CombatCardStateViewAdapter(enemyHandList, true);
        enemyHand.setAdapter(enemyHandCombatCardStateViewAdapter);
        LinearLayoutManager enemyHandLayoutManager = new LinearLayoutManager(mainActivity);
        enemyHandLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        enemyHand.setLayoutManager(enemyHandLayoutManager);
        enemyBackLine = layout.findViewById(R.id.enemyBackLine);
        enemyBackLineList = new ArrayList<>();
        enemyBackLineCombatCardStateViewAdapter = new CombatCardStateViewAdapter(enemyBackLineList, false);
        enemyBackLine.setAdapter(enemyBackLineCombatCardStateViewAdapter);
        LinearLayoutManager enemyBackLineLayoutManager = new LinearLayoutManager(mainActivity);
        enemyBackLineLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        enemyBackLine.setLayoutManager(enemyBackLineLayoutManager);
        enemyFrontLine = layout.findViewById(R.id.enemyFrontLine);
        enemyFrontLineList = new ArrayList<>();
        enemyFrontLineCombatCardStateViewAdapter = new CombatCardStateViewAdapter(enemyFrontLineList, false);
        enemyFrontLine.setAdapter(enemyFrontLineCombatCardStateViewAdapter);
        LinearLayoutManager enemyFrontLineLayoutManager = new LinearLayoutManager(mainActivity);
        enemyFrontLineLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        enemyFrontLine.setLayoutManager(enemyFrontLineLayoutManager);
        playerFrontLine = layout.findViewById(R.id.playerFrontLine);
        playerFrontLineList = new ArrayList<>();
        playerFrontLineCombatCardStateViewAdapter = new CombatCardStateViewAdapter(playerFrontLineList, false);
        playerFrontLine.setAdapter(playerFrontLineCombatCardStateViewAdapter);
        LinearLayoutManager playerFrontLineLayoutManager = new LinearLayoutManager(mainActivity);
        playerFrontLineLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        playerFrontLine.setLayoutManager(playerFrontLineLayoutManager);
        playerBackLine = layout.findViewById(R.id.playerBackLine);
        playerBackLineList = new ArrayList<>();
        playerBackLineCombatCardStateViewAdapter = new CombatCardStateViewAdapter(playerBackLineList, false);
        playerBackLine.setAdapter(playerBackLineCombatCardStateViewAdapter);
        LinearLayoutManager playerBackLineLayoutManager = new LinearLayoutManager(mainActivity);
        playerBackLineLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        playerBackLine.setLayoutManager(playerBackLineLayoutManager);
        playerHand = layout.findViewById(R.id.playerHand);
        playerHandList = new ArrayList<>();
        playerHandCombatCardStateViewAdapter = new CombatCardStateViewAdapter(playerHandList, true);
        playerHand.setAdapter(playerHandCombatCardStateViewAdapter);
        LinearLayoutManager playerHandLayoutManager = new LinearLayoutManager(mainActivity);
        playerHandLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        playerHand.setLayoutManager(playerHandLayoutManager);

        mainActivity.getCombatService().initializeCombat();
        return layout;
    }

    @Override
    public void publishEvent(Event event) {
        if (event.getEventType().equals(EventType.DECK_FETCH_EVENT)) {
            Deck userDeck = ((DeckFetchEvent)event).getDeck();
            mainActivity.getCombatService().deckFetchReturned(userDeck);
        } else if(event.getEventType().equals(EventType.COMBAT_STATE_UPDATE_EVENT)){
            CombatStateUpdateEvent combatStateUpdateEvent = (CombatStateUpdateEvent) event;
            playerHandList.clear();
            playerHandList.addAll(combatStateUpdateEvent.getCombatStateModel().getPlayerHand());
            for(CombatCardModel combatCardModel: playerHandList){
                Log.e("CombatFragment", "player hand card: " + combatCardModel.getCardName());
            }
            playerHandCombatCardStateViewAdapter.notifyDataSetChanged();
        }
    }
}
