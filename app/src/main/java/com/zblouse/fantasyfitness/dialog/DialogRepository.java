package com.zblouse.fantasyfitness.dialog;

import android.content.Context;

import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DialogRepository implements Repository<Dialog> {

    private DialogService dialogService;
    private final DialogSqlDatabase dialogSqlDatabase;
    private DialogFirestoreDatabase dialogFirestoreDatabase;

    public DialogRepository(DialogService dialogService, DialogSqlDatabase dialogSqlDatabase, DialogFirestoreDatabase dialogFirestoreDatabase){
        this.dialogService = dialogService;
        this.dialogSqlDatabase = dialogSqlDatabase;
        this.dialogFirestoreDatabase = dialogFirestoreDatabase;
    }

    public DialogRepository(DialogService dialogService, Context context){
        this.dialogSqlDatabase = new DialogSqlDatabase(context);
        this.dialogFirestoreDatabase = new DialogFirestoreDatabase();
        this.dialogService = dialogService;
    }

    public void writeDialog(Dialog dialog){
        this.dialogSqlDatabase.addDialogToDatabase(dialog);
    }

    public void writeQuestDialog(Dialog dialog, String userId, Map<String, Object> metadata){
        this.dialogFirestoreDatabase.write(dialog, userId, this, metadata);
    }

    public void readQuestDialogs(String userId, String referenceId1,String referenceId2,String referenceId3,String referenceId4, Map<String, Object> metadata){
        List<String> referenceIds = new ArrayList<>();
        if(referenceId1 != null){
            referenceIds.add(referenceId1);
        }
        if(referenceId2 != null){
            referenceIds.add(referenceId2);
        }
        if(referenceId3 != null){
            referenceIds.add(referenceId3);
        }
        if(referenceId4 != null){
            referenceIds.add(referenceId4);
        }
        this.dialogFirestoreDatabase.readList(userId, referenceIds, this, metadata);
    }

    public void readQuestDialog(String userId, String referenceId, Map<String, Object> metadata){
        this.dialogFirestoreDatabase.read(userId, referenceId, this, metadata);
    }

    public Dialog readDialog(String referenceId){
        return this.dialogSqlDatabase.getDialogByReferenceId(referenceId);
    }

    public boolean databaseInitialized(){
        return dialogSqlDatabase.databaseInitialized();
    }

    public void readListCallback(List<Dialog> dialogs, Map<String, Object> metadata){
        dialogService.repositoryListResponse(dialogs,metadata);
    }

    @Override
    public void readCallback(Dialog dialog, Map<String, Object> metadata) {
        dialogService.repositoryResponse(dialog,metadata);
    }

    @Override
    public void writeCallback(Dialog object, Map<String, Object> metadata) {

    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {

    }
}
