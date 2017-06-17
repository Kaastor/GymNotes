package pl.edu.wat.gymnotes.data;

import pl.edu.wat.gymnotes.data.ExerciseContract.PracticeEntry;
import pl.edu.wat.gymnotes.data.ExerciseContract.ExerciseEntry;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.junit.Test;

import java.util.ArrayList;

public class ExerciseProviderTest extends AndroidTestCase {

    static ArrayList<Long> exercisesRowIds ;

    public ExerciseProviderTest() {
    }

    public static final String LOG_TAG = ExerciseProviderTest.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                PracticeEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                ExerciseEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                PracticeEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Practice table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                ExerciseEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Exercise table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        deleteAllRecords();
    }

    @Test
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                ExerciseProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + ExerciseContract.CONTENT_AUTHORITY,
                    providerInfo.authority, ExerciseContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        // content://pl.edu.wat.gymnotes.app/practice/
        String type = mContext.getContentResolver().getType(PracticeEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the PracticeEntry CONTENT_URI should return PracticeEntry.CONTENT_TYPE",
                PracticeEntry.CONTENT_TYPE, type);

        String testDate = "1982-11-08";
        // content://pl.edu.wat.gymnotes.app/practice/"1982-11-08"
        type = mContext.getContentResolver().getType(
                PracticeEntry.buildPracticeForDate(testDate));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the PracticeEntry CONTENT_URI with location should return PracticeEntry.CONTENT_TYPE",
                PracticeEntry.CONTENT_TYPE, type);

        // content://pl.edu.wat.gymnotes.app/exercise/
        type = mContext.getContentResolver().getType(ExerciseEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/location
        assertEquals("Error: the ExerciseEntry CONTENT_URI should return ExerciseEntry.CONTENT_TYPE",
                ExerciseEntry.CONTENT_TYPE, type);
    }



    public void testBasicPracticeQuery() {
        // insert our test records into the database
        ExerciseDbHelper dbHelper = new ExerciseDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues practiceValues = TestUtilities.createSimplePracticeValues(exercisesRowIds.get(0));

        long practiceRowId = db.insert(PracticeEntry.TABLE_NAME, null, practiceValues);
        assertTrue("Unable to Insert PracticeEntry into the Database", practiceRowId != -1);
        db.close();

        // Test the basic content provider query
        Cursor practiceCursor = mContext.getContentResolver().query(
                PracticeEntry.buildPracticeForDate(TestUtilities.date),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicPracticeQuery", practiceCursor, practiceValues);
    }


    public void testBasicExerciseQuery() {
        // insert our test records into the database
        ExerciseDbHelper dbHelper = new ExerciseDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        exercisesRowIds = new ArrayList<>();
        exercisesRowIds.add(TestUtilities.insertSimpleExerciseValues(mContext, "Pompka", "To jest opis pompki."));
        exercisesRowIds.add(TestUtilities.insertSimpleExerciseValues(mContext, "Przysiad", "To jest opis przysiadu."));
        exercisesRowIds.add(TestUtilities.insertSimpleExerciseValues(mContext, "Brzuszki", "To jest opis brzuszków."));
        // Fantastic.  Now that we have a location, add some weather!
        System.out.println(exercisesRowIds );
        // Test the basic content provider query
        Cursor exerciseCursor = mContext.getContentResolver().query(
                ExerciseEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        assertEquals(exerciseCursor.getCount(), 3);

    }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 15;
    static ContentValues[] createBulkInsertPracticeValues() {
        String currentTestDate = TestUtilities.date;

        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++ ) {
            ContentValues practiceValues = new ContentValues();
            if(i<5)
                practiceValues.put(PracticeEntry.COLUMN_EX_KEY, 3);
            if(i>=5 && i< 10)
                practiceValues.put(PracticeEntry.COLUMN_EX_KEY, 2);
            if(i>=10)
                practiceValues.put(PracticeEntry.COLUMN_EX_KEY, 1);
            practiceValues.put(PracticeEntry.COLUMN_DATE, currentTestDate);
            practiceValues.put(PracticeEntry.COLUMN_SERIES, 10);
            practiceValues.put(PracticeEntry.COLUMN_REPS, i);

            returnContentValues[i] = practiceValues;

        }
        return returnContentValues;
    }

    public void testBulkInsert() {

        ContentValues[] bulkInsertContentValues = createBulkInsertPracticeValues();

        int insertCount = mContext.getContentResolver().bulkInsert(PracticeEntry.CONTENT_URI, bulkInsertContentValues);
        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                PracticeEntry.buildPracticeForDate(TestUtilities.date),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                PracticeEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
        );

        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("date"));
                System.out.println("Gowno " + data);
            }while(cursor.moveToNext());
        }


        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), cursor.getCount());

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

    public void testPracticeQueryForDate() {

        Cursor cursor;
        cursor = mContext.getContentResolver().query(
                PracticeEntry.buildPracticeForDate(TestUtilities.date),
                new String[]{ExerciseEntry.COLUMN_NAME, PracticeEntry.COLUMN_DATE}, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                PracticeEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
        );

        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("date"));
                String nameEx = cursor.getString(cursor.getColumnIndex("name"));
                System.out.println(cursor.getCount() + " Gowno " + data + " " + nameEx);
            }while(cursor.moveToNext());
        }
    }

    public void testPracticeQueryForDates() {
        Cursor cursor;
        cursor = mContext.getContentResolver().query(
                PracticeEntry.buildDistinctPracticesDates(),
                new String[]{
                        PracticeEntry.COLUMN_DATE}, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                PracticeEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
        );

        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("date"));
                System.out.println(cursor.getCount() + " Gowno " + data);
            }while(cursor.moveToNext());
        }


    }

}
