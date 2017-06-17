package pl.edu.wat.gymnotes.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


public class ExerciseContractTest extends AndroidTestCase {

    private static final String TEST_EXERCISE_NAME = "Pompki";
    private static final String TEST_PRACTICE_DATE = "1982-11-08";

    @Test
    public void testBuildPracticeDate() {
        Uri practiceUri = ExerciseContract.PracticeEntry.buildPracticeForDate(TEST_PRACTICE_DATE);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildPracticeForDate in " +
                        "ExerciseContract.",
                practiceUri);
        assertEquals("Error: Practice date not properly appended to the end of the Uri",
                TEST_PRACTICE_DATE, practiceUri.getQueryParameter(ExerciseContract.PracticeEntry.COLUMN_DATE));
        assertEquals("Error: Practice date Uri doesn't match our expected result",
                practiceUri.toString(),
                "content://pl.edu.wat.gymnotes.app/practice?date=1982-11-08");

    }

    @Test
    public void testBuildExerciseName() {
        Uri exerciseUri = ExerciseContract.ExerciseEntry.buildExerciseForName(TEST_EXERCISE_NAME);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildExerciseForName in " +
                        "ExerciseContract.",
                exerciseUri);
        System.out.println("gowno" + exerciseUri.toString());
        assertEquals("Error: Exercise name not properly appended to the end of the Uri",
                TEST_EXERCISE_NAME, exerciseUri.getLastPathSegment());
        assertEquals("Error: Exercise name Uri doesn't match our expected result",
                exerciseUri.toString(),
                "content://pl.edu.wat.gymnotes.app/exercise/Pompki");

    }
}
