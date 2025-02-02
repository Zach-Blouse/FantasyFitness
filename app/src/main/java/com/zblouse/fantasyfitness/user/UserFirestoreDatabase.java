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

public class UserFirestoreDatabase extends FirestoreDatabase {

    private static final String USER_ID_FIELD = "UID";
    private static final String USERNAME_FIELD = "USERNAME";
    private static final String COLLECTION = "users";

    public UserFirestoreDatabase(){
        super();
    }

    public void create(User user, Repository<User> repository){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put(USER_ID_FIELD,user.getId());
        newUser.put(USERNAME_FIELD,user.getUsername());
        firestore.collection(COLLECTION).document(user.getId()).set(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    repository.writeCallback(user);
                } else {
                    repository.writeCallback(null);
                }
            }
        });
    }

    public void read(int userId, Repository<User> repository){
        firestore.collection(COLLECTION).document(String.valueOf(userId)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();
                    User returnedUser = new User(result.get(USER_ID_FIELD,String.class), result.get(USERNAME_FIELD,String.class));
                    repository.readCallback(returnedUser);
                } else {
                    repository.readCallback(null);
                }
            }
        });
    }
}
