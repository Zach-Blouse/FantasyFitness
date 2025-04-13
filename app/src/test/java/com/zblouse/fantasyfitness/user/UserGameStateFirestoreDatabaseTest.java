package com.zblouse.fantasyfitness.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class UserGameStateFirestoreDatabaseTest {

    @Test
    public void writeSuccessTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        Double testUserDistance = 5.5;
        int testUserGameCurrency = 6;
        UserGameState testUserGameState = new UserGameState(testUserId,testUserLocation,testUserDistance,testUserGameCurrency);
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("gameState"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument.set(anyMap())).thenReturn(mockTask);
        UserGameStateRepository mockRepository = Mockito.mock(UserGameStateRepository.class);

        UserGameStateFirestoreDatabase testedDatabase = new UserGameStateFirestoreDatabase(mockFirestore);
        testedDatabase.write(testUserGameState,mockRepository,new HashMap<>());

        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockDocument).set(mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD));
        assertEquals(testUserLocation,mapArgumentCaptor.getValue().get(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD));
        assert(mapArgumentCaptor.getValue().containsKey(UserGameStateFirestoreDatabase.USER_SAVED_DISTANCE));
        assertEquals(testUserDistance,(Double)mapArgumentCaptor.getValue().get(UserGameStateFirestoreDatabase.USER_SAVED_DISTANCE), 0.0);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        ArgumentCaptor<UserGameState> userGameStateArgumentCaptor = ArgumentCaptor.forClass(UserGameState.class);
        verify(mockRepository).writeCallback(userGameStateArgumentCaptor.capture(),anyMap());
        assertEquals(testUserId, userGameStateArgumentCaptor.getValue().getUserId());
        assertEquals(testUserLocation, userGameStateArgumentCaptor.getValue().getCurrentGameLocationName());
        assertEquals(testUserDistance,userGameStateArgumentCaptor.getValue().getSavedWorkoutDistanceMeters(),0.0);
        assertEquals(testUserGameCurrency, userGameStateArgumentCaptor.getValue().getUserGameCurrency());

    }

    @Test
    public void writeFailedTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        Double testUserDistance = 5.5;
        UserGameState testUserGameState = new UserGameState(testUserId,testUserLocation,testUserDistance,6);
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("gameState"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(false);
        when(mockDocument.set(anyMap())).thenReturn(mockTask);
        UserGameStateRepository mockRepository = Mockito.mock(UserGameStateRepository.class);

        UserGameStateFirestoreDatabase testedDatabase = new UserGameStateFirestoreDatabase(mockFirestore);
        testedDatabase.write(testUserGameState,mockRepository,new HashMap<>());

        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockDocument).set(mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD));
        assertEquals(testUserLocation,mapArgumentCaptor.getValue().get(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD));
        assert(mapArgumentCaptor.getValue().containsKey(UserGameStateFirestoreDatabase.USER_SAVED_DISTANCE));
        assertEquals(testUserDistance,(Double)mapArgumentCaptor.getValue().get(UserGameStateFirestoreDatabase.USER_SAVED_DISTANCE), 0.0);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        verify(mockRepository).writeCallback(eq(null),anyMap());
    }

    @Test
    public void updateFieldSuccessTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("gameState"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument.update(anyString(),any())).thenReturn(mockTask);
        UserGameStateRepository mockRepository = Mockito.mock(UserGameStateRepository.class);

        UserGameStateFirestoreDatabase testedDatabase = new UserGameStateFirestoreDatabase(mockFirestore);
        testedDatabase.updateField(testUserId,UserGameStateFirestoreDatabase.USER_LOCATION_FIELD,testUserLocation,mockRepository,new HashMap<>());

        verify(mockDocument).update(eq(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD),eq(testUserLocation));

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).updateCallback(eq(true),anyMap());
    }

    @Test
    public void updateFieldFailTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("gameState"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(false);
        when(mockDocument.update(anyString(),any())).thenReturn(mockTask);
        UserGameStateRepository mockRepository = Mockito.mock(UserGameStateRepository.class);

        UserGameStateFirestoreDatabase testedDatabase = new UserGameStateFirestoreDatabase(mockFirestore);
        testedDatabase.updateField(testUserId,UserGameStateFirestoreDatabase.USER_LOCATION_FIELD,testUserLocation,mockRepository,new HashMap<>());

        verify(mockDocument).update(eq(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD),eq(testUserLocation));

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).updateCallback(eq(false),anyMap());
    }

    @Test
    public void readSuccessfulExistsTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        Double testUserDistance = 5.5;
        int testUserCurrency = 6;
        UserGameState testUserGameState = new UserGameState(testUserId,testUserLocation,testUserDistance,testUserCurrency);
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("gameState"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.get(eq(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD), eq(String.class))).thenReturn(testUserLocation);
        when(mockDocumentSnapshot.get(eq(UserGameStateFirestoreDatabase.USER_SAVED_DISTANCE), eq(Double.class))).thenReturn(testUserDistance);
        when(mockDocumentSnapshot.get(eq(UserGameStateFirestoreDatabase.USER_GAME_CURRENCY), eq(Integer.class))).thenReturn(testUserCurrency);
        when(mockDocument.get()).thenReturn(mockTask);
        UserGameStateRepository mockRepository = Mockito.mock(UserGameStateRepository.class);

        UserGameStateFirestoreDatabase testedDatabase = new UserGameStateFirestoreDatabase(mockFirestore);

        testedDatabase.read(testUserId,mockRepository,new HashMap<>());
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        ArgumentCaptor<UserGameState> userGameStateArgumentCaptor = ArgumentCaptor.forClass(UserGameState.class);
        verify(mockRepository).readCallback(userGameStateArgumentCaptor.capture(),anyMap());
        assertEquals(testUserId, userGameStateArgumentCaptor.getValue().getUserId());
        assertEquals(testUserLocation, userGameStateArgumentCaptor.getValue().getCurrentGameLocationName());
        assertEquals(testUserDistance, userGameStateArgumentCaptor.getValue().getSavedWorkoutDistanceMeters(),0.0);
        assertEquals(testUserCurrency, userGameStateArgumentCaptor.getValue().getUserGameCurrency());
    }

    @Test
    public void readSuccessfulNotExistsTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        Double testUserDistance = 5.5;
        int testUserCurrency = 6;
        UserGameState testUserGameState = new UserGameState(testUserId,testUserLocation,testUserDistance,testUserCurrency);
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("gameState"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        when(mockDocumentSnapshot.get(eq(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD), eq(String.class))).thenReturn(testUserLocation);
        when(mockDocumentSnapshot.get(eq(UserGameStateFirestoreDatabase.USER_SAVED_DISTANCE), eq(Double.class))).thenReturn(testUserDistance);
        when(mockDocumentSnapshot.get(eq(UserGameStateFirestoreDatabase.USER_GAME_CURRENCY), eq(Integer.class))).thenReturn(testUserCurrency);
        when(mockDocument.get()).thenReturn(mockTask);
        UserGameStateRepository mockRepository = Mockito.mock(UserGameStateRepository.class);

        UserGameStateFirestoreDatabase testedDatabase = new UserGameStateFirestoreDatabase(mockFirestore);

        testedDatabase.read(testUserId,mockRepository,new HashMap<>());
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        ArgumentCaptor<UserGameState> userGameStateArgumentCaptor = ArgumentCaptor.forClass(UserGameState.class);
        verify(mockRepository).readCallback(eq(null),anyMap());

    }

    @Test
    public void readFailedTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        Double testUserDistance = 5.5;
        Integer testUserCurrency = 6;
        UserGameState testUserGameState = new UserGameState(testUserId,testUserLocation,testUserDistance,testUserCurrency);
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("gameState"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(false);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        when(mockDocumentSnapshot.get(eq(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD), eq(String.class))).thenReturn(testUserLocation);
        when(mockDocumentSnapshot.get(eq(UserGameStateFirestoreDatabase.USER_SAVED_DISTANCE), eq(Double.class))).thenReturn(testUserDistance);
        when(mockDocumentSnapshot.get(eq(UserGameStateFirestoreDatabase.USER_GAME_CURRENCY), eq(Integer.class))).thenReturn(testUserCurrency);
        when(mockDocument.get()).thenReturn(mockTask);
        UserGameStateRepository mockRepository = Mockito.mock(UserGameStateRepository.class);

        UserGameStateFirestoreDatabase testedDatabase = new UserGameStateFirestoreDatabase(mockFirestore);

        testedDatabase.read(testUserId,mockRepository,new HashMap<>());
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        ArgumentCaptor<UserGameState> userGameStateArgumentCaptor = ArgumentCaptor.forClass(UserGameState.class);
        verify(mockRepository).readCallback(eq(null),anyMap());

    }
}
