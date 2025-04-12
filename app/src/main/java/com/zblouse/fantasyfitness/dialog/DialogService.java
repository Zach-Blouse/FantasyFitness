package com.zblouse.fantasyfitness.dialog;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.quest.Quest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DialogService implements DomainService<Dialog> {

    public static final String EMPTY_DIALOG_INIT = "emptyDialog";
    public static final String HERMIT_DIALOG_INIT = "hermitInit";
    public static final String INNKEEPER_DIALOG_INIT = "innKeeperDialogInit";
    private final DialogRepository dialogRepository;
    private final MainActivity mainActivity;

    public DialogService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.dialogRepository = new DialogRepository(mainActivity);
    }

    public DialogService(MainActivity mainActivity, DialogRepository dialogRepository){
        this.mainActivity = mainActivity;
        this.dialogRepository = dialogRepository;
    }

    public Dialog fetchDialogOption(String referenceId){
        Log.e("DialogService", "Fetching dialog: " + referenceId);
        return dialogRepository.readDialog(referenceId);
    }

    public void selectDialogOption(String referenceId, String currentLocation, Map<String, Object> metadata){
        Dialog dialogSelected = dialogRepository.readDialog(referenceId);
        if(dialogSelected.getDialogAffect().getDialogAffectType().equals(DialogAffectType.QUEST_GENERATE)){
            List<Quest> questsGenerated = mainActivity.getQuestService().generateQuests(currentLocation, 3);
            for(Quest quest: questsGenerated){
                DialogAffect questStartDialogAffect = new DialogAffect(DialogAffectType.QUEST_START);
                questStartDialogAffect.setQuestUuid(quest.getQuestUuid());
                Dialog questSelectDialog = new Dialog(UUID.randomUUID().toString(),"Good luck on your quest.",quest.getQuestDescription(),questStartDialogAffect);
                dialogRepository.writeDialog(questSelectDialog);
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
        }
        mainActivity.publishEvent(new DialogSelectedEvent(dialogSelected, metadata));
    }

    public void writeDialog(Dialog dialog){
        dialogRepository.writeDialog(dialog);
    }

    public boolean hasBeenInitialized(){
        return dialogRepository.databaseInitialized();
    }

    @Override
    public void repositoryResponse(Dialog responseBody, Map<String, Object> metadata) {

    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }

    public void initializeDialogs(){
        Dialog emptyDialog = new Dialog(EMPTY_DIALOG_INIT, "You spot a particularly uninteresting bird.","empty", new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(emptyDialog);
        Dialog initialHermitDialog = new Dialog(HERMIT_DIALOG_INIT,"You come upon an old hermit. He is clad in dirty pelts.","initial", new DialogAffect(DialogAffectType.NONE));
        initialHermitDialog.setDialogOption1("hermit1");
        initialHermitDialog.setDialogOption2("hermit2");
        initialHermitDialog.setDialogOption3("hermit3");
        dialogRepository.writeDialog(initialHermitDialog);
        Dialog hermitOption1 = new Dialog("hermit1", "He looks up at you and says, \"Hello Adventurer. You remind me of myself when I was your age. Want some free advice?\"","Hello Sir", new DialogAffect(DialogAffectType.NONE));
        hermitOption1.setDialogOption1("hermit1_1");
        hermitOption1.setDialogOption2("hermit1_2");
        dialogRepository.writeDialog(hermitOption1);
        Dialog hermitOption1_1 = new Dialog("hermit1_1", "Stay away from the Valley of Monsters, the monsters there are far too powerful. That's how I lost my whole adventuring party.\" The hermit gets a distant look in his eye and refuses to speak more.","Of course sir", new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(hermitOption1_1);
        Dialog hermitOption1_2 = new Dialog("hermit1_2", "Well then. You are free to make your own mistakes.","I don't think I want advice from a hermit.", new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(hermitOption1_2);
        Dialog hermitOption2 = new Dialog("hermit2","\"Well, keep on passing then.\"","I'm just passing through", new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(hermitOption2);
        Dialog hermitOption3 = new Dialog("hermit3", "The old hermit scowls at you and says \"You are in your own way. Begone.\"","Get out of my way old man", new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(hermitOption3);

        Dialog innKeeperDialogInit = new Dialog(INNKEEPER_DIALOG_INIT, "The inn is cozy inside. A nice fire is going and the air smells of warm spice drinks. The innkeeper is cleaning a mub behind the bar and a stranger in a long black cloak is sitting alone at a table by the window.","none",new DialogAffect(DialogAffectType.NONE));
        innKeeperDialogInit.setDialogOption1("innkeeper1");
        innKeeperDialogInit.setDialogOption2("innkeeper2");
        innKeeperDialogInit.setDialogOption3("innkeeper3");
        dialogRepository.writeDialog(innKeeperDialogInit);

        Dialog innkeeperOption1 = new Dialog("innkeeper1","The innkeeper continues polishing the mug she is cleaning but smiles and says \"Hello fair traveler, how can I help you?\"","Walk up to the innkeeper behind the bar.", new DialogAffect(DialogAffectType.NONE));
        innkeeperOption1.setDialogOption1("innkeeper1_1");
        innkeeperOption1.setDialogOption2("innkeeper1_2");
        innkeeperOption1.setDialogOption3("innkeeper1_3");
        dialogRepository.writeDialog(innkeeperOption1);
        Dialog innkeeperOption1_1 = new Dialog("innkeeper1_1","\"Feel free to sit and rest your feet. Let me know whenever you need anything\"","I am just taking a break from my travels",new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(innkeeperOption1_1);

        Dialog innkeeperOption1_2 = new Dialog("innkeeper1_2","\"Of course honey, my only other guest tonight is that traveller over there. She points to the mysterious hooded figure. Your room is the first one on the right down that hall. Have a good night's sleep\"","Can I get a room for the night?",new DialogAffect(DialogAffectType.NONE));
        innkeeperOption1_2.setDialogOption1("innkeeper1_2_1");
        innkeeperOption1_2.setDialogOption2("innkeeper1_2_2");
        dialogRepository.writeDialog(innkeeperOption1_2);

        Dialog innkeeperOption1_2_1 = new Dialog("innkeeper1_2_1", "The room is simple, but clean and comfortable looking.","Walk to your room",new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(innkeeperOption1_2_1);

        Dialog innkeeperOption1_2_2 = new Dialog("innkeeper1_2_2", "\"I make it my policy to never pry into my guests' business. You'd have to ask them\"", "\"What is going on with that hooded person over there?\"",new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(innkeeperOption1_2_2);

        Dialog innkeeperOption1_3 = new Dialog("innkeeper1_3", "These are the jobs I have at the moment", "Are there any quests on the job board?", new DialogAffect(DialogAffectType.QUEST_GENERATE));
        dialogRepository.writeDialog(innkeeperOption1_3);

        Dialog innkeeperOption2 = new Dialog("innkeeper2", "It feels nice to finally rest your weary legs. You feel yourself drift off to a well deserved sleep.","Take a seat at a table",new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(innkeeperOption2);

        Dialog innkeeperOption3 = new Dialog("innkeeper3", "The figure's cloak is up over their head, it is so shadowed inside you cannot see their face. Their head shifts slightly to look at you as you approach.","Walk over to the mysterious stranger", new DialogAffect(DialogAffectType.NONE));
        innkeeperOption3.setDialogOption1("innkeeper3_1");
        innkeeperOption3.setDialogOption2("innkeeper3_2");
        dialogRepository.writeDialog(innkeeperOption3);

        Dialog innkeeperOption3_1 = new Dialog("innkeeper3_1", "\"Leave\" the figure says in a much softer voice than you expected.", "Sit down at the same table as the figure.", new DialogAffect(DialogAffectType.NONE));
        innkeeperOption3_1.setDialogOption1("innkeeper3_1_1");
        innkeeperOption3_1.setDialogOption2("innkeeper3_1_2");
        dialogRepository.writeDialog(innkeeperOption3_1);

        Dialog innkeeperOption3_1_1 = new Dialog("innkeeper3_1_1", "You have a nice relaxing evening in the tavern. Eventually the stranger gets up and hobbles oddly over to their room.","Respect the strangers wishes and sit somewhere else.", new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(innkeeperOption3_1_1);

        Dialog innkeeperOption3_1_2 = new Dialog("innkeeper3_1_2", "\"I ... um... are a normal drinker of cider in this inn. Nothing for you to be curious about", "\"What's your deal?\"", new DialogAffect(DialogAffectType.NONE));
        innkeeperOption3_1_2.setDialogOption1("innkeeper3_1_2_1");
        innkeeperOption3_1_2.setDialogOption2("innkeeper3_1_2_2");
        dialogRepository.writeDialog(innkeeperOption3_1_2);

        Dialog innkeeperOption3_1_2_1 = new Dialog("innkeeper3_1_2_1","As you rip off the stranger's hood, you see a small cute reptilian face looking back at you in fear. The kobold then jumps up onto the table and crashes through the window. Two other kobolds quickly climb up out of the cloak and follow. The innkeeper shouts over, \"Again! You people need to stop scaring them, they drink enough for three but only take up one seat.\"", "Push the stranger's hood back",new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(innkeeperOption3_1_2_1);
        Dialog innkeeperOption3_1_2_2 = new Dialog("innkeeper3_1_2_2","The stranger faces you warily until you leave their table.", "\"Fine keep your secrets\"",new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(innkeeperOption3_1_2_2);

        Dialog innkeeperDialog3_2 = new Dialog("innkeeper3_2","The hooded figure gives you a small nod, then goes back to their drink.","Introduce yourself", new DialogAffect(DialogAffectType.NONE));
        innkeeperDialog3_2.setDialogOption1("innkeeper3_2_1");
        innkeeperDialog3_2.setDialogOption2("innkeeper3_2_2");
        dialogRepository.writeDialog(innkeeperDialog3_2);

        Dialog innkeeperDialog3_2_1 = new Dialog("innkeeper3_2_1", "You find your own seat in the inn and have a relaxing evening.", "Leave the stranger alone to their drink.", new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(innkeeperDialog3_2_1);

        Dialog innkeeperDialog3_2_2 = new Dialog("innkeeper3_2_2", "The stranger huddles down as though afraid. \"Hey!\" the innkeeper shouts over. \"Leave them alone. They clearly don't want to be bothered.\"","\"I'm talking to you. It's rude to not respond.\"", new DialogAffect(DialogAffectType.NONE));
        innkeeperDialog3_2_2.setDialogOption1("innkeeperDialog3_2_2_1");
        innkeeperDialog3_2_2.setDialogOption2("innkeeperDialog3_2_1");
        dialogRepository.writeDialog(innkeeperDialog3_2_2);

        Dialog innkeeperDialog3_2_2_1 = new Dialog("innkeeper3_2_2_1", "The cloak flies back as three kobolds burst out of it and run out of the inn. The innkeeper says to you, \"Dammit, they were my best customers. You get out of my inn too! You aren't allowed to bother my patrons.\"","\"No they need to show me who they are.\"", new DialogAffect(DialogAffectType.NONE));
        dialogRepository.writeDialog(innkeeperDialog3_2_2_1);
    }

    private void cleanUpQuestStartDialogs(){

    }
}
