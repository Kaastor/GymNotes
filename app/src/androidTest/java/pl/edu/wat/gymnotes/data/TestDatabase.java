package pl.edu.wat.gymnotes.data;


import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class TestDatabase extends AndroidTestCase{

    public void testCreateDatabase() throws Throwable{
        mContext.deleteDatabase(ExerciseDbHelper.DATABASE_NAME);
        SQLiteDatabase database = new ExerciseDbHelper(mContext).getWritableDatabase();

        assertEquals(true, database.isOpen());
        database.close();
    }

}
