package com.zblouse.fantasyfitness.dialog;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class DialogSelectedEvent extends Event {

    private final Dialog newDialog;

    public DialogSelectedEvent(Dialog newDialog, Map<String, Object> metadata){
        super(EventType.DIALOG_SELECTED_EVENT, metadata);
        this.newDialog = newDialog;
    }

    public Dialog getNewDialog(){
        return newDialog;
    }
}
