package com.zblouse.fantasyfitness.dialog;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class BaseDialogFetchEvent extends Event {

    private Dialog dialog;

    public BaseDialogFetchEvent(Dialog dialog, Map<String, Object> metadata){
        super(EventType.BASE_DIALOG_FETCH_EVENT, metadata);
        this.dialog = dialog;
    }

    public Dialog getDialog(){
        return this.dialog;
    }
}
