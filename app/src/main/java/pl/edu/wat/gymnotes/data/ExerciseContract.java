package pl.edu.wat.gymnotes.data;


import android.provider.BaseColumns;

public class ExerciseContract {

    public static final class ExerciseEntry implements BaseColumns{

        public static final String TABLE_NAME = "exercise";

        public static final String COLUMN_PICTURE_BEFORE = "pic_before";

        public static final String COLUMN_PICTURE_AFTER = "pic_after";

        public static final String COLUMN_DESCRIPTION = "description";
    }

    public static final class PracticeEntry implements BaseColumns{

        public static final String TABLE_NAME = "practice";

        public static final String COLUMN_EX_KEY = "exercise_id";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_SERIES = "series";

        public static final String COLUMN_REPS = "reps";
    }
}
