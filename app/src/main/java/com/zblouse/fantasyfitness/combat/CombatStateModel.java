package com.zblouse.fantasyfitness.combat;

import com.zblouse.fantasyfitness.actions.CombatActionResult;

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

    public CombatStateModel(){
        this.playerHand = new ArrayList<>();
        this.enemyHand = new ArrayList<>();
        this.enemyBackLine = new ArrayList<>();
        this.enemyFrontLine = new ArrayList<>();
        this.playerFrontLine = new ArrayList<>();
        this.playerBackLine = new ArrayList<>();
    }

    public CombatStateModel(CombatDeckModel userDeck, CombatDeckModel enemyDeck, List<CombatCardModel> playerHand, List<CombatCardModel> playerBackLine,
                            List<CombatCardModel> playerFrontLine, List<CombatCardModel> enemyHand, List<CombatCardModel> enemyFrontLine, List<CombatCardModel> enemyBackLine){
        this.userDeck = userDeck;
        this.enemyDeck = enemyDeck;
        this.playerHand = playerHand;
        this.enemyHand = enemyHand;
        this.enemyBackLine = enemyBackLine;
        this.enemyFrontLine = enemyFrontLine;
        this.playerFrontLine = playerFrontLine;
        this.playerBackLine = playerBackLine;
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

    public void initialPlayerHand(List<CombatCardModel> playerHand){
        this.playerHand = playerHand;
    }

    public List<CombatCardModel> getEnemyHand(){
        return this.enemyHand;
    }

    public void addCardToEnemyHand(CombatCardModel combatCardModel){
        this.enemyHand.add(combatCardModel);
    }

    public void initialEnemyHand(List<CombatCardModel> enemyHand){
        this.enemyHand = enemyHand;
    }

    public CombatDeckModel getPlayerDeck(){
        return this.userDeck;
    }

    public void setUserDeck(CombatDeckModel userDeck){
        this.userDeck = userDeck;
    }

    public CombatDeckModel getEnemyDeck(){
        return this.enemyDeck;
    }

    public void setEnemyDeck(CombatDeckModel enemyDeck){
        this.enemyDeck = enemyDeck;
    }

    public boolean fullyInitialized(){
        return userDeck != null && enemyDeck != null;
    }
}
