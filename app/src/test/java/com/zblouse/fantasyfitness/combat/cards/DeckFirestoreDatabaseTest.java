package com.zblouse.fantasyfitness.combat.cards;

import static org.junit.Assert.assertEquals;
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

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;

public class DeckFirestoreDatabaseTest {

    @Test
    public void writeDeckTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("userCards"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("decks"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("testDeck"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument2.set(any())).thenReturn(mockTask);
        DeckRepository mockRepository = Mockito.mock(DeckRepository.class);

        DeckFirestoreDatabase testedDatabase = new DeckFirestoreDatabase(mockFirestore);

        Deck deck = new Deck("testUser", "testDeck", Arrays.asList("1", "2"));
        testedDatabase.writeDeck(deck,mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).writeCallback(eq(deck),anyMap());

    }

    @Test
    public void readDeckTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("userCards"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("userId"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("decks"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("deckName"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument2.get()).thenReturn(mockTask);
        DocumentSnapshot documentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(documentSnapshot);
        when(documentSnapshot.exists()).thenReturn(true);
        when(documentSnapshot.get("deckList")).thenReturn(Arrays.asList("1","2"));
        DeckRepository mockRepository = Mockito.mock(DeckRepository.class);

        DeckFirestoreDatabase testedDatabase = new DeckFirestoreDatabase(mockFirestore);

        testedDatabase.readDeck("userId", "deckName",mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        ArgumentCaptor<Deck> deckArgumentCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(mockRepository).readCallback(deckArgumentCaptor.capture(),anyMap());

        assertEquals("userId", deckArgumentCaptor.getValue().getUserId());
        assertEquals("deckName", deckArgumentCaptor.getValue().getDeckName());

    }
}
