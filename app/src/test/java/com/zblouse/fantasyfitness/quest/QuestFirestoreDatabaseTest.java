package com.zblouse.fantasyfitness.quest;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardFirestoreDatabase;
import com.zblouse.fantasyfitness.combat.cards.CardRepository;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuestFirestoreDatabaseTest {

    @Test
    public void writeTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("quests"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("testUuid"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument2.set(any())).thenReturn(mockTask);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestFirestoreDatabase testedDatabase = new QuestFirestoreDatabase(mockFirestore);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", "testUuid", 8, Arrays.asList(objective1, objective2));

        testedDatabase.writeQuest(quest,"testUser", mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).writeCallback(eq(quest),anyMap());

    }

    @Test
    public void readTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("quests"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("testUuid"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument2.get()).thenReturn(mockTask);
        DocumentSnapshot questSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(questSnapshot);
        when(questSnapshot.exists()).thenReturn(true);
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", "testUuid", 8, Arrays.asList(objective1, objective2));

        when(questSnapshot.toObject(eq(Quest.class))).thenReturn(quest);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestFirestoreDatabase testedDatabase = new QuestFirestoreDatabase(mockFirestore);

        testedDatabase.readQuest("testUser","testUuid", mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).readCallback(eq(quest),anyMap());

    }

    @Test
    public void fetchQuests(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("quests"))).thenReturn(mockCollectionReference2);
        Task<QuerySnapshot> mockQuestQueryTask = Mockito.mock(Task.class);
        when(mockCollectionReference2.get()).thenReturn(mockQuestQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQuestQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> questSnapshots = new ArrayList<>();

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", "testUuid", 8, Arrays.asList(objective1, objective2));
        QueryDocumentSnapshot quest1Snapshot = Mockito.mock(QueryDocumentSnapshot.class);
        when(quest1Snapshot.toObject(eq(Quest.class))).thenReturn(quest);
        questSnapshots.add(quest1Snapshot);

        QuestObjective objective3 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective4 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest2 = new Quest("testQuest", "testUuid2", 8, Arrays.asList(objective3, objective4));
        QueryDocumentSnapshot quest2Snapshot = Mockito.mock(QueryDocumentSnapshot.class);
        when(quest2Snapshot.toObject(eq(Quest.class))).thenReturn(quest2);
        questSnapshots.add(quest2Snapshot);

        QuerySnapshot querySnapshotQuest = Mockito.mock(QuerySnapshot.class);
        when(querySnapshotQuest.iterator()).thenReturn(questSnapshots.iterator());
        when(mockQuestQueryTask.getResult()).thenReturn(querySnapshotQuest);

        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestFirestoreDatabase testedDatabase = new QuestFirestoreDatabase(mockFirestore);

        testedDatabase.fetchQuests("testUser",mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockQuestQueryTask).addOnCompleteListener((OnCompleteListener<QuerySnapshot>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockQuestQueryTask);

        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockRepository).listReadCallback(listArgumentCaptor.capture(),anyMap());

        boolean quest1Returned = false;
        boolean quest2Returned = false;
        for(Quest returnedQuest: (List<Quest>)listArgumentCaptor.getValue()){
            if(returnedQuest.getQuestUuid().equals(quest.getQuestUuid())){
                quest1Returned = true;
            }
            if(returnedQuest.getQuestUuid().equals(quest2.getQuestUuid())){
                quest2Returned = true;
            }
        }
        assertTrue(quest1Returned && quest2Returned);
    }

    @Test
    public void deleteTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("quests"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("testUuid"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument2.delete()).thenReturn(mockTask);
        DocumentSnapshot questSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(questSnapshot);
        when(questSnapshot.exists()).thenReturn(true);
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", "testUuid", 8, Arrays.asList(objective1, objective2));

        when(questSnapshot.toObject(eq(Quest.class))).thenReturn(quest);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestFirestoreDatabase testedDatabase = new QuestFirestoreDatabase(mockFirestore);

        testedDatabase.deleteQuest(quest,"testUser", mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).deleteCallback(eq(quest),anyMap());

    }
}
