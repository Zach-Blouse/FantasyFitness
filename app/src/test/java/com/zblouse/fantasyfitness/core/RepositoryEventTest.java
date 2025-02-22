package com.zblouse.fantasyfitness.core;

import static org.junit.Assert.assertEquals;

import com.zblouse.fantasyfitness.user.User;

import org.junit.Test;

import java.util.HashMap;

public class RepositoryEventTest {

    @Test
    public void repositoryEventTest(){
        User user = new User("id1","testUsername1");
        RepositoryEvent<User> testedEvent = new RepositoryEvent<>(EventType.USER_EXIST_EVENT,user,new HashMap<>());
        User resultUser = testedEvent.getRepositoryResponseObject();
        assertEquals(user, resultUser);
    }
}
