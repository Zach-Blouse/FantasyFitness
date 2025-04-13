package com.zblouse.fantasyfitness.quest;

import android.content.Context;

import com.zblouse.fantasyfitness.activity.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class QuestTemporaryDataCache {

    public void saveQuest(Quest quest, MainActivity mainActivity) throws IOException {

        FileOutputStream fileOutputStream = mainActivity.openFileOutput( quest.getQuestUuid() + ".tmp", Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(quest);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public Quest loadQuest(String questUuid, MainActivity mainActivity) throws IOException {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            fileInputStream = mainActivity.openFileInput( questUuid +".tmp");
            objectInputStream = new ObjectInputStream(fileInputStream);
            Quest quest = (Quest) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return quest;
        } catch(Exception e){
            if(objectInputStream != null){
                objectInputStream.close();
            }
            if(fileInputStream != null) {
                fileInputStream.close();
            }

        }
        return null;
    }

    public boolean deleteQuest(String questUuid, MainActivity mainActivity){
        try{
            File file = new File(questUuid +".tmp");
            if(file.exists()){
                file.delete();
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
