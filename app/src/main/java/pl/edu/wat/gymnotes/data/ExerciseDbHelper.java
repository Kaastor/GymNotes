package pl.edu.wat.gymnotes.data;

import pl.edu.wat.gymnotes.data.ExerciseContract.ExerciseEntry;
import pl.edu.wat.gymnotes.data.ExerciseContract.PracticeEntry;
import pl.edu.wat.gymnotes.data.ExerciseContract.UserEntry;
import pl.edu.wat.gymnotes.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExerciseDbHelper extends SQLiteOpenHelper {

    private Logger logger = Logger.getLogger(ExerciseDbHelper.class.toString());

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "gymnotes.db";

    public ExerciseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY," +
                UserEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                " UNIQUE (" + UserEntry.COLUMN_EMAIL +
                " ));";

        final String SQL_CREATE_EXERCISE_TABLE = "CREATE TABLE " + ExerciseEntry.TABLE_NAME + " (" +
                ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
                ExerciseEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ExerciseEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                " UNIQUE (" + ExerciseEntry.COLUMN_NAME +
                " ));";

        final String SQL_CREATE_PRACTICE_TABLE = "CREATE TABLE " + PracticeEntry.TABLE_NAME + " (" +
                PracticeEntry._ID + " INTEGER PRIMARY KEY," +
                PracticeEntry.COLUMN_USER_KEY + " INTEGER NOT NULL, " +
                PracticeEntry.COLUMN_EX_KEY + " INTEGER NOT NULL, " +
                PracticeEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                PracticeEntry.COLUMN_SERIES + " INTEGER NOT NULL, " +
                PracticeEntry.COLUMN_REPS + " INTEGER NOT NULL, " +
                " UNIQUE (" + PracticeEntry.COLUMN_USER_KEY + ", " +
                PracticeEntry.COLUMN_EX_KEY + ", " +
                PracticeEntry.COLUMN_DATE + "), " +

                " FOREIGN KEY (" + PracticeEntry.COLUMN_EX_KEY + ") REFERENCES " +
                ExerciseEntry.TABLE_NAME + " (" + ExerciseEntry._ID + ") " +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EXERCISE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PRACTICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PracticeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addUser(User user){
        logger.log(Level.INFO, "addUser: " + user );
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_NAME, user.getName());
        contentValues.put(UserEntry.COLUMN_EMAIL, user.getEmail());
        contentValues.put(UserEntry.COLUMN_PASSWORD, user.getPassword());

        database.insertWithOnConflict(UserEntry.TABLE_NAME, null, contentValues,  SQLiteDatabase.CONFLICT_IGNORE);
        database.close();
    }
    public boolean checkUser(String email){
        logger.log(Level.INFO, "checkUser: " + email );
        String[] columns = {
                UserEntry._ID
        };
        SQLiteDatabase database = this.getWritableDatabase();
        String selection = UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = database.query(UserEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        database.close();

        return cursorCount == 1;
    }

    public String getUserId(String email){
        logger.log(Level.INFO, "getUserId: " + email );
        String userId = "";
        String[] columns = {
                UserEntry._ID, UserEntry.COLUMN_EMAIL
        };
        SQLiteDatabase database = this.getWritableDatabase();
        String selection = UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = database.query(UserEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex(UserEntry._ID));
        }
        cursor.close();
        return userId;
    }

    public int getExerciseId(String name){
        logger.log(Level.INFO, "getExerciseId: " + name );
        String exerciseId = "";
        String[] columns = {
                ExerciseEntry._ID, ExerciseEntry.COLUMN_NAME
        };
        SQLiteDatabase database = this.getWritableDatabase();
        String selection = ExerciseEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs = { name };

        Cursor cursor = database.query(ExerciseEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            exerciseId = cursor.getString(cursor.getColumnIndex(ExerciseEntry._ID));
        }
        cursor.close();
        return Integer.parseInt(exerciseId);
    }

    public String getUserName(String email){
        logger.log(Level.INFO, "getUserName: " + email );
        String userName = "";
        String[] columns = {
                UserEntry._ID, UserEntry.COLUMN_NAME
        };
        SQLiteDatabase database = this.getWritableDatabase();
        String selection = UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = database.query(UserEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME));
        }

        cursor.close();
        database.close();

        return userName;
    }

    public boolean checkUser(String email, String password){
        logger.log(Level.INFO, "checkUser: " + email + " " + password );
        String[] columns = {
                UserEntry._ID
        };
        SQLiteDatabase database = this.getWritableDatabase();
        String selection = UserEntry.COLUMN_EMAIL + " = ? AND " + UserEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };

        Cursor cursor = database.query(UserEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        database.close();

        return cursorCount > 0;
    }
}
