package pl.edu.wat.gymnotes.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExerciseContract {

    public static final String CONTENT_AUTHORITY = "pl.edu.wat.gymnotes.app";
    public static final Uri BASE_CONTENT_URI  = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EXERCISE = "exercise";
    public static final String PATH_PRACTICE = "practice";

    public static final String PATH_DIST_PRACTICE = "distinctPractice";


    public static final class ExerciseEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.
                buildUpon().appendPath(PATH_EXERCISE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;

        public static final String TABLE_NAME = "exercise";
        public static final String COLUMN_NAME = "name";
//        public static final String COLUMN_PICTURE_BEFORE = "pic_before";
//        public static final String COLUMN_PICTURE_AFTER = "pic_after";
        public static final String COLUMN_DESCRIPTION = "description";


        public static Uri buildExerciseUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildExerciseForName(String name) {
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }

        public static Uri buildExercises() {
            return CONTENT_URI.buildUpon().build();
        }
    }


    public static final class PracticeEntry implements BaseColumns{

        public static final Uri CONTENT_DIST_URI = BASE_CONTENT_URI.
                buildUpon().appendPath(PATH_DIST_PRACTICE).build();

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.
                buildUpon().appendPath(PATH_PRACTICE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRACTICE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRACTICE;

        public static final String TABLE_NAME = "practice";
        public static final String COLUMN_EX_KEY = "exercise_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_SERIES = "series";
        public static final String COLUMN_REPS = "reps";

        public static Uri buildPracticeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPracticeForDate(String date) {
            return CONTENT_URI.buildUpon().appendPath(date).build();
        }

        public static Uri buildPractices() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildDistinctPracticesDates() {
            return CONTENT_DIST_URI.buildUpon().build();
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }



}
