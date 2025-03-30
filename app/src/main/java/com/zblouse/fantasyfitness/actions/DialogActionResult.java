package com.zblouse.fantasyfitness.actions;

import com.zblouse.fantasyfitness.dialog.Dialog;

public class DialogActionResult extends ActionResult {

    private final Dialog initialDialog;

    public DialogActionResult(Dialog initialDialog) {
        super(ActionResultType.DIALOG);
        this.initialDialog = initialDialog;
    }

    public Dialog getInitialDialog(){
        return this.initialDialog;
    }
}
