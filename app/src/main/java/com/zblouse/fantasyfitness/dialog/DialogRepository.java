package com.zblouse.fantasyfitness.dialog;

import android.content.Context;

import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.Map;

public class DialogRepository implements Repository<Dialog> {

    private final DialogSqlDatabase dialogSqlDatabase;

    public DialogRepository(DialogSqlDatabase dialogSqlDatabase){
        this.dialogSqlDatabase = dialogSqlDatabase;
    }

    public DialogRepository(Context context){
        this.dialogSqlDatabase = new DialogSqlDatabase(context);
    }

    public void writeDialog(Dialog dialog){
        this.dialogSqlDatabase.addDialogToDatabase(dialog);
    }

    public Dialog readDialog(String referenceId){
        return this.dialogSqlDatabase.getDialogByReferenceId(referenceId);
    }

    @Override
    public void readCallback(Dialog object, Map<String, Object> metadata) {

    }

    @Override
    public void writeCallback(Dialog object, Map<String, Object> metadata) {

    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {

    }
}
