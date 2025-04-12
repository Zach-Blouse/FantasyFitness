package com.zblouse.fantasyfitness.quest;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.actions.ActionResultType;
import com.zblouse.fantasyfitness.dialog.Dialog;
import com.zblouse.fantasyfitness.dialog.DialogAffect;
import com.zblouse.fantasyfitness.dialog.DialogAffectType;
import com.zblouse.fantasyfitness.dialog.DialogService;
import com.zblouse.fantasyfitness.world.GameLocationBuildingUtil;
import com.zblouse.fantasyfitness.world.GameLocationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class QuestGenerator {

    public static Quest generateQuest(String gameLocationId, int objectiveCount, DialogService dialogService){
        String questUuid = UUID.randomUUID().toString();

        List<String> combatLocationOptions = new ArrayList<>();
        List<String> visitLocationOptions = new ArrayList<>();
        int questReward = 10;
        if(gameLocationId.equals(GameLocationService.THANADEL_VILLAGE)){
            combatLocationOptions = Arrays.asList(GameLocationService.WOODLANDS);
            visitLocationOptions = Arrays.asList(GameLocationService.FARMLANDS);
            questReward =  new Random().nextInt(10) + 10;

        }else if(gameLocationId.equals(GameLocationService.FARMLANDS)){
            combatLocationOptions = Arrays.asList(GameLocationService.WOODLANDS);
            visitLocationOptions = Arrays.asList(GameLocationService.THANADEL_VILLAGE);
            questReward =  new Random().nextInt(10) + 10;

        } else {
            QuestObjective basicObjective = new QuestObjective(QuestObjectiveType.FIGHT, UUID.randomUUID().toString(), GameLocationService.WOODLANDS, R.id.dark_forest_button,false);
            return new Quest("Quest", questUuid,10, Arrays.asList(basicObjective));
        }

        List<QuestObjective> questObjectiveList = new ArrayList<>();
        String firstObjectiveLocation = getRandomStringOption(visitLocationOptions);
        questObjectiveList.add(new QuestObjective(QuestObjectiveType.VISIT,UUID.randomUUID().toString(),firstObjectiveLocation,getRandomIntegerOption(GameLocationBuildingUtil.getBuildingsInLocation(firstObjectiveLocation)),false));

        for(int i=1; i<objectiveCount; i++){
            QuestObjectiveType questObjectiveType = getRandomObjectiveType();
            if(questObjectiveType.equals(QuestObjectiveType.VISIT)){
                String objectiveLocation = getRandomStringOption(visitLocationOptions);
                questObjectiveList.add(new QuestObjective(questObjectiveType,UUID.randomUUID().toString(),objectiveLocation,getRandomIntegerOption(GameLocationBuildingUtil.getBuildingsInLocation(objectiveLocation)),false));
            } else if(questObjectiveType.equals(QuestObjectiveType.FIGHT)){
                String objectiveLocation = getRandomStringOption(combatLocationOptions);
                questObjectiveList.add(new QuestObjective(questObjectiveType, UUID.randomUUID().toString(),objectiveLocation,getRandomIntegerOption(GameLocationBuildingUtil.getBuildingsInLocation(objectiveLocation)),false));
            }
        }

        questObjectiveList.add(new QuestObjective(QuestObjectiveType.VISIT, UUID.randomUUID().toString(),firstObjectiveLocation,getRandomIntegerOption(GameLocationBuildingUtil.getBuildingsInLocation(firstObjectiveLocation)),false));


        //generate dialogs
        for(int i=0; i<questObjectiveList.size(); i++){
            QuestObjective objective = questObjectiveList.get(i);
            if(objective.getQuestObjectiveType().equals(QuestObjectiveType.VISIT)){
                if(i==0){
                    Dialog questObjectiveRootDialog = new Dialog(UUID.randomUUID().toString(),"As you enter the " + objective.getGameLocation() + " " + GameLocationBuildingUtil.getPrintableStringForBuilding(objective.getBuildingId()) + " you see the person you need to talk to.","none", new DialogAffect(DialogAffectType.NONE), true);
                    objective.setQuestDialogId(questObjectiveRootDialog.getReferenceId());
                    Dialog questDialogOption1 = new Dialog(UUID.randomUUID().toString(),"Ah, yes, could you " + determineDialogOptionForFollowingObjectives(questObjectiveList,i),"Hello, I was told you have a quest for me?", new DialogAffect(DialogAffectType.NONE), true);
                    DialogAffect questStartDialogAffect = new DialogAffect(DialogAffectType.QUEST_GOAL);
                    questStartDialogAffect.setQuestUuid(questUuid);
                    questStartDialogAffect.setQuestObjectiveUuid(objective.getQuestObjectiveUuid());
                    Dialog questDialogOptionAccept = new Dialog(UUID.randomUUID().toString(),"Excellent, good luck!","I'll do it.",questStartDialogAffect, true);
                    Dialog questDialogOptionRefuse = new Dialog(UUID.randomUUID().toString(),"That's a shame.","No, I'll pass.", new DialogAffect(DialogAffectType.NONE), true);
                    questObjectiveRootDialog.setDialogOption1(questDialogOption1.getReferenceId());
                    dialogService.writeDialog(questObjectiveRootDialog);
                    questDialogOption1.setDialogOption1(questDialogOptionAccept.getReferenceId());
                    questDialogOption1.setDialogOption2(questDialogOptionRefuse.getReferenceId());
                    dialogService.writeDialog(questDialogOption1);
                    dialogService.writeDialog(questDialogOptionAccept);
                    dialogService.writeDialog(questDialogOptionRefuse);
                } else {
                    Dialog questObjectiveRootDialog = new Dialog(UUID.randomUUID().toString(),"As you enter the " + objective.getGameLocation() + " " + GameLocationBuildingUtil.getPrintableStringForBuilding(objective.getBuildingId()) + " you see the person you need to talk to.","none", new DialogAffect(DialogAffectType.NONE), true);
                    objective.setQuestDialogId(questObjectiveRootDialog.getReferenceId());
                    DialogAffect questObjectiveDialogAffect = new DialogAffect(DialogAffectType.QUEST_GOAL);
                    questObjectiveDialogAffect.setQuestUuid(questUuid);
                    questObjectiveDialogAffect.setQuestObjectiveUuid(objective.getQuestObjectiveUuid());
                    String questDialogOptionFlavorText = "this did not replace";
                    if(i==(questObjectiveList.size()-1)){
                        questDialogOptionFlavorText = "Thank you! Here is your reward";
                    } else {
                        questDialogOptionFlavorText = "Thank you! Could you now " + determineDialogOptionForFollowingObjectives(questObjectiveList,i);
                    }
                    Dialog questDialogOption1 = new Dialog(UUID.randomUUID().toString(), questDialogOptionFlavorText, determineOptionTextForQuestObjectiveOption(questObjectiveList,i),questObjectiveDialogAffect, true);
                    Dialog questDialogOptionAccept = new Dialog(UUID.randomUUID().toString(),"Excellent, good luck!","Thank you.",new DialogAffect(DialogAffectType.NONE), true);
                    questObjectiveRootDialog.setDialogOption1(questDialogOption1.getReferenceId());
                    dialogService.writeDialog(questObjectiveRootDialog);
                    questDialogOption1.setDialogOption1(questDialogOptionAccept.getReferenceId());
                    dialogService.writeDialog(questDialogOption1);
                    dialogService.writeDialog(questDialogOptionAccept);
                }
            }
        }

        return new Quest("Daily Quest",questUuid,questReward,questObjectiveList);
    }

    private static QuestObjectiveType getRandomObjectiveType(){
        Random random = new Random();
        int result = random.nextInt(3);
        switch (result) {
            case 0:
            case 1:
                return QuestObjectiveType.FIGHT;
            default:
                return QuestObjectiveType.VISIT;
        }
    }

    private static String getRandomStringOption(List<String> optionList){
        Random random = new Random();
        int result = random.nextInt(optionList.size());
        return optionList.get(result);
    }

    private static Integer getRandomIntegerOption(List<Integer> optionList){
        Random random = new Random();
        int result = random.nextInt(optionList.size());
        return optionList.get(result);
    }

    private static String determineDialogOptionForFollowingObjectives(List<QuestObjective> objectives, int thisObjective){
        String dialog = "";

        for(int i = (thisObjective+1); i<objectives.size();i++){
            if(objectives.get(i).getQuestObjectiveType().equals(QuestObjectiveType.VISIT)){
                if(dialog.equals("")){
                    dialog =  "bring this package to the " + GameLocationBuildingUtil.getPrintableStringForBuilding(objectives.get(i).getBuildingId()) + " in " + objectives.get(i).getGameLocation();
                } else {
                    if (objectives.get(i - 1).getQuestObjectiveType().equals(QuestObjectiveType.VISIT)) {
                        dialog += "and then bring this other package to the " + GameLocationBuildingUtil.getPrintableStringForBuilding(objectives.get(i).getBuildingId()) + " in " + objectives.get(i).getGameLocation();
                    } else if (objectives.get(i - 1).getQuestObjectiveType().equals(QuestObjectiveType.FIGHT)) {
                        dialog += " and then go to the " + GameLocationBuildingUtil.getPrintableStringForBuilding(objectives.get(i).getBuildingId()) + " in " + objectives.get(i).getGameLocation() + " and let them know you cleared out the monsters?";
                    }
                }
            } else if (objectives.get(i).getQuestObjectiveType().equals(QuestObjectiveType.FIGHT)){
                if(dialog.equals("")){
                    dialog = "go to " + objectives.get(i).getGameLocation() + " and clear out any monsters in the " + GameLocationBuildingUtil.getPrintableStringForBuilding(objectives.get(i).getBuildingId());
                } else {
                    if (objectives.get(i - 1).getQuestObjectiveType().equals(QuestObjectiveType.VISIT)) {
                        dialog += " and then go to " + objectives.get(i).getGameLocation() + " and clear out any monsters in the " + GameLocationBuildingUtil.getPrintableStringForBuilding(objectives.get(i).getBuildingId());
                    } else if (objectives.get(i - 1).getQuestObjectiveType().equals(QuestObjectiveType.FIGHT)) {
                        dialog += " and then also clear out the " + GameLocationBuildingUtil.getPrintableStringForBuilding(objectives.get(i).getBuildingId()) + " in " + objectives.get(i).getGameLocation();
                    }
                }
            }
        }
        return dialog;
    }

    private static String determineOptionTextForQuestObjectiveOption(List<QuestObjective> objectives, int thisObjective){
        QuestObjective previousObjective = objectives.get(thisObjective-1);
        //check if this is the final objective
        if(previousObjective.getQuestObjectiveType().equals(QuestObjectiveType.VISIT)){
            return "I have this package for you.";
        } else {
            return "I fought those monsters in the " + previousObjective.getGameLocation();
        }
    }
}
