package com.zblouse.fantasyfitness.combat;

import java.util.ArrayList;
import java.util.List;

public class CombatStateModel {

    private CombatDeckModel userDeck;
    private CombatDeckModel enemyDeck;
    private List<CombatCardModel> enemyBackLine;
    private List<CombatCardModel> enemyFrontLine;
    private List<CombatCardModel> playerFrontLine;
    private List<CombatCardModel> playerBackLine;
    private List<CombatCardModel> playerHand;
    private List<CombatCardModel> enemyHand;

    public CombatStateModel(CombatDeckModel userDeck, CombatDeckModel enemyDeck, List<CombatCardModel> playerHand, List<CombatCardModel> enemyHand){
        this.userDeck = userDeck;
        this.enemyDeck = enemyDeck;
        this.playerHand = playerHand;
        this.enemyHand = enemyHand;
        enemyBackLine = new ArrayList<>();
        enemyFrontLine = new ArrayList<>();
        playerFrontLine = new ArrayList<>();
        playerBackLine = new ArrayList<>();
    }

    public List<CombatCardModel> getEnemyBackLine(){
        return this.enemyBackLine;
    }

    public void addCardToEnemyBackLine(CombatCardModel combatCardModel){
        this.enemyBackLine.add(combatCardModel);
    }

    public List<CombatCardModel> getEnemyFrontLine(){
        return this.enemyFrontLine;
    }

    public void addCardToEnemyFrontLine(CombatCardModel combatCardModel){
        this.enemyFrontLine.add(combatCardModel);
    }

    public List<CombatCardModel> getPlayerFrontLine(){
        return this.playerFrontLine;
    }

    public void addCardToPlayerFrontLine(CombatCardModel combatCardModel){
        this.playerFrontLine.add(combatCardModel);
    }

    public List<CombatCardModel> getPlayerBackLine(){
        return this.playerBackLine;
    }

    public void addCardToPlayerBackLine(CombatCardModel combatCardModel){
        this.playerBackLine.add(combatCardModel);
    }

    public List<CombatCardModel> getPlayerHand(){
        return this.playerHand;
    }

    public void addCardToPlayerHand(CombatCardModel combatCardModel){
        this.playerHand.add(combatCardModel);
    }

    public List<CombatCardModel> getEnemyHand(){
        return this.enemyHand;
    }

    public void addCardToEnemyHand(CombatCardModel combatCardModel){
        this.enemyHand.add(combatCardModel);
    }

    public CombatDeckModel getUserDeck(){
        return this.userDeck;
    }

    public CombatDeckModel getEnemyDeck(){
        return this.enemyDeck;
    }
}
