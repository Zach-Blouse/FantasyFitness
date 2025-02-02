package com.zblouse.fantasyfitness.user;

public class UserService {

    private UserRepository userRepository;

    public UserService(){
        userRepository = new UserRepository();
    }

    public void registerUser(String firebaseUid, String username){
        //TODO make sure the user does not already exist
        userRepository.createUser(firebaseUid, username);
    }
}
