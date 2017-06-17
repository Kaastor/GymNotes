package pl.edu.wat.gymnotes.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestUtilities {

    static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//    static String date = sdf.format(Calendar.getInstance().getTime());
    static String date = "15-06-2017";

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }


    static ContentValues createSimpleExerciseValues(String name, String description){

        ContentValues testValues = new ContentValues();
        testValues.put(ExerciseContract.ExerciseEntry.COLUMN_NAME, name);
        testValues.put(ExerciseContract.ExerciseEntry.COLUMN_DESCRIPTION, description);

        return testValues;
    }

    static long insertSimpleExerciseValues(Context context, String name, String description) {
        ExerciseDbHelper dbHelper = new ExerciseDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createSimpleExerciseValues(name, description);
        long exerciseRowId;
        exerciseRowId = db.insert(ExerciseContract.ExerciseEntry.TABLE_NAME, null, testValues);

        assertTrue("Error: Failure to insert Simple Exercise Values", exerciseRowId != -1);
        return exerciseRowId;
    }

    static ContentValues createSimplePracticeValues(long exerciseRowId){

        ContentValues testValues = new ContentValues();
        testValues.put(ExerciseContract.PracticeEntry.COLUMN_EX_KEY, exerciseRowId);
        testValues.put(ExerciseContract.PracticeEntry.COLUMN_DATE, date);
        testValues.put(ExerciseContract.PracticeEntry.COLUMN_SERIES, 10);
        testValues.put(ExerciseContract.PracticeEntry.COLUMN_REPS, 10);

        return testValues;
    }

}
