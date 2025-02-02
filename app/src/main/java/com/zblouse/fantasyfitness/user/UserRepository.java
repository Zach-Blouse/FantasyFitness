package com.zblouse.fantasyfitness.user;

import android.os.Parcelable;
import android.util.Log;

import com.zblouse.fantasyfitness.core.Repository;

public class UserRepository implements Repository<User> {

    UserFirestoreDatabase userFirestoreDatabase;

    public UserRepository(){
        userFirestoreDatabase = new UserFirestoreDatabase();
    }

    public void createUser(String userId, String username){
        User newUser = new User(userId, username);
        userFirestoreDatabase.create(newUser, this);
    }

    @Override
    public void readCallback(User user) {
        if(user == null){
            Log.e("UserRepository", "failed to fetch user");
        }
    }

    @Override
    public void writeCallback(User user) {
        if(user == null){
            Log.e("UserRepository", "failed to write user");
        }
    }
}
