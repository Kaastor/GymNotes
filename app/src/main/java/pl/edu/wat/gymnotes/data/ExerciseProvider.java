package pl.edu.wat.gymnotes.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.activities.AboutActivity;

public class ExerciseProvider extends ContentProvider{

    private Logger logger = Logger.getLogger(ExerciseProvider.class.toString());

    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private ExerciseDbHelper exerciseDbHelper;

    private static final SQLiteQueryBuilder sPracticeWithExerciseQueryBuilder;
    static{
        sPracticeWithExerciseQueryBuilder = new SQLiteQueryBuilder();
        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sPracticeWithExerciseQueryBuilder.setTables(
                ExerciseContract.PracticeEntry.TABLE_NAME + " AS p INNER JOIN " +
                        ExerciseContract.ExerciseEntry.TABLE_NAME +
                        " AS e ON " +
                        "p." + ExerciseContract.PracticeEntry.COLUMN_EX_KEY +
                        " = " +
                        "e." + ExerciseContract.ExerciseEntry._ID);
    }

    static final int EXERCISE = 100;
    static final int EXERCISE_FOR_NAME = 101;
    static final int PRACTICE = 200;
    static final int PRACTICE_WITH_USER_AND_DATE = 201;
    static final int PRACTICE_DISTINCT = 202;
    static final int PRACTICE_WITH_ID = 203;

    static final int USER = 300;

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ExerciseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ExerciseContract.PATH_USER, USER);

        matcher.addURI(authority, ExerciseContract.PATH_EXERCISE, EXERCISE);
        matcher.addURI(authority, ExerciseContract.PATH_EXERCISE + "/*", EXERCISE_FOR_NAME);

        matcher.addURI(authority, ExerciseContract.PATH_PRACTICE, PRACTICE);
        matcher.addURI(authority, ExerciseContract.PATH_PRACTICE + "/*/*", PRACTICE_WITH_USER_AND_DATE);
        matcher.addURI(authority, ExerciseContract.PATH_DIST_PRACTICE , PRACTICE_DISTINCT);
        matcher.addURI(authority, ExerciseContract.PATH_DEL_PRACTICE + "/*", PRACTICE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        exerciseDbHelper = new ExerciseDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        logger.log(Level.INFO, "query: uri: " + uri + " projection: " + projection + " selection: " + selection + " selectionArgs:" + selectionArgs
                + " sortOrder: " + sortOrder);
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)){
            case EXERCISE:
                retCursor = exerciseDbHelper.getReadableDatabase().query(
                        ExerciseContract.ExerciseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRACTICE_WITH_USER_AND_DATE:
                retCursor = getPracticeByUserAndDate(uri, projection, sortOrder);
                break;
            case PRACTICE_DISTINCT:
                retCursor = getDistinctPracticesDates(uri, projection, sortOrder);
                break;
            case PRACTICE:
                retCursor = exerciseDbHelper.getReadableDatabase().query(
                        ExerciseContract.PracticeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case EXERCISE:
                return ExerciseContract.ExerciseEntry.CONTENT_TYPE;
            case EXERCISE_FOR_NAME:
                return ExerciseContract.ExerciseEntry.CONTENT_ITEM_TYPE;
            case PRACTICE:
                return ExerciseContract.PracticeEntry.CONTENT_TYPE;
            case PRACTICE_WITH_USER_AND_DATE:
                return ExerciseContract.PracticeEntry.CONTENT_TYPE;
            case PRACTICE_WITH_ID:
                return ExerciseContract.PracticeEntry.CONTENT_ITEM_TYPE;
            default:
                throw  new UnsupportedOperationException("Unknown uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        logger.log(Level.INFO, "insert: uri: " + uri + " concentValues: " + contentValues);
        final SQLiteDatabase database = exerciseDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case PRACTICE: {
                long _id = database.insert(ExerciseContract.PracticeEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = ExerciseContract.PracticeEntry.buildPracticeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case EXERCISE: {
                long _id = database.insert(ExerciseContract.ExerciseEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = ExerciseContract.ExerciseEntry.buildExerciseUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        database.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        logger.log(Level.INFO, "delete: uri: " + uri + " selection: " + selection + " selectionArgs:" + selectionArgs);
        final SQLiteDatabase database = exerciseDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case PRACTICE:
                rowsDeleted = database.delete(
                        ExerciseContract.PracticeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRACTICE_WITH_ID:
                rowsDeleted = deletePracticeById(new String[]{uri.getLastPathSegment()});
                break;
            case EXERCISE:
                rowsDeleted = database.delete(
                        ExerciseContract.ExerciseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = database.delete(
                        ExerciseContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        logger.log(Level.INFO, "update: uri: " + uri  + " selection: " + selection + " selectionArgs:" + selectionArgs);
        final SQLiteDatabase database = exerciseDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case PRACTICE:
                rowsUpdated = database.update(ExerciseContract.PracticeEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case EXERCISE:
                rowsUpdated = database.update(ExerciseContract.ExerciseEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = exerciseDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRACTICE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ExerciseContract.PracticeEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private static final String sPracticeDateSelection =
            "p"+  "." + ExerciseContract.PracticeEntry.COLUMN_USER_KEY + " = ? AND " + "p"+  "." +
                    ExerciseContract.PracticeEntry.COLUMN_DATE+ " = ? ";

    private Cursor getPracticeByUserAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String date = ExerciseContract.PracticeEntry.getDateFromUri(uri);
        String user = ExerciseContract.PracticeEntry.getUserFromUri(uri);//getID
        String userId = exerciseDbHelper.getUserId(user);
        return sPracticeWithExerciseQueryBuilder.query(exerciseDbHelper.getReadableDatabase(),
                projection,
                sPracticeDateSelection,
                new String[]{userId, date},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDistinctPracticesDates(
            Uri uri, String[] projection, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ExerciseContract.PracticeEntry.TABLE_NAME);
        queryBuilder.setDistinct(true);
        return queryBuilder.query(
                exerciseDbHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    private int deletePracticeById(String[] id){
        return getContext().getContentResolver().delete(
                ExerciseContract.PracticeEntry.CONTENT_URI,
                "_ID=?",
                id
        );
    }
}
