package com.zblouse.fantasyfitness.dialog;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zblouse.fantasyfitness.core.FirestoreDatabase;
import com.zblouse.fantasyfitness.core.Repository;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.quest.QuestObjective;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DialogFirestoreDatabase extends FirestoreDatabase {

    private static final String COLLECTION = "dialog";
    private static final String DIALOGS = "dialogs";

    public DialogFirestoreDatabase(){
        super();
    }

    public DialogFirestoreDatabase(FirebaseFirestore firestore){
        super(firestore);
    }

    public void read(String userId, String dialogUuid, Repository<Dialog> dialogRepository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(userId).collection(DIALOGS).document(dialogUuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();
                    if(result.exists()) {
                        dialogRepository.readCallback(result.toObject(Dialog.class), metadata);
                    }
                } else {
                    dialogRepository.readCallback(null, metadata);
                }
            }
        });
    }

    public void readList(String userId, List<String> dialogUuids, DialogRepository dialogRepository, Map<String, Object> metadata){
        if(dialogUuids.isEmpty()){
            dialogRepository.readListCallback(new ArrayList<>(), metadata);
        } else {
            firestore.collection(COLLECTION).document(userId).collection(DIALOGS).whereIn("referenceId", dialogUuids).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Dialog> dialogList = task.getResult().toObjects(Dialog.class);
                        dialogRepository.readListCallback(dialogList, metadata);
                    } else {
                        dialogRepository.readListCallback(new ArrayList<>(), metadata);
                    }
                }
            });
        }
    }

    public void write(Dialog dialog, String userId, Repository<Dialog> dialogRepository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(userId).collection(DIALOGS).document(dialog.getReferenceId()).set(dialog).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dialogRepository.writeCallback(dialog, metadata);
                } else {
                    dialogRepository.writeCallback(null, metadata);
                }
            }
        });
    }

    public void delete(String userId, String dialogUuid, Repository<Dialog> dialogRepository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(userId).collection(DIALOGS).document(dialogUuid).delete();
    }

}
