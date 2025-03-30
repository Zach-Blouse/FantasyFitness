package com.zblouse.fantasyfitness.dialog;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.Map;

public class DialogService implements DomainService<Dialog> {

    public static final String EMPTY_DIALOG_INIT = "emptyDialog";
    public static final String HERMIT_DIALOG_INIT = "hermitInit";
    private final DialogRepository dialogRepository;
    private final MainActivity mainActivity;

    public DialogService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.dialogRepository = new DialogRepository(mainActivity);
    }

    public Dialog fetchDialogOption(String referenceId){
        return dialogRepository.readDialog(referenceId);
    }

    public void selectDialogOption(String referenceId, Map<String, Object> metadata){
        mainActivity.publishEvent(new DialogSelectedEvent(dialogRepository.readDialog(referenceId),metadata));
    }

    @Override
    public void repositoryResponse(Dialog responseBody, Map<String, Object> metadata) {

    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }

    public void initializeDialogs(){
        Dialog emptyDialog = new Dialog(EMPTY_DIALOG_INIT, "You spot a particularly uninteresting bird.","empty");
        dialogRepository.writeDialog(emptyDialog);
        Dialog initialHermitDialog = new Dialog(HERMIT_DIALOG_INIT,"You come upon an old hermit. He is clad in dirty pelts.","initial");
        initialHermitDialog.setDialogOption1("hermit1");
        initialHermitDialog.setDialogOption2("hermit2");
        initialHermitDialog.setDialogOption3("hermit3");
        dialogRepository.writeDialog(initialHermitDialog);
        Dialog hermitOption1 = new Dialog("hermit1", "He looks up at you and says, \"Hello Adventurer. You remind me of myself when I was your age. Want some free advice?\"","Hello Sir");
        hermitOption1.setDialogOption1("hermit1_1");
        hermitOption1.setDialogOption2("hermit1_2");
        dialogRepository.writeDialog(hermitOption1);
        Dialog hermitOption1_1 = new Dialog("hermit1_1", "Stay away from the Valley of Monsters, the monsters there are far too powerful. That's how I lost my whole adventuring party.\" The hermit gets a distance look in his eye and refuses to speak more.","Of course sir");
        dialogRepository.writeDialog(hermitOption1_1);
        Dialog hermitOption1_2 = new Dialog("hermit1_2", "Well then. You are free to make your own mistakes.","\"I don't think I want advice from a hermit.\"");
        dialogRepository.writeDialog(hermitOption1_2);
        Dialog hermitOption2 = new Dialog("hermit2","\"Well, keep on passing then.\"","I'm just passing through");
        dialogRepository.writeDialog(hermitOption2);
        Dialog hermitOption3 = new Dialog("hermit3", "The old hermit scowls at you and says \"You are in your own way. Begone.\"","Get out of my way old man");
        dialogRepository.writeDialog(hermitOption3);
    }
}
