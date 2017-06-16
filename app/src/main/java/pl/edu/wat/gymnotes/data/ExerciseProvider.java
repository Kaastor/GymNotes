package pl.edu.wat.gymnotes.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ExerciseProvider extends ContentProvider{

    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private ExerciseDbHelper exerciseDbHelper;

    static final int EXERCISES = 100;
    static final int EXERCISE_FOR_ID = 101;
    static final int PRACTICE = 200;
    static final int PRACTICES = 201;
    static final int PRACTICE_WITH_DATE = 202;

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ExerciseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ExerciseContract.PATH_EXERCISE, EXERCISES);
//        matcher.addURI(authority, ExerciseContract.PATH_EXERCISE + "/#", EXERCISE_FOR_ID);

        matcher.addURI(authority, ExerciseContract.PATH_PRACTICE, PRACTICES);
        matcher.addURI(authority, ExerciseContract.PATH_PRACTICE + "/*", PRACTICE_WITH_DATE);

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
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case EXERCISES:
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
            case PRACTICES:
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
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case EXERCISES:
                return ExerciseContract.ExerciseEntry.CONTENT_TYPE;
            case PRACTICES:
                return ExerciseContract.PracticeEntry.CONTENT_TYPE;
            case PRACTICE_WITH_DATE:
                return ExerciseContract.PracticeEntry.CONTENT_TYPE;
            default:
                throw  new UnsupportedOperationException("Unknwn uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
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
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        database.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
