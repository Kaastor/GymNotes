package pl.edu.wat.gymnotes.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.junit.Test;




public class ExerciseDbHelperTest extends AndroidTestCase {

    public static final String LOG_TAG = ExerciseDbHelperTest.class.getSimpleName();


    private void deleteTheDatabase() {
        mContext.deleteDatabase(ExerciseDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    long exerciseRowId;

    @Test
    public void testExerciseTable()  {
        ExerciseDbHelper dbHelper = new ExerciseDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createSimpleExerciseValues();

        exerciseRowId = database.insert(ExerciseContract.ExerciseEntry.TABLE_NAME, null, testValues);

        assertTrue(exerciseRowId != -1);

        Cursor cursor = database.query(false,
                ExerciseContract.ExerciseEntry.TABLE_NAME,
                null, null, null, null, null, null, null);

        assertTrue("Error:: no records returned from exercise query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Exercise query val failed", cursor, testValues);

        assertFalse("Error: Moire than one record returned from exercise query", cursor.moveToNext());

        cursor.close();
        database.close();
    }

    @Test
    public void testPracticeTable(){

        assertFalse("Error: Location Not Inserted Correctly", exerciseRowId == -1L);

        ExerciseDbHelper dbHelper = new ExerciseDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues practiceValues = TestUtilities.createSimplePracticeValues(exerciseRowId);

        long practiceRowId = database.insert(ExerciseContract.PracticeEntry.TABLE_NAME, null, practiceValues);
        assertTrue(practiceRowId != -1);

        Cursor practiceCursor = database.query(false,
                ExerciseContract.PracticeEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null, null  // sort order
        );

        assertTrue( "Error: No Records returned from location query", practiceCursor.moveToFirst() );
        TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                practiceCursor, practiceValues);
        assertFalse( "Error: More than one record returned from weather query",
                practiceCursor.moveToNext() );

        practiceCursor.close();
        dbHelper.close();
    }
}
