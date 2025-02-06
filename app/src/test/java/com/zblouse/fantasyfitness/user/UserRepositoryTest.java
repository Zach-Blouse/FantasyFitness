package com.zblouse.fantasyfitness.user;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.zblouse.fantasyfitness.core.Repository;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryTest {

    @Test
    public void writeUserTest(){
        UserFirestoreDatabase mockFirestoreDatabase = Mockito.mock(UserFirestoreDatabase.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        UserRepository testedRepository = new UserRepository(mockUserService,mockFirestoreDatabase);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserService.CALLING_FUNCTION_KEY,UserService.REGISTER_USER);
        String testUid = "testId";
        String testUsername = "testusername";
        testedRepository.writeUser(testUid,testUsername, metadata);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockFirestoreDatabase).create((User)userArgumentCaptor.capture(), eq(testedRepository),(Map)mapArgumentCaptor.capture());
        assert(userArgumentCaptor.getValue().getId().equals(testUid));
        assert(userArgumentCaptor.getValue().getUsername().equals(testUsername));
        assert(mapArgumentCaptor.getValue().containsKey(UserService.CALLING_FUNCTION_KEY));
        assert(mapArgumentCaptor.getValue().get(UserService.CALLING_FUNCTION_KEY).equals(UserService.REGISTER_USER));
        assert(mapArgumentCaptor.getValue().containsKey(Repository.REPOSITORY_INTERACTION));
        assert(mapArgumentCaptor.getValue().get(Repository.REPOSITORY_INTERACTION).equals(UserRepository.WRITE_USER));
    }

    @Test
    public void readUserTest(){
        UserFirestoreDatabase mockFirestoreDatabase = Mockito.mock(UserFirestoreDatabase.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        UserRepository testedRepository = new UserRepository(mockUserService,mockFirestoreDatabase);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserService.CALLING_FUNCTION_KEY,UserService.USER_EXIST_CHECK);
        String testUid = "testId";
        testedRepository.readUser(testUid, metadata);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockFirestoreDatabase).read(eq(testUid), eq(testedRepository),(Map)mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(UserService.CALLING_FUNCTION_KEY));
        assert(mapArgumentCaptor.getValue().get(UserService.CALLING_FUNCTION_KEY).equals(UserService.USER_EXIST_CHECK));
        assert(mapArgumentCaptor.getValue().containsKey(Repository.REPOSITORY_INTERACTION));
        assert(mapArgumentCaptor.getValue().get(Repository.REPOSITORY_INTERACTION).equals(UserRepository.READ_USER));
    }

    @Test
    public void writeCallbackTest(){
        UserFirestoreDatabase mockFirestoreDatabase = Mockito.mock(UserFirestoreDatabase.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        UserRepository testedRepository = new UserRepository(mockUserService,mockFirestoreDatabase);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserService.CALLING_FUNCTION_KEY,UserService.REGISTER_USER);
        metadata.put(Repository.REPOSITORY_INTERACTION,UserRepository.WRITE_USER);
        String testUid = "testId";
        String testUsername = "testusername";
        User testUser = new User(testUid,testUsername);
        testedRepository.writeCallback(testUser,metadata);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUserService).repositoryResponse((User)userArgumentCaptor.capture(), (Map)mapArgumentCaptor.capture());
        assert(userArgumentCaptor.getValue().getId().equals(testUid));
        assert(userArgumentCaptor.getValue().getUsername().equals(testUsername));
        assert(mapArgumentCaptor.getValue().containsKey(UserService.CALLING_FUNCTION_KEY));
        assert(mapArgumentCaptor.getValue().get(UserService.CALLING_FUNCTION_KEY).equals(UserService.REGISTER_USER));
        assert(mapArgumentCaptor.getValue().containsKey(Repository.REPOSITORY_INTERACTION));
        assert(mapArgumentCaptor.getValue().get(Repository.REPOSITORY_INTERACTION).equals(UserRepository.WRITE_USER));
    }

    @Test
    public void readCallbackTest(){
        UserFirestoreDatabase mockFirestoreDatabase = Mockito.mock(UserFirestoreDatabase.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        UserRepository testedRepository = new UserRepository(mockUserService,mockFirestoreDatabase);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserService.CALLING_FUNCTION_KEY,UserService.USER_EXIST_CHECK);
        metadata.put(Repository.REPOSITORY_INTERACTION,UserRepository.READ_USER);
        String testUid = "testId";
        String testUsername = "testusername";
        User testUser = new User(testUid,testUsername);
        testedRepository.readCallback(testUser,metadata);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUserService).repositoryResponse((User)userArgumentCaptor.capture(), (Map)mapArgumentCaptor.capture());
        assert(userArgumentCaptor.getValue().getId().equals(testUid));
        assert(userArgumentCaptor.getValue().getUsername().equals(testUsername));
        assert(mapArgumentCaptor.getValue().containsKey(UserService.CALLING_FUNCTION_KEY));
        assert(mapArgumentCaptor.getValue().get(UserService.CALLING_FUNCTION_KEY).equals(UserService.USER_EXIST_CHECK));
        assert(mapArgumentCaptor.getValue().containsKey(Repository.REPOSITORY_INTERACTION));
        assert(mapArgumentCaptor.getValue().get(Repository.REPOSITORY_INTERACTION).equals(UserRepository.READ_USER));
    }
}
