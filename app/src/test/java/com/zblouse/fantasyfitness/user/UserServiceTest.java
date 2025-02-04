package com.zblouse.fantasyfitness.user;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.zblouse.fantasyfitness.MainActivity;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class UserServiceTest {

    @Test
    public void registerUserTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserRepository mockRespository = Mockito.mock(UserRepository.class);
        UserService testedUserService = new UserService(mockActivity, mockRespository);

        testedUserService.registerUser("testId","testusername");
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockRespository).writeUser(eq("testId"),eq("testusername"),(Map<String,Object>)mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(UserService.CALLING_FUNCTION_KEY));
        assert(mapArgumentCaptor.getValue().get(UserService.CALLING_FUNCTION_KEY).equals(UserService.REGISTER_USER));
    }

    @Test
    public void userExistCheckTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserRepository mockRespository = Mockito.mock(UserRepository.class);
        UserService testedUserService = new UserService(mockActivity, mockRespository);

        testedUserService.userExistCheck("testId");
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockRespository).readUser(eq("testId"),(Map<String,Object>)mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(UserService.CALLING_FUNCTION_KEY));
        assert(mapArgumentCaptor.getValue().get(UserService.CALLING_FUNCTION_KEY).equals(UserService.USER_EXIST_CHECK));
    }

    @Test
    public void repositoryResponseRegisterSucceededTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserRepository mockRespository = Mockito.mock(UserRepository.class);
        UserService testedUserService = new UserService(mockActivity,mockRespository);
        User testUser = new User("testUser", "testusername");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserService.CALLING_FUNCTION_KEY,UserService.REGISTER_USER);

        testedUserService.repositoryResponse(testUser,metadata);
        ArgumentCaptor<CreateAccountResponseEvent> createAccountResponseEventArgumentCaptor = ArgumentCaptor.forClass(CreateAccountResponseEvent.class);
        verify(mockActivity).publishEvent((CreateAccountResponseEvent)createAccountResponseEventArgumentCaptor.capture());
        assert(createAccountResponseEventArgumentCaptor.getValue().isAccountCreated());
    }

    @Test
    public void repositoryResponseRegisterFailedTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserRepository mockRespository = Mockito.mock(UserRepository.class);
        UserService testedUserService = new UserService(mockActivity,mockRespository);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserService.CALLING_FUNCTION_KEY,UserService.REGISTER_USER);

        testedUserService.repositoryResponse(null,metadata);
        ArgumentCaptor<CreateAccountResponseEvent> createAccountResponseEventArgumentCaptor = ArgumentCaptor.forClass(CreateAccountResponseEvent.class);
        verify(mockActivity).publishEvent((CreateAccountResponseEvent)createAccountResponseEventArgumentCaptor.capture());
        assert(!createAccountResponseEventArgumentCaptor.getValue().isAccountCreated());
    }

    @Test
    public void repositoryResponseUserExistExistsTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserRepository mockRespository = Mockito.mock(UserRepository.class);
        UserService testedUserService = new UserService(mockActivity,mockRespository);
        User testUser = new User("testUser", "testusername");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserService.CALLING_FUNCTION_KEY,UserService.USER_EXIST_CHECK);

        testedUserService.repositoryResponse(testUser,metadata);
        ArgumentCaptor<UserExistEvent> userExistEventArgumentCaptor = ArgumentCaptor.forClass(UserExistEvent.class);
        verify(mockActivity).publishEvent((UserExistEvent)userExistEventArgumentCaptor.capture());
        assert(userExistEventArgumentCaptor.getValue().exists());
    }

    @Test
    public void repositoryResponseUserExistDoesNotExistTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserRepository mockRespository = Mockito.mock(UserRepository.class);
        UserService testedUserService = new UserService(mockActivity,mockRespository);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserService.CALLING_FUNCTION_KEY,UserService.USER_EXIST_CHECK);

        testedUserService.repositoryResponse(null,metadata);
        ArgumentCaptor<UserExistEvent> userExistEventArgumentCaptor = ArgumentCaptor.forClass(UserExistEvent.class);
        verify(mockActivity).publishEvent((UserExistEvent)userExistEventArgumentCaptor.capture());
        assert(!userExistEventArgumentCaptor.getValue().exists());
    }
}
