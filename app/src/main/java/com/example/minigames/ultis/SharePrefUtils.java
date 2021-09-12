package com.example.minigames.ultis;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class SharePrefUtils {

    private static final String USER_ID_EXTRA = "user_id_extra";

    /**
     * Get user id.
     *
     * @param context
     * @return user's id.
     */
    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(USER_ID_EXTRA, null);
    }

    public static void saveUserInfor(Context context, String userId) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(USER_ID_EXTRA, userId);
        editor.apply();
    }
}
