package com.zblouse.fantasyfitness.dialog;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.HashMap;
import java.util.Map;

public class DialogFetchEvent extends Event {

    private final Dialog dialogOption1;
    private final Dialog dialogOption2;
    private final Dialog dialogOption3;
    private final Dialog dialogOption4;

    public DialogFetchEvent(Dialog dialog1, Dialog dialog2, Dialog dialog3, Dialog dialog4, Map<String, Object> metadata){
        super(EventType.DIALOG_FETCH_EVENT, metadata);
        this.dialogOption1 = dialog1;
        this.dialogOption2 = dialog2;
        this.dialogOption3 = dialog3;
        this.dialogOption4 = dialog4;
    }

    public Dialog getDialogOption1(){
        return this.dialogOption1;
    }

    public Dialog getDialogOption2(){
        return this.dialogOption2;
    }

    public Dialog getDialogOption3(){
        return this.dialogOption3;
    }

    public Dialog getDialogOption4(){
        return this.dialogOption4;
    }
}
