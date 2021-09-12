package com.example.minigames.firebase;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public final class FirebaseUtils {

    /**
     * Get values from HashMap<Key, Value> of Firebase result.
     *
     * @param dataSnapshot {@link DataSnapshot}
     * @param tClass
     *
     * @return List value.
     * */
    public static <T> List<T> getListValue(DataSnapshot dataSnapshot, Class<T> tClass) {
        List<T> listValues = new ArrayList<>();
        for(DataSnapshot data: dataSnapshot.getChildren()) {
            listValues.add(data.getValue(tClass));
        }

        return listValues;
    }
}
