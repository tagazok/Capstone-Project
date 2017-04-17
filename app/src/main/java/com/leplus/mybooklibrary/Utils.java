package com.leplus.mybooklibrary;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by olivier on 17/04/2017.
 */

public class Utils {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
