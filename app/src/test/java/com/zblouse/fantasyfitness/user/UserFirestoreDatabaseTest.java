package com.zblouse.fantasyfitness.user;

import static org.junit.Assert.assertEquals;
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

import java.util.HashMap;
import java.util.Map;

public class UserFirestoreDatabaseTest {

    @Test
    public void createSuccessTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        UserRepository mockRepository = Mockito.mock(UserRepository.class);
        String testUserId = "testId1";
        String testUsername = "username1";
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("testName", "createTest");
        User testUser = new User(testUserId, testUsername);

        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("users"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument.set(anyMap())).thenReturn(mockTask);
        UserFirestoreDatabase testedDatabase = new UserFirestoreDatabase(mockFirestore);
        testedDatabase.create(testUser,mockRepository,metadata);

        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockDocument).set(mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey("UID"));
        assertEquals(testUserId, mapArgumentCaptor.getValue().get("UID"));
        assert(mapArgumentCaptor.getValue().containsKey("USERNAME"));
        assertEquals(testUsername, mapArgumentCaptor.getValue().get("USERNAME"));
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Map> mapArgumentCaptor2 = ArgumentCaptor.forClass(Map.class);
        verify(mockRepository).writeCallback(userArgumentCaptor.capture(),mapArgumentCaptor2.capture());
        assertEquals(testUserId,userArgumentCaptor.getValue().getId());
        assertEquals(testUsername, userArgumentCaptor.getValue().getUsername());
        assert(mapArgumentCaptor2.getValue().containsKey("testName"));
        assert(mapArgumentCaptor2.getValue().get("testName").equals("createTest"));
    }

    @Test
    public void createFailedTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        UserRepository mockRepository = Mockito.mock(UserRepository.class);
        String testUserId = "testId1";
        String testUsername = "username1";
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("testName", "createTest");
        User testUser = new User(testUserId, testUsername);

        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("users"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(false);
        when(mockDocument.set(anyMap())).thenReturn(mockTask);
        UserFirestoreDatabase testedDatabase = new UserFirestoreDatabase(mockFirestore);
        testedDatabase.create(testUser,mockRepository,metadata);

        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockDocument).set(mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey("UID"));
        assertEquals(testUserId, mapArgumentCaptor.getValue().get("UID"));
        assert(mapArgumentCaptor.getValue().containsKey("USERNAME"));
        assertEquals(testUsername, mapArgumentCaptor.getValue().get("USERNAME"));
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        ArgumentCaptor<Map> mapArgumentCaptor2 = ArgumentCaptor.forClass(Map.class);
        verify(mockRepository).writeCallback(eq(null),mapArgumentCaptor2.capture());
        assert(mapArgumentCaptor2.getValue().containsKey("testName"));
        assert(mapArgumentCaptor2.getValue().get("testName").equals("createTest"));
    }

    @Test
    public void readSuccessfulTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        UserRepository mockRepository = Mockito.mock(UserRepository.class);
        String testUserId = "testId1";
        String testUsername = "username1";
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("testName", "createTest");

        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("users"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocument.get()).thenReturn(mockTask);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.get("UID", String.class)).thenReturn(testUserId);
        when(mockDocumentSnapshot.get("USERNAME", String.class)).thenReturn(testUsername);
        UserFirestoreDatabase testedDatabase = new UserFirestoreDatabase(mockFirestore);
        testedDatabase.read(testUserId,mockRepository,metadata);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener(onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Map> mapArgumentCaptor2 = ArgumentCaptor.forClass(Map.class);
        verify(mockRepository).readCallback(userArgumentCaptor.capture(),mapArgumentCaptor2.capture());
        assertEquals(testUserId,userArgumentCaptor.getValue().getId());
        assertEquals(testUsername, userArgumentCaptor.getValue().getUsername());
        assert(mapArgumentCaptor2.getValue().containsKey("testName"));
        assert(mapArgumentCaptor2.getValue().get("testName").equals("createTest"));
    }

    @Test
    public void readFailedTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        UserRepository mockRepository = Mockito.mock(UserRepository.class);
        String testUserId = "testId1";
        String testUsername = "username1";
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("testName", "createTest");

        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("users"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(false);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocument.get()).thenReturn(mockTask);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.get("UID", String.class)).thenReturn(testUserId);
        when(mockDocumentSnapshot.get("USERNAME", String.class)).thenReturn(testUsername);
        UserFirestoreDatabase testedDatabase = new UserFirestoreDatabase(mockFirestore);
        testedDatabase.read(testUserId,mockRepository,metadata);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener(onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        ArgumentCaptor<Map> mapArgumentCaptor2 = ArgumentCaptor.forClass(Map.class);
        verify(mockRepository).readCallback(eq(null),mapArgumentCaptor2.capture());
        assert(mapArgumentCaptor2.getValue().containsKey("testName"));
        assert(mapArgumentCaptor2.getValue().get("testName").equals("createTest"));
    }

    @Test
    public void readUserNotFoundTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        UserRepository mockRepository = Mockito.mock(UserRepository.class);
        String testUserId = "testId1";
        String testUsername = "username1";
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("testName", "createTest");

        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("users"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocument.get()).thenReturn(mockTask);
        when(mockDocumentSnapshot.exists()).thenReturn(false);

        UserFirestoreDatabase testedDatabase = new UserFirestoreDatabase(mockFirestore);
        testedDatabase.read(testUserId,mockRepository,metadata);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener(onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);
        ArgumentCaptor<Map> mapArgumentCaptor2 = ArgumentCaptor.forClass(Map.class);
        verify(mockRepository).readCallback(eq(null),mapArgumentCaptor2.capture());
        assert(mapArgumentCaptor2.getValue().containsKey("testName"));
        assert(mapArgumentCaptor2.getValue().get("testName").equals("createTest"));
    }
}
