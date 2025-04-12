package com.zblouse.fantasyfitness.dialog;

public class DialogAffect {

    private final DialogAffectType dialogAffectType;
    private String questUuid;
    private String questObjectiveUuid;

    public DialogAffect(DialogAffectType dialogAffectType){
        this.dialogAffectType = dialogAffectType;
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
}
