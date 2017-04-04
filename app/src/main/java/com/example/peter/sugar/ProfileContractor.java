package com.example.peter.sugar;

import android.provider.BaseColumns;

/**
 * Created by Peter on 04.04.2017.
 */

public final class ProfileContractor {

    /* Don't initialize UNLESS needed! */
    private ProfileContractor() {}

    /* Table contents of the schema 'PROFILE' */
    public static class ProfileEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "PROFILE";
        public static final String COLUMN_PROFILE_NAME = "PROFILE_NAME";
        public static final String COLUMN_PROFILE_START = "PROFILE_START";
        public static final String COLUMN_PROFILE_END = "PROFILE_END";
        public static final String COLUMN_PROFILE_CONTACTS = "PROFILE_CONTACTS";
        public static final String COLUMN_PROFILE_ACTIVE = "PROFILE_ACTIVE";
    }
}
