package com.zblouse.fantasyfitness.user;

import android.util.Log;

import com.zblouse.fantasyfitness.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.HashMap;
import java.util.Map;

public class UserService implements DomainService<User> {

    public static final String CALLING_FUNCTION_KEY = "callingFunctionKey";
    public static final String REGISTER_USER = "registerUser";
    public static final String USER_EXIST_CHECK = "userExistCheck";
    private UserRepository userRepository;
    private MainActivity activity;

    public UserService(MainActivity activity){
        userRepository = new UserRepository(this);
        this.activity = activity;
    }

    public UserService(MainActivity activity, UserRepository userRepository){
        this.userRepository = userRepository;
        this.activity = activity;
    }

    public UserService(){

    }

    public void setMainActivity(MainActivity mainActivity){
        this.activity = mainActivity;
    }

    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void registerUser(String firebaseUid, String username){
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(CALLING_FUNCTION_KEY,REGISTER_USER);
        userRepository.writeUser(firebaseUid, username, metadata);
    }

    public void userExistCheck(String firebaseUid){
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(CALLING_FUNCTION_KEY,USER_EXIST_CHECK);
        userRepository.readUser(firebaseUid,metadata);
    }

    @Override
    public void repositoryResponse(User responseBody, Map<String, Object> metadata) {
        if(metadata.containsKey(CALLING_FUNCTION_KEY)){
            if(metadata.get(CALLING_FUNCTION_KEY).equals(REGISTER_USER)){
                if(responseBody==null){
                    CreateAccountResponseEvent createAccountResponseEvent = new CreateAccountResponseEvent(false);
                    activity.publishEvent(createAccountResponseEvent);
                }else {
                    CreateAccountResponseEvent createAccountResponseEvent = new CreateAccountResponseEvent(true);
                    activity.publishEvent(createAccountResponseEvent);
                }
            } else if(metadata.get(CALLING_FUNCTION_KEY).equals(USER_EXIST_CHECK)){
                if(responseBody == null){
                    UserExistEvent event = new UserExistEvent(false);
                    activity.publishEvent(event);
                } else {
                    UserExistEvent event = new UserExistEvent(true);
                    activity.publishEvent(event);
                }
            }
        } else {
            Log.e(this.getClass().toString(),"Error: Repository Response does not contain Repository Interaction metadata");
        }
    }
}
