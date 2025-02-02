package com.zblouse.fantasyfitness.user;

import android.util.Log;

import com.zblouse.fantasyfitness.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.HashMap;
import java.util.Map;

public class UserService implements DomainService<User> {

    private final UserRepository userRepository;
    private final MainActivity activity;

    public UserService(MainActivity activity){
        userRepository = new UserRepository(this);
        this.activity = activity;
    }

    public void registerUser(String firebaseUid, String username){
        //TODO make sure the user does not already exist
        Map<String, Object> metadata = new HashMap<>();
        userRepository.createUser(firebaseUid, username, metadata);
    }

    @Override
    public void repositoryResponse(User responseBody, Map<String, Object> metadata) {
        if(metadata.containsKey(Repository.REPOSITORY_INTERACTION)){
            if(metadata.get(Repository.REPOSITORY_INTERACTION).equals(UserRepository.CREATE_USER)){
                if(responseBody==null){
                    CreateAccountResponseEvent createAccountResponseEvent = new CreateAccountResponseEvent(false);
                    activity.publishEvent(createAccountResponseEvent);
                }else {
                    CreateAccountResponseEvent createAccountResponseEvent = new CreateAccountResponseEvent(true);
                    activity.publishEvent(createAccountResponseEvent);
                }
            }
        } else {
            Log.e(this.getClass().toString(),"Error: Repository Response does not contain Repository Interaction metadata");
        }
    }
}
