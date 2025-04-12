package com.zblouse.fantasyfitness.quest;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zblouse.fantasyfitness.core.FirestoreDatabase;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestFirestoreDatabase extends FirestoreDatabase {

    private static final String COLLECTION = "quest";
    private static final String QUESTS = "quests";

    public QuestFirestoreDatabase(){
        super();
    }

    public QuestFirestoreDatabase(FirebaseFirestore firestore){
        super(firestore);
    }

    public void writeQuest(Quest quest, String userId, Repository<Quest> questRepository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(userId).collection(QUESTS).document(quest.getQuestUuid()).set(quest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    questRepository.writeCallback(quest, metadata);
                } else {
                    questRepository.writeCallback(null, metadata);
                }
            }
        });
    }

    public void fetchQuests(String userId, QuestRepository questRepository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(userId).collection(QUESTS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<Quest> questList = new ArrayList<>();
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult() ){
                        questList.add(documentSnapshot.toObject(Quest.class));
                    }
                    questRepository.listReadCallback(questList, metadata);
                }
            }
        });
    }

    public void deleteQuest(Quest quest, String userId, QuestRepository questRepository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(userId).collection(QUESTS).document(quest.getQuestUuid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    questRepository.deleteCallback(quest, metadata);
                } else {
                    questRepository.deleteCallback(null, metadata);
                }
            }
        });
    }
}
