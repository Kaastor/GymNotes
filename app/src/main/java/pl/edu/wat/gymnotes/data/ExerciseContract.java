package pl.edu.wat.gymnotes.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.edu.wat.gymnotes.model.User;

public class ExerciseContract {

    static final String CONTENT_AUTHORITY = "pl.edu.wat.gymnotes.app";
    static final Uri BASE_CONTENT_URI  = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_EXERCISE = "exercise";
    static final String PATH_PRACTICE = "practice";
    static final String PATH_USER = "user";

    static final String PATH_DIST_PRACTICE = "distinctPractice";
    static final String PATH_DEL_PRACTICE = "distinctPractice";

    static final class UserEntry implements BaseColumns{

        static final Uri CONTENT_URI = BASE_CONTENT_URI.
                buildUpon().appendPath(PATH_USER).build();

        static final String TABLE_NAME = "user";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_EMAIL = "email";
        static final String COLUMN_PASSWORD = "password";

    }

    public static final class ExerciseEntry implements BaseColumns{

        static final Uri CONTENT_URI = BASE_CONTENT_URI.
                buildUpon().appendPath(PATH_EXERCISE).build();
        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;

        static final String TABLE_NAME = "exercise";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";


        static Uri buildExerciseUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static Uri buildExerciseForName(String name) {
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }

        public static Uri buildExercises() {
            return CONTENT_URI.buildUpon().build();
        }
    }


    public static final class PracticeEntry implements BaseColumns{

        static final Uri CONTENT_DIST_URI = BASE_CONTENT_URI.
                buildUpon().appendPath(PATH_DIST_PRACTICE).build();

        static final Uri CONTENT_DEL_URI = BASE_CONTENT_URI.
                buildUpon().appendPath(PATH_DEL_PRACTICE).build();

        static final Uri CONTENT_URI = BASE_CONTENT_URI.
                buildUpon().appendPath(PATH_PRACTICE).build();
        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRACTICE;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRACTICE;

        public static final String TABLE_NAME = "practice";
        public static final String COLUMN_USER_KEY = "user_id";
        public static final String COLUMN_EX_KEY = "exercise_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_SERIES = "series";
        public static final String COLUMN_REPS = "reps";

        static Uri buildPracticeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPracticeForDateAndUser(String email, String date) {
            return CONTENT_URI.buildUpon().appendPath(email).appendPath(date).build();
        }

        public static Uri buildPracticeForId(String id) {
            return CONTENT_DEL_URI.buildUpon().appendPath(id).build();
        }

        public static Uri buildPractices() {
            return CONTENT_URI.buildUpon().build();
        }

        static Uri buildDistinctPracticesDates() {
            return CONTENT_DIST_URI.buildUpon().build();
        }

        static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
        static String getUserFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }



}
