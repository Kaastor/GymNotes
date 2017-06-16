package pl.edu.wat.gymnotes.data;

import android.content.ContentValues;
import org.junit.Assert;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;



public class ExerciseDbHelperTest extends AndroidTestCase {

    public static final String LOG_TAG = ExerciseDbHelperTest.class.getSimpleName();

    @Test
    public void testExerciseTable()  {
        ExerciseDbHelper dbHelper = new ExerciseDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createSimpleExerciseValues();

        long exerciseRowId;
        exerciseRowId = database.insert(ExerciseContract.ExerciseEntry.TABLE_NAME, null, testValues);

        Assert.assertTrue(exerciseRowId != -1);

        Cursor cursor = database.query(false,
                ExerciseContract.ExerciseEntry.TABLE_NAME,
                null, null, null, null, null, null, null);

        Assert.assertTrue("Error:: no records returned from exercise query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Exercise query val failed", cursor, testValues);

        Assert.assertFalse("Error: Moire than one record returned from exercise query", cursor.moveToNext());

        cursor.close();
        database.close();
    }
}
