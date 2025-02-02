package com.zblouse.fantasyfitness.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.zblouse.fantasyfitness.core.FirestoreDatabase;
import com.zblouse.fantasyfitness.core.Repository;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UserFirestoreDatabase extends FirestoreDatabase {

    private static final String USER_ID_FIELD = "UID";
    private static final String USERNAME_FIELD = "USERNAME";
    private static final String COLLECTION = "users";

    public UserFirestoreDatabase(){
        super();
    }

    public void create(User user, Repository<User> repository, Map<String, Object> metadata){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put(USER_ID_FIELD,user.getId());
        newUser.put(USERNAME_FIELD,user.getUsername());
        firestore.collection(COLLECTION).document(user.getId()).set(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    repository.writeCallback(user, metadata);
                } else {
                    repository.writeCallback(null, metadata);
                }
            }
        });
    }

    public void read(String userId, Repository<User> repository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();
                    User returnedUser = new User(result.get(USER_ID_FIELD,String.class), result.get(USERNAME_FIELD,String.class));
                    repository.readCallback(returnedUser, metadata);
                } else {
                    repository.readCallback(null, metadata);
                }
            }
        });
    }
}
