package com.zblouse.fantasyfitness.dialog;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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
import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AbilityType;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardFirestoreDatabase;
import com.zblouse.fantasyfitness.combat.cards.CardRepository;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.CharacterCard;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.combat.cards.DebuffAbility;
import com.zblouse.fantasyfitness.combat.cards.EffectCard;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.combat.cards.PermanentCard;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.quest.QuestFirestoreDatabase;
import com.zblouse.fantasyfitness.quest.QuestObjective;
import com.zblouse.fantasyfitness.quest.QuestObjectiveType;
import com.zblouse.fantasyfitness.quest.QuestRepository;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DialogFirestoreDatabaseTest {

    @Test
    public void writeTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("dialog"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("dialogs"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("referenceId"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument2.set(any())).thenReturn(mockTask);
        DialogRepository mockRepository = Mockito.mock(DialogRepository.class);

        DialogFirestoreDatabase testedDatabase = new DialogFirestoreDatabase(mockFirestore);

        Dialog dialog = new Dialog("referenceId", "flavorTest", "optionTest", new DialogAffect(DialogAffectType.QUEST_GENERATE),true);

        testedDatabase.write(dialog,"testUser",mockRepository, new HashMap<>());

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).writeCallback(eq(dialog),anyMap());
    }


    @Test
    public void writeFailedTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("dialog"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("dialogs"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("referenceId"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(false);
        when(mockDocument2.set(any())).thenReturn(mockTask);
        DialogRepository mockRepository = Mockito.mock(DialogRepository.class);

        DialogFirestoreDatabase testedDatabase = new DialogFirestoreDatabase(mockFirestore);

        Dialog dialog = new Dialog("referenceId", "flavorTest", "optionTest", new DialogAffect(DialogAffectType.QUEST_GENERATE),true);

        testedDatabase.write(dialog,"testUser",mockRepository, new HashMap<>());

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).writeCallback(eq(null),anyMap());
    }

    @Test
    public void readTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("dialog"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("dialogs"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("referenceId"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument2.get()).thenReturn(mockTask);
        DialogRepository mockRepository = Mockito.mock(DialogRepository.class);

        DialogAffect testAffect = new DialogAffect(DialogAffectType.QUEST_GOAL);
        testAffect.setQuestObjectiveUuid("questUuid");
        testAffect.setQuestObjectiveUuid("questObjectiveUuid");
        Dialog dialog = new Dialog("referenceId", "flavorTest", "optionTest", testAffect,true);

        DocumentSnapshot mockDialogSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDialogSnapshot);
        when(mockDialogSnapshot.exists()).thenReturn(true);
        when(mockDialogSnapshot.toObject(eq(Dialog.class))).thenReturn(dialog);

        DialogFirestoreDatabase testedDatabase = new DialogFirestoreDatabase(mockFirestore);

        testedDatabase.read("testUser","referenceId",mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        ArgumentCaptor<Dialog> dialogArgumentCaptor = ArgumentCaptor.forClass(Dialog.class);
        verify(mockRepository).readCallback(dialogArgumentCaptor.capture(), anyMap());

        assertEquals(dialog, dialogArgumentCaptor.getValue());
    }

    @Test
    public void readFailedTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("dialog"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("dialogs"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("referenceId"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(false);
        when(mockDocument2.get()).thenReturn(mockTask);
        DialogRepository mockRepository = Mockito.mock(DialogRepository.class);

        DialogAffect testAffect = new DialogAffect(DialogAffectType.QUEST_GOAL);
        testAffect.setQuestObjectiveUuid("questUuid");
        testAffect.setQuestObjectiveUuid("questObjectiveUuid");
        Dialog dialog = new Dialog("referenceId", "flavorTest", "optionTest", testAffect,true);

        DocumentSnapshot mockDialogSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDialogSnapshot);
        when(mockDialogSnapshot.exists()).thenReturn(true);
        when(mockDialogSnapshot.toObject(eq(Dialog.class))).thenReturn(dialog);

        DialogFirestoreDatabase testedDatabase = new DialogFirestoreDatabase(mockFirestore);

        testedDatabase.read("testUser","referenceId",mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).readCallback(eq(null), anyMap());

    }

    @Test
    public void readDialogListTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("dialog"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("dialogs"))).thenReturn(mockCollectionReference2);
        Query mockQuery = Mockito.mock(Query.class);
        when(mockCollectionReference2.whereIn(eq("referenceId"), anyList())).thenReturn(mockQuery);
        Task mockTask = Mockito.mock(Task.class);
        when(mockQuery.get()).thenReturn(mockTask);
        when(mockTask.isSuccessful()).thenReturn(true);

        Dialog dialog1 = new Dialog("referenceId1", "flavorTest", "optionTest", new DialogAffect(DialogAffectType.QUEST_GENERATE),true);
        Dialog dialog2 = new Dialog("referenceId2", "flavorTest", "optionTest", new DialogAffect(DialogAffectType.QUEST_GENERATE),true);


        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(mockTask.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.toObjects(eq(Dialog.class))).thenReturn(Arrays.asList(dialog1, dialog2));

        DialogRepository mockRepository = Mockito.mock(DialogRepository.class);

        DialogFirestoreDatabase testedDatabase = new DialogFirestoreDatabase(mockFirestore);

        testedDatabase.readList("testUser", Arrays.asList("referenceId1", "referenceId2"),mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<QuerySnapshot>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);


        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockRepository).readListCallback(listCaptor.capture(),anyMap());

        assertEquals(dialog1, listCaptor.getValue().get(0));
        assertEquals(dialog2,listCaptor.getValue().get(1));

    }
}
