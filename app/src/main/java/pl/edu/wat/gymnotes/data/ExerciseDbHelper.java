package pl.edu.wat.gymnotes.data;

import pl.edu.wat.gymnotes.data.ExerciseContract.ExerciseEntry;
import pl.edu.wat.gymnotes.data.ExerciseContract.PracticeEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExerciseDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "gymnotes.db";
    static Context context;

    public ExerciseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EXERCISE_TABLE = "CREATE TABLE " + ExerciseEntry.TABLE_NAME + " (" +
                ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
                ExerciseEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ExerciseEntry.COLUMN_NAME + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_PRACTICE_TABLE = "CREATE TABLE " + PracticeEntry.TABLE_NAME + " (" +
                PracticeEntry._ID + " INTEGER PRIMARY KEY," +
                PracticeEntry.COLUMN_EX_KEY + " INTEGER NOT NULL, " +
                PracticeEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                PracticeEntry.COLUMN_SERIES + " INTEGER NOT NULL, " +
                PracticeEntry.COLUMN_REPS + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + PracticeEntry.COLUMN_EX_KEY + ") REFERENCES " +
                ExerciseEntry.TABLE_NAME + " (" + ExerciseEntry._ID + ") " +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_EXERCISE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PRACTICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PracticeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
