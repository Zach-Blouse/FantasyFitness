package com.zblouse.fantasyfitness.dialog;

import com.zblouse.fantasyfitness.quest.Quest;

import java.io.Serializable;

public class Dialog implements Serializable {

    private Integer id;
    private String referenceId;
    private String optionText;
    private String flavorText;
    private String dialogOption1;
    private String dialogOption2;
    private String dialogOption3;
    private String dialogOption4;
    private DialogAffect dialogAffect;
    private boolean questDialog;

    public Dialog(String referenceId, String flavorText, String optionText, DialogAffect dialogAffect, boolean questDialog){
        this.referenceId = referenceId;
        this.flavorText = flavorText;
        this.optionText = optionText;
        this.dialogAffect = dialogAffect;
        this.questDialog = questDialog;
    }

    public Dialog(Integer id, String referenceId, String optionText, String flavorText, String dialogOption1, String dialogOption2, String dialogOption3, String dialogOption4, DialogAffect dialogAffect, boolean questDialog){
        this.id = id;
        this.referenceId = referenceId;
        this.optionText = optionText;
        this.flavorText = flavorText;
        this.dialogOption1 = dialogOption1;
        this.dialogOption2 = dialogOption2;
        this.dialogOption3 = dialogOption3;
        this.dialogOption4 = dialogOption4;
        this.dialogAffect = dialogAffect;
        this.questDialog = questDialog;
    }

    public Dialog(){}

    public Integer getId(){
        return this.id;
    }

    public String getReferenceId(){
        return this.referenceId;
    }

    public String getOptionText(){
        return this.optionText;
    }

    public String getFlavorText(){
        return this.flavorText;
    }

    public String getDialogOption1(){
        return this.dialogOption1;
    }

    public void setDialogOption1(String dialogOption1){
        this.dialogOption1 = dialogOption1;
    }

    public String getDialogOption2(){
        return this.dialogOption2;
    }

    public void setDialogOption2(String dialogOption2){
        this.dialogOption2 = dialogOption2;
    }

    public String getDialogOption3(){
        return this.dialogOption3;
    }

    public void setDialogOption3(String dialogOption3){
        this.dialogOption3 = dialogOption3;
    }

    public String getDialogOption4(){
        return this.dialogOption4;
    }

    public void setDialogOption4(String dialogOption4){
        this.dialogOption4 = dialogOption4;
    }

    public DialogAffect getDialogAffect(){
        return dialogAffect;
    }

    public boolean isQuestDialog(){
        return this.questDialog;
    }
}
