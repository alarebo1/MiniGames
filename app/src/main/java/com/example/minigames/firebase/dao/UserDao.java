package com.example.minigames.firebase.dao;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import com.example.minigames.MyApplication;
import com.example.minigames.R;
import com.example.minigames.firebase.FirebaseUtils;
import com.example.minigames.firebase.model.User;
import com.example.minigames.ultis.Constants;

public class UserDao {

    public static final String TAG = UserDao.class.getSimpleName();

    private FirebaseAuth mAuth;
    protected FirebaseDatabase firebase;
    private StorageReference StorageRef;

    public UserDao(FirebaseDatabase firebase, StorageReference StorageRef) {
        this.firebase = firebase;
        mAuth = FirebaseAuth.getInstance();
        this.StorageRef = StorageRef;
    }

    /**
     * Sign in firebase with
     *
     * @param email
     * @param password
     */
    public LiveData<Task<AuthResult>> signIn(String email, String password, boolean isNeedVerify) {
        MediatorLiveData<Task<AuthResult>> liveData = new MediatorLiveData<>();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (isNeedVerify && !user.isEmailVerified()) {
                            Log.d(TAG, "not isEmailVerified");
                            liveData.addSource(sendVerifyEmai(user), authResultTask -> {
                                liveData.setValue(authResultTask);
                            });
                        } else {
                            Log.d(TAG, "isEmailVerified");
                            // Sign in success, update UI with the signed-in user's information
                            liveData.setValue(task);
                        }
                    } else {
                        Log.d(TAG, "send fail " + task.getException());
                        Task<AuthResult> resultTask = new Task<AuthResult>() {
                            @Override
                            public boolean isComplete() {
                                return true;
                            }

                            @Override
                            public boolean isSuccessful() {
                                return false;
                            }

                            @Override
                            public boolean isCanceled() {
                                return false;
                            }

                            @Nullable
                            @Override
                            public AuthResult getResult() {
                                return null;
                            }

                            @Nullable
                            @Override
                            public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
                                return null;
                            }

                            @Nullable
                            @Override
                            public Exception getException() {
                                return task.getException();
                            }

                            @NonNull
                            @Override
                            public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                                return null;
                            }

                            @NonNull
                            @Override
                            public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                                return null;
                            }

                            @NonNull
                            @Override
                            public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                                return null;
                            }

                            @NonNull
                            @Override
                            public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                                return null;
                            }

                            @NonNull
                            @Override
                            public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                                return null;
                            }

                            @NonNull
                            @Override
                            public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                                return null;
                            }
                        };
                        liveData.setValue(resultTask);
                    }
                });
        return liveData;
    }

    /**
     * Sign up firebase with
     *
     * @param email
     * @param password
     */
    public LiveData<Task<AuthResult>> signUp(String email, String password, String name, boolean isNeedVerify) {
        MediatorLiveData<Task<AuthResult>> liveData = new MediatorLiveData<>();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(command -> {
                                    if (command.isSuccessful()) {
                                        Log.d(TAG, "update name success");
                                    }
                                });

                        if (isNeedVerify) {
                            Log.d(TAG, "not isEmailVerified");

                            liveData.addSource(sendVerifyEmai(user), authResultTask -> {
                                liveData.setValue(authResultTask);
                            });
                        } else {
                            liveData.setValue(task);
                        }
                    }
                });
        return liveData;
    }

    private LiveData<Task<AuthResult>> sendVerifyEmai(FirebaseUser user) {
        MediatorLiveData<Task<AuthResult>> liveData = new MediatorLiveData<>();
        String url = "http://www.example.com/verify?uid=" + user.getUid();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setHandleCodeInApp(false)
                // The default for this is populated with the current android package name
                .setAndroidPackageName("binh.le.game", false, null)
                .build();

        user.sendEmailVerification(actionCodeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Email sent, after email is sent just logout the user and finish this activity
                    FirebaseAuth.getInstance().signOut();
                    Log.d(TAG, "send isSuccessful");
                    liveData.setValue(null);
                } else {
                    // Email not sent, so display message and restart the activity or do whatever you wish to do
                    Log.d(TAG, "send fail " + task.getException());
                    Task<AuthResult> resultTask = new Task<AuthResult>() {
                        @Override
                        public boolean isComplete() {
                            return true;
                        }

                        @Override
                        public boolean isSuccessful() {
                            return false;
                        }

                        @Override
                        public boolean isCanceled() {
                            return false;
                        }

                        @Nullable
                        @Override
                        public AuthResult getResult() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
                            return null;
                        }

                        @Nullable
                        @Override
                        public Exception getException() {
                            return task.getException();
                        }

                        @NonNull
                        @Override
                        public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                            return null;
                        }

                        @NonNull
                        @Override
                        public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                            return null;
                        }

                        @NonNull
                        @Override
                        public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                            return null;
                        }

                        @NonNull
                        @Override
                        public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                            return null;
                        }

                        @NonNull
                        @Override
                        public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                            return null;
                        }

                        @NonNull
                        @Override
                        public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                            return null;
                        }
                    };
                    liveData.setValue(resultTask);
                }
            }
        });
        return liveData;
    }

    /**
     * Update {@link User} info
     *
     * @param userId *
     */
    public LiveData<Boolean> updateUserInfo(String userId, String email, String name) {
        MediatorLiveData<Boolean> result = new MediatorLiveData<>();
        firebase.getReference(Constants.USER_PATH)
                .child(userId).updateChildren(
                new User(userId, email, name).toMap(), (databaseError, databaseReference) -> {
                    result.postValue(true);
                });
        return result;
    }

    public void updateUserName(String userName) {
        String uid = mAuth.getCurrentUser().getUid();
        firebase.getReference(Constants.USER_PATH).child(uid)
                .child(Constants.User.NAME).setValue(userName);
    }

    public void updateGamePoint(int game, long gamePoint) {
        String gamePath = Constants.User.SCORE_GAME1;
        switch (game) {
            case 1:
                gamePath = Constants.User.SCORE_GAME1;
                break;
            case 2:
                gamePath = Constants.User.SCORE_GAME2;
                break;
            case 3:
                gamePath = Constants.User.SCORE_GAME3;
                break;
            case 4:
                gamePath = Constants.User.SCORE_GAME4;
                break;
            case 5:
                gamePath = Constants.User.SCORE_GAME5;
                break;
            case 6:
                gamePath = Constants.User.CURRENCY;

                break;
            default:
                gamePath = Constants.User.SCORE_GAME1;

        }

        String uid = mAuth.getCurrentUser().getUid();
        firebase.getReference(Constants.USER_PATH).child(uid)
                .child(gamePath).setValue(gamePoint);
    }

    public LiveData<String> updateImage(Bitmap bitmap) {
        MediatorLiveData<String> result = new MediatorLiveData<>();
        String uid = mAuth.getCurrentUser().getUid();
        StorageReference imageRef = StorageRef.child(uid);
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();
        List<UploadTask> tasks = imageRef.getActiveUploadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the upload
            UploadTask task = tasks.get(0);
            if (!task.isComplete()) {
                result.postValue(null);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);

        uploadTask.addOnFailureListener(e -> {
            result.postValue(e.getMessage());
        });

        uploadTask.addOnCanceledListener(() -> {
            result.postValue("Cancel upload.");
        });

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Do nothing
        });

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                result.postValue(task.getResult().toString());
            } else {
                result.postValue(task.getException().getMessage());
            }
        });
        return result;
    }

    public LiveData<User> getUser(String userId) {
        MediatorLiveData<User> liveData = new MediatorLiveData<>();
        if (!TextUtils.isEmpty(userId)) {
            DatabaseReference user = firebase.getReference(Constants.USER_PATH)
                    .child(userId);
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        liveData.setValue(dataSnapshot.getValue(User.class));
                    } else {
                        liveData.setValue(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return liveData;
    }

    public LiveData<List<User>> getTopUsers(int game) {
        MediatorLiveData<List<User>> result = new MediatorLiveData<>();
        String orderChild;
        switch (game) {
            case 1: {
                orderChild = Constants.User.SCORE_GAME1;
                break;
            }
            case 2: {
                orderChild = Constants.User.SCORE_GAME2;
                break;
            }
            case 3: {
                orderChild = Constants.User.SCORE_GAME3;
                break;
            }
            case 4: {
                orderChild = Constants.User.SCORE_GAME4;
                break;
            }
            case 5: {
                orderChild = Constants.User.SCORE_GAME5;
                break;
            }
            default:
                orderChild = Constants.User.SCORE_GAME1;
        }
        firebase.getReference(Constants.USER_PATH).orderByChild(orderChild)
                .startAt(1, orderChild)
                .limitToLast(10)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            List<User> users = FirebaseUtils.getListValue(dataSnapshot, User.class);
                            result.setValue(users);
                        } else {
                            result.setValue(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return result;
    }

    public void keepSyncedData() {
        firebase.getReference(Constants.USER_PATH).keepSynced(true);
    }
}
