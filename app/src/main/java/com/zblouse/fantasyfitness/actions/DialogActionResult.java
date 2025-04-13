package com.zblouse.fantasyfitness.actions;

public class DialogActionResult extends ActionResult {

    private final String initialDialogReferenceId;
    private final boolean questDialog;

    public DialogActionResult(String initialDialogReferenceId, boolean questDialog) {
        super(ActionResultType.DIALOG);
        this.initialDialogReferenceId = initialDialogReferenceId;
        this.questDialog = questDialog;
    }

    public String getInitialDialogReferenceId(){
        return this.initialDialogReferenceId;
    }

    public boolean isQuestDialog(){
        return this.questDialog;
    }
}
