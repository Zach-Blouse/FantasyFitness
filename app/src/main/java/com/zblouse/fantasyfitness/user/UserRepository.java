package com.zblouse.fantasyfitness.user;

import android.util.Log;

import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.core.Repository;
import com.zblouse.fantasyfitness.core.RepositoryEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UserRepository implements Repository<User> {


    public static final String CREATE_USER = "createUser";
    private final UserFirestoreDatabase userFirestoreDatabase;
    private final DomainService<User> domainService;

    public UserRepository(DomainService<User> domainService){
        this.userFirestoreDatabase = new UserFirestoreDatabase();
        this.domainService = domainService;
    }

    public void createUser(String userId, String username, Map<String, Object> metadata){
        metadata.put(REPOSITORY_INTERACTION,CREATE_USER);
        User newUser = new User(userId, username);
        userFirestoreDatabase.create(newUser, this, metadata);
    }

    @Override
    public void readCallback(User user, Map<String, Object> metadata) {
        domainService.repositoryResponse(user,metadata);
    }

    @Override
    public void writeCallback(User user, Map<String, Object> metadata) {
        domainService.repositoryResponse(user,metadata);
    }
}
