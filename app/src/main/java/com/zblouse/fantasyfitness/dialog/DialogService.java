package com.zblouse.fantasyfitness.dialog;

import android.util.Log;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.merchant.MerchantDisplayEvent;
import com.zblouse.fantasyfitness.quest.Quest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DialogService implements DomainService<Dialog> {

    public static final String EMPTY_DIALOG_INIT = "emptyDialog";
    public static final String HERMIT_DIALOG_INIT = "hermitInit";
    public static final String INNKEEPER_DIALOG_INIT = "innKeeperDialogInit";
    public static final String FAOLYN_GENERAL_STORE_DIALOG_INIT = "faolynGeneralStoreDialogInit";
    public static final String BRIDGETON_GENERAL_STORE_DIALOG_INIT = "bridgetonGeneralStoreDialogInit";
    public static final String THANADEL_GENERAL_STORE_DIALOG_INIT = "thanadelGeneralStoreDialogInit";
    public static final String FAOLYN_BLACKSMITH_DIALOG_INIT = "faolynBlacksmithDialogInit";
    public static final String BRIDGETON_BLACKSMITH_DIALOG_INIT = "bridgetonBlacksmithDialogInit";
    private final DialogRepository dialogRepository;
    private final MainActivity mainActivity;

    public DialogService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.dialogRepository = new DialogRepository(this, mainActivity);
    }

    public DialogService(MainActivity mainActivity, DialogRepository dialogRepository){
        this.mainActivity = mainActivity;
        this.dialogRepository = dialogRepository;
    }

    public void fetchDialogOptions(Dialog baseDialog, Map<String, Object> metadata){

        String referenceId1 = baseDialog.getDialogOption1();
        String referenceId2 = baseDialog.getDialogOption2();
        String referenceId3 = baseDialog.getDialogOption3();
        String referenceId4 = baseDialog.getDialogOption4();

        Log.e("DialogService", "Fetching Dialogs for dialog " + baseDialog.getReferenceId()  + " isQuestDialog: " + baseDialog.isQuestDialog() + " baseDialogType: " + baseDialog.getDialogAffect().getDialogAffectType());

        if(baseDialog.isQuestDialog() || baseDialog.getDialogAffect().getDialogAffectType().equals(DialogAffectType.QUEST_GENERATE)){
            dialogRepository.readQuestDialogs(mainActivity.getCurrentUser().getUid(),referenceId1, referenceId2, referenceId3, referenceId4, metadata);
        } else {
            Dialog dialog1 = null;
            Dialog dialog2 = null;
            Dialog dialog3 = null;
            Dialog dialog4 = null;
            if(referenceId1 != null){
                dialog1 = dialogRepository.readDialog(referenceId1);
            }
            if(referenceId2 != null){
                dialog2 = dialogRepository.readDialog(referenceId2);
            }
            if(referenceId3 != null){
                dialog3 = dialogRepository.readDialog(referenceId3);
            }
            if(referenceId4 != null){
                dialog4 = dialogRepository.readDialog(referenceId4);
            }

            mainActivity.publishEvent(new DialogFetchEvent(dialog1, dialog2, dialog3, dialog4, metadata));
        }
    }

    public void selectDialogOption(Dialog dialogSelected, String currentLocation, Map<String, Object> metadata){
        if(dialogSelected.getDialogAffect().getDialogAffectType().equals(DialogAffectType.QUEST_GENERATE)){
            List<Quest> questsGenerated = mainActivity.getQuestService().generateQuests(currentLocation, 3);
            for(Quest quest: questsGenerated){
                DialogAffect questStartDialogAffect = new DialogAffect(DialogAffectType.QUEST_START);
                questStartDialogAffect.setQuestUuid(quest.getQuestUuid());
                Dialog questSelectDialog = new Dialog(UUID.randomUUID().toString(),"Good luck on your quest.",quest.getQuestDescription(),questStartDialogAffect, true);
                dialogRepository.writeQuestDialog(questSelectDialog,mainActivity.getCurrentUser().getUid(),new HashMap<>());
                if(dialogSelected.getDialogOption1() == null){
                    dialogSelected.setDialogOption1(questSelectDialog.getReferenceId());
                } else if(dialogSelected.getDialogOption2() == null){
                    dialogSelected.setDialogOption2(questSelectDialog.getReferenceId());
                } else if(dialogSelected.getDialogOption3() == null){
                    dialogSelected.setDialogOption3(questSelectDialog.getReferenceId());
                } else if(dialogSelected.getDialogOption4() == null){
                    dialogSelected.setDialogOption4(questSelectDialog.getReferenceId());
                }
            }

        } else if(dialogSelected.getDialogAffect().getDialogAffectType().equals(DialogAffectType.QUEST_START)){
            mainActivity.getQuestService().startQuest(dialogSelected.getDialogAffect().getQuestUuid());
            cleanUpQuestStartDialogs();
        }else if(dialogSelected.getDialogAffect().getDialogAffectType().equals(DialogAffectType.QUEST_GOAL)){
            mainActivity.getQuestService().dialogQuestObjectiveCompleted(dialogSelected.getDialogAffect().getQuestUuid(), dialogSelected.getDialogAffect().getQuestObjectiveUuid());
        }else if(dialogSelected.getDialogAffect().getDialogAffectType().equals(DialogAffectType.SHOP_OPEN)){
            mainActivity.publishEvent(new MerchantDisplayEvent(mainActivity.getMerchantService().getMerchantByTag(dialogSelected.getDialogAffect().getShopTag())));
            return;
        }
        mainActivity.publishEvent(new DialogSelectedEvent(dialogSelected, metadata));
    }

    public void fetchBaseDialog(String dialogReferenceId, boolean isQuestDialog, Map<String, Object> metadata){
        if(isQuestDialog){
            dialogRepository.readQuestDialog(mainActivity.getCurrentUser().getUid(),dialogReferenceId,metadata);
        } else {
            mainActivity.publishEvent(new BaseDialogFetchEvent(dialogRepository.readDialog(dialogReferenceId),metadata));
        }
    }

    public void writeDialog(Dialog dialog){
        if(dialog.isQuestDialog()){
            dialogRepository.writeQuestDialog(dialog,mainActivity.getCurrentUser().getUid(),new HashMap<>());
        } else {
            dialogRepository.writeDialog(dialog);
        }
    }

    public boolean hasBeenInitialized(){
        return dialogRepository.databaseInitialized();
    }

    public void repositoryListResponse(List<Dialog> dialogs, Map<String, Object> metadata){
        Dialog dialog1 = null;
        Dialog dialog2 = null;
        Dialog dialog3 = null;
        Dialog dialog4 = null;
        if(dialogs.size() > 0){
            dialog1 = dialogs.get(0);
        }
        if(dialogs.size() > 1){
            dialog2 = dialogs.get(1);
        }
        if(dialogs.size() > 2){
            dialog3 = dialogs.get(2);
        }
        if(dialogs.size() > 3){
            dialog4 = dialogs.get(3);
        }
        mainActivity.publishEvent(new DialogFetchEvent(dialog1, dialog2, dialog3, dialog4, metadata));
    }

    @Override
    public void repositoryResponse(Dialog responseBody, Map<String, Object> metadata) {
        Log.e("DialogService","repositoryResponse");
        mainActivity.publishEvent(new BaseDialogFetchEvent(responseBody,metadata));
    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }

    public void initializeDialogs(){
        Dialog emptyDialog = new Dialog(EMPTY_DIALOG_INIT, "You spot a particularly uninteresting bird.","empty", new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(emptyDialog);
        Dialog initialHermitDialog = new Dialog(HERMIT_DIALOG_INIT,"You come upon an old hermit. He is clad in dirty pelts.","initial", new DialogAffect(DialogAffectType.NONE), false);
        initialHermitDialog.setDialogOption1("hermit1");
        initialHermitDialog.setDialogOption2("hermit2");
        initialHermitDialog.setDialogOption3("hermit3");
        dialogRepository.writeDialog(initialHermitDialog);
        Dialog hermitOption1 = new Dialog("hermit1", "He looks up at you and says, \"Hello Adventurer. You remind me of myself when I was your age. Want some free advice?\"","Hello Sir", new DialogAffect(DialogAffectType.NONE), false);
        hermitOption1.setDialogOption1("hermit1_1");
        hermitOption1.setDialogOption2("hermit1_2");
        dialogRepository.writeDialog(hermitOption1);
        Dialog hermitOption1_1 = new Dialog("hermit1_1", "Stay away from the Valley of Monsters, the monsters there are far too powerful. That's how I lost my whole adventuring party.\" The hermit gets a distant look in his eye and refuses to speak more.","Of course sir", new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(hermitOption1_1);
        Dialog hermitOption1_2 = new Dialog("hermit1_2", "Well then. You are free to make your own mistakes.","I don't think I want advice from a hermit.", new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(hermitOption1_2);
        Dialog hermitOption2 = new Dialog("hermit2","\"Well, keep on passing then.\"","I'm just passing through", new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(hermitOption2);
        Dialog hermitOption3 = new Dialog("hermit3", "The old hermit scowls at you and says \"You are in your own way. Begone.\"","Get out of my way old man", new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(hermitOption3);

        Dialog innKeeperDialogInit = new Dialog(INNKEEPER_DIALOG_INIT, "The inn is cozy inside. A nice fire is going and the air smells of warm spice drinks. The innkeeper is cleaning a mub behind the bar and a stranger in a long black cloak is sitting alone at a table by the window.","none",new DialogAffect(DialogAffectType.NONE), false);
        innKeeperDialogInit.setDialogOption1("innkeeper1");
        innKeeperDialogInit.setDialogOption2("innkeeper2");
        innKeeperDialogInit.setDialogOption3("innkeeper3");
        dialogRepository.writeDialog(innKeeperDialogInit);

        Dialog innkeeperOption1 = new Dialog("innkeeper1","The innkeeper continues polishing the mug she is cleaning but smiles and says \"Hello fair traveler, how can I help you?\"","Walk up to the innkeeper behind the bar.", new DialogAffect(DialogAffectType.NONE), false);
        innkeeperOption1.setDialogOption1("innkeeper1_1");
        innkeeperOption1.setDialogOption2("innkeeper1_2");
        innkeeperOption1.setDialogOption3("innkeeper1_3");
        dialogRepository.writeDialog(innkeeperOption1);
        Dialog innkeeperOption1_1 = new Dialog("innkeeper1_1","\"Feel free to sit and rest your feet. Let me know whenever you need anything\"","I am just taking a break from my travels",new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(innkeeperOption1_1);

        Dialog innkeeperOption1_2 = new Dialog("innkeeper1_2","\"Of course honey, my only other guest tonight is that traveller over there. She points to the mysterious hooded figure. Your room is the first one on the right down that hall. Have a good night's sleep\"","Can I get a room for the night?",new DialogAffect(DialogAffectType.NONE), false);
        innkeeperOption1_2.setDialogOption1("innkeeper1_2_1");
        innkeeperOption1_2.setDialogOption2("innkeeper1_2_2");
        dialogRepository.writeDialog(innkeeperOption1_2);

        Dialog innkeeperOption1_2_1 = new Dialog("innkeeper1_2_1", "The room is simple, but clean and comfortable looking.","Walk to your room",new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(innkeeperOption1_2_1);

        Dialog innkeeperOption1_2_2 = new Dialog("innkeeper1_2_2", "\"I make it my policy to never pry into my guests' business. You'd have to ask them\"", "\"What is going on with that hooded person over there?\"",new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(innkeeperOption1_2_2);

        Dialog innkeeperOption1_3 = new Dialog("innkeeper1_3", "These are the jobs I have at the moment", "Are there any quests on the job board?", new DialogAffect(DialogAffectType.QUEST_GENERATE), false);
        dialogRepository.writeDialog(innkeeperOption1_3);

        Dialog innkeeperOption2 = new Dialog("innkeeper2", "It feels nice to finally rest your weary legs. You feel yourself drift off to a well deserved sleep.","Take a seat at a table",new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(innkeeperOption2);

        Dialog innkeeperOption3 = new Dialog("innkeeper3", "The figure's cloak is up over their head, it is so shadowed inside you cannot see their face. Their head shifts slightly to look at you as you approach.","Walk over to the mysterious stranger", new DialogAffect(DialogAffectType.NONE), false);
        innkeeperOption3.setDialogOption1("innkeeper3_1");
        innkeeperOption3.setDialogOption2("innkeeper3_2");
        dialogRepository.writeDialog(innkeeperOption3);

        Dialog innkeeperOption3_1 = new Dialog("innkeeper3_1", "\"Leave\" the figure says in a much softer voice than you expected.", "Sit down at the same table as the figure.", new DialogAffect(DialogAffectType.NONE), false);
        innkeeperOption3_1.setDialogOption1("innkeeper3_1_1");
        innkeeperOption3_1.setDialogOption2("innkeeper3_1_2");
        dialogRepository.writeDialog(innkeeperOption3_1);

        Dialog innkeeperOption3_1_1 = new Dialog("innkeeper3_1_1", "You have a nice relaxing evening in the tavern. Eventually the stranger gets up and hobbles oddly over to their room.","Respect the strangers wishes and sit somewhere else.", new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(innkeeperOption3_1_1);

        Dialog innkeeperOption3_1_2 = new Dialog("innkeeper3_1_2", "\"I ... um... are a normal drinker of cider in this inn. Nothing for you to be curious about", "\"What's your deal?\"", new DialogAffect(DialogAffectType.NONE), false);
        innkeeperOption3_1_2.setDialogOption1("innkeeper3_1_2_1");
        innkeeperOption3_1_2.setDialogOption2("innkeeper3_1_2_2");
        dialogRepository.writeDialog(innkeeperOption3_1_2);

        Dialog innkeeperOption3_1_2_1 = new Dialog("innkeeper3_1_2_1","As you rip off the stranger's hood, you see a small cute reptilian face looking back at you in fear. The kobold then jumps up onto the table and crashes through the window. Two other kobolds quickly climb up out of the cloak and follow. The innkeeper shouts over, \"Again! You people need to stop scaring them, they drink enough for three but only take up one seat.\"", "Push the stranger's hood back",new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(innkeeperOption3_1_2_1);
        Dialog innkeeperOption3_1_2_2 = new Dialog("innkeeper3_1_2_2","The stranger faces you warily until you leave their table.", "\"Fine keep your secrets\"",new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(innkeeperOption3_1_2_2);

        Dialog innkeeperDialog3_2 = new Dialog("innkeeper3_2","The hooded figure gives you a small nod, then goes back to their drink.","Introduce yourself", new DialogAffect(DialogAffectType.NONE), false);
        innkeeperDialog3_2.setDialogOption1("innkeeper3_2_1");
        innkeeperDialog3_2.setDialogOption2("innkeeper3_2_2");
        dialogRepository.writeDialog(innkeeperDialog3_2);

        Dialog innkeeperDialog3_2_1 = new Dialog("innkeeper3_2_1", "You find your own seat in the inn and have a relaxing evening.", "Leave the stranger alone to their drink.", new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(innkeeperDialog3_2_1);

        Dialog innkeeperDialog3_2_2 = new Dialog("innkeeper3_2_2", "The stranger huddles down as though afraid. \"Hey!\" the innkeeper shouts over. \"Leave them alone. They clearly don't want to be bothered.\"","\"I'm talking to you. It's rude to not respond.\"", new DialogAffect(DialogAffectType.NONE), false);
        innkeeperDialog3_2_2.setDialogOption1("innkeeperDialog3_2_2_1");
        innkeeperDialog3_2_2.setDialogOption2("innkeeperDialog3_2_1");
        dialogRepository.writeDialog(innkeeperDialog3_2_2);

        Dialog innkeeperDialog3_2_2_1 = new Dialog("innkeeper3_2_2_1", "The cloak flies back as three kobolds burst out of it and run out of the inn. The innkeeper says to you, \"Dammit, they were my best customers. You get out of my inn too! You aren't allowed to bother my patrons.\"","\"No they need to show me who they are.\"", new DialogAffect(DialogAffectType.NONE), false);
        dialogRepository.writeDialog(innkeeperDialog3_2_2_1);

        DialogAffect thanadelGeneralStoreAffect = new DialogAffect(DialogAffectType.SHOP_OPEN);
        thanadelGeneralStoreAffect.setShopTag(mainActivity.getString(R.string.thanadel_general_store));
        Dialog thandelGeneralStoreDialogInit = new Dialog(THANADEL_GENERAL_STORE_DIALOG_INIT,"As you walk in you see a well organized shop selling all manner of goods. The shopkeeper says, \"Hello adventurer, can I interest you in any of my wares?\"","none",new DialogAffect(DialogAffectType.NONE),false);
        thandelGeneralStoreDialogInit.setDialogOption1("thanadelGeneralStoreDialog1");
        dialogRepository.writeDialog(thandelGeneralStoreDialogInit);
        Dialog thanadelGeneralStoreDialog1 = new Dialog("thanadelGeneralStoreDialog1","Will not be displayed","\"Yes please\"",thanadelGeneralStoreAffect, false);
        dialogRepository.writeDialog(thanadelGeneralStoreDialog1);

        DialogAffect bridgetonGeneralStoreAffect = new DialogAffect(DialogAffectType.SHOP_OPEN);
        bridgetonGeneralStoreAffect.setShopTag(mainActivity.getString(R.string.faolyn_general_store));
        Dialog bridgetonGeneralStoreDialogInit = new Dialog(BRIDGETON_GENERAL_STORE_DIALOG_INIT,"As you walk in you see a well organized shop selling all manner of goods. The shopkeeper says, \"Hello adventurer, can I interest you in any of my wares?\"","none",new DialogAffect(DialogAffectType.NONE),false);
        bridgetonGeneralStoreDialogInit.setDialogOption1("bridgetonGeneralStoreDialog1");
        dialogRepository.writeDialog(bridgetonGeneralStoreDialogInit);
        Dialog bridgetonGeneralStoreDialog1 = new Dialog("bridgetonGeneralStoreDialog1","Will not be displayed","\"Yes please\"",bridgetonGeneralStoreAffect, false);
        dialogRepository.writeDialog(bridgetonGeneralStoreDialog1);

        DialogAffect faolynGeneralStoreAffect = new DialogAffect(DialogAffectType.SHOP_OPEN);
        faolynGeneralStoreAffect.setShopTag(mainActivity.getString(R.string.faolyn_general_store));
        Dialog faolynGeneralStoreDialogInit = new Dialog(FAOLYN_GENERAL_STORE_DIALOG_INIT,"As you walk in you see a well organized shop selling all manner of goods. The shopkeeper says, \"Hello adventurer, can I interest you in any of my wares?\"","none",new DialogAffect(DialogAffectType.NONE),false);
        faolynGeneralStoreDialogInit.setDialogOption1("faolynGeneralStoreDialog1");
        dialogRepository.writeDialog(faolynGeneralStoreDialogInit);
        Dialog faolynGeneralStoreDialog1 = new Dialog("faolynGeneralStoreDialog1","Will not be displayed","\"Yes please\"",faolynGeneralStoreAffect, false);
        dialogRepository.writeDialog(faolynGeneralStoreDialog1);

        DialogAffect faolynBlacksmithAffect = new DialogAffect(DialogAffectType.SHOP_OPEN);
        faolynBlacksmithAffect.setShopTag(mainActivity.getString(R.string.faolyn_blacksmith));
        Dialog faolynBlacksmithDialogInit = new Dialog(FAOLYN_BLACKSMITH_DIALOG_INIT,"As you approach the blacksmith's shop you can hear the sounds of a hammer on metal. As you walk around the side you see the burly blacksmith hammering on a lump of metal while his young apprentice holds the tongs. He finishes hammering, puts the metal back in the forge and asks, \"What do you need?\"","none",new DialogAffect(DialogAffectType.NONE),false);
        faolynBlacksmithDialogInit.setDialogOption1("faolynBlacksmithDialog1");
        dialogRepository.writeDialog(faolynBlacksmithDialogInit);
        Dialog faolynBlackSmithDialog1 = new Dialog("faolynBlacksmithDialog1","Will not be displayed","\"I would like to see what you have available for purchase\"",faolynBlacksmithAffect, false);
        dialogRepository.writeDialog(faolynBlackSmithDialog1);

        DialogAffect bridgetonBlacksmithAffect = new DialogAffect(DialogAffectType.SHOP_OPEN);
        bridgetonBlacksmithAffect.setShopTag(mainActivity.getString(R.string.bridgeton_blacksmith));
        Dialog bridgetonBlacksmithDialogInit = new Dialog(BRIDGETON_BLACKSMITH_DIALOG_INIT,"As you approach the blacksmith's shop you can hear the sounds of a hammer on metal. As you walk around the side you see the burly blacksmith hammering on a lump of metal while his young apprentice holds the tongs. He finishes hammering, puts the metal back in the forge and asks, \"What do you need?\"","none",new DialogAffect(DialogAffectType.NONE),false);
        bridgetonBlacksmithDialogInit.setDialogOption1("bridgetonBlacksmithDialog1");
        dialogRepository.writeDialog(bridgetonBlacksmithDialogInit);
        Dialog bridgetonBlackSmithDialog1 = new Dialog("bridgetonBlacksmithDialog1","Will not be displayed","\"I would like to see what you have available for purchase\"",bridgetonBlacksmithAffect, false);
        dialogRepository.writeDialog(bridgetonBlackSmithDialog1);
    }

    private void cleanUpQuestStartDialogs(){

    }
}
