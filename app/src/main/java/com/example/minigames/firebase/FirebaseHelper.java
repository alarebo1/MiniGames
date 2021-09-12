package com.example.minigames.firebase;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.example.minigames.BuildConfig;
import com.example.minigames.firebase.dao.UserDao;

public class FirebaseHelper {

    public static final String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";

    private static FirebaseHelper INSTANCE;
    public static FirebaseHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (FirebaseHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FirebaseHelper();
                }
            }
        }
        return INSTANCE;
    }

    private FirebaseDatabase firebaseDatabase;
    private StorageReference StorageRef;
    private final UserDao userDao;

    public FirebaseHelper() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        StorageRef = FirebaseStorage.getInstance().getReference();
        firebaseDatabase.setPersistenceEnabled(true);
        if (BuildConfig.DEBUG) {
            firebaseDatabase.setLogLevel(Logger.Level.DEBUG);
        }
        userDao = new UserDao(firebaseDatabase, StorageRef);
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
