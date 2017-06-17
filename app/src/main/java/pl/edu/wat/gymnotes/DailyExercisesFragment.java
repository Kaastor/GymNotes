package pl.edu.wat.gymnotes;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.edu.wat.gymnotes.data.ExerciseContract;

/**
 * A placeholder fragment containing a simple view.
 */

public class DailyExercisesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    String todayDate;

    private static final String[] PRACTICE_COLUMNS = {
            ExerciseContract.PracticeEntry._ID,
            ExerciseContract.PracticeEntry.COLUMN_DATE,
            ExerciseContract.PracticeEntry.COLUMN_EX_KEY,
            ExerciseContract.PracticeEntry.COLUMN_SERIES,
            ExerciseContract.PracticeEntry.COLUMN_REPS,
    };

    public static final int COL_PRACTICE_ID = 0;
    public static final int COL_PRACTICE_EX_KEY = 1;
    public static final int COL_PRACTICE_SERIES = 2;
    public static final int COL_PRACTICE_REPS = 3;
    public static final int COL_PRACTICE_DATE = 4;

    private ListView todayExercisesList;
    private SimpleCursorAdapter mExerciseAdapter;
    private static final int DAILY_EXERCISE_LOADER = 0;

    public DailyExercisesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_main, container, false);

        mExerciseAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_exercises, null,
                new String[]{
                        ExerciseContract.PracticeEntry.COLUMN_DATE,
                        ExerciseContract.PracticeEntry.COLUMN_EX_KEY,
                        ExerciseContract.PracticeEntry.COLUMN_SERIES,
                        ExerciseContract.PracticeEntry.COLUMN_REPS
                },
                new int[]{
                        R.id.list_item_exercises_date,
                        R.id.list_item_exercises_exercise,
                        R.id.list_item_exercises_series,
                        R.id.list_item_exercises_reps
                },
                0
        );
        todayExercisesList = rootView.findViewById(R.id.list_view_exercises);
        todayExercisesList.setAdapter(mExerciseAdapter);

        todayExercisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), "Coś", Toast.LENGTH_SHORT).show();
//                SimpleCursorAdapter adapter = (SimpleCursorAdapter) adapterView.getAdapter();
//                Cursor cursor = adapter.getCursor();
//                if(null != cursor && cursor.moveToPosition(position)){
//                    String[] practiceDescription = new String[]{
//                            cursor.getString(COL_PRACTICE_DATE),
//                            cursor.getString(COL_PRACTICE_EX_KEY),
//                            cursor.getString(COL_PRACTICE_SERIES),
//                            cursor.getString(COL_PRACTICE_REPS)
//                    };
//                    Intent intent = new Intent(getActivity(), DetailActivity.class)
//                            .putExtra("practiceDescription", practiceDescription);
//                    startActivity(intent);
//                }
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DAILY_EXERCISE_LOADER, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        todayDate = sdf.format(Calendar.getInstance().getTime());
        String todayDate = "11-08-1982";


        Uri practicesUri = ExerciseContract.PracticeEntry.buildPracticeForDate(todayDate);
        return new CursorLoader(
                getActivity(),
                practicesUri,
                PRACTICE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mExerciseAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mExerciseAdapter.swapCursor(null);
    }
}
