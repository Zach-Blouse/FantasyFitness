package com.zblouse.fantasyfitness.dialog;

public class DialogAffect {

    private DialogAffectType dialogAffectType;
    private String questUuid;
    private String questObjectiveUuid;
    private String shopTag;

    public DialogAffect(DialogAffectType dialogAffectType){
        this.dialogAffectType = dialogAffectType;
    }

    public DialogAffect(){

    }

    public DialogAffectType getDialogAffectType(){
        return this.dialogAffectType;
    }

    public String getQuestUuid(){
        return this.questUuid;
    }

    public void setQuestUuid(String questUuid){
        this.questUuid = questUuid;
    }

    public String getQuestObjectiveUuid(){
        return this.questObjectiveUuid;
    }

    public void setQuestObjectiveUuid(String questObjectiveUuid){
        this.questObjectiveUuid = questObjectiveUuid;
    }

    public String getShopTag(){
        return this.shopTag;
    }

    public void setShopTag(String shopTag){
        this.shopTag = shopTag;
    }
}
