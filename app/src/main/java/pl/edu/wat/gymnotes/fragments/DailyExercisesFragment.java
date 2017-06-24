package pl.edu.wat.gymnotes.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.R;
import pl.edu.wat.gymnotes.activities.AddTrainingActivity;
import pl.edu.wat.gymnotes.activities.LoginActivity;
import pl.edu.wat.gymnotes.data.ExerciseContract;

/**
 * A placeholder fragment containing a simple view.
 */

public class DailyExercisesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Logger logger = Logger.getLogger(DailyExercisesFragment.class.toString());

    String todayDate;

    private static final String[] PRACTICE_COLUMNS = {
            "p." + ExerciseContract.PracticeEntry._ID,
            "p." + ExerciseContract.PracticeEntry.COLUMN_DATE,
            "e." + ExerciseContract.ExerciseEntry.COLUMN_NAME,
            "p." + ExerciseContract.PracticeEntry.COLUMN_SERIES,
            "p." + ExerciseContract.PracticeEntry.COLUMN_REPS,
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
        logger.log(Level.INFO, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_list_main, container, false);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTrainingActivity.class);
                startActivity(intent);
                logger.log(Level.INFO, "Send intent to AddTrainingActivity");
            }
        });

        mExerciseAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_exercises, null,
                new String[]{
                        ExerciseContract.ExerciseEntry.COLUMN_NAME,
                        ExerciseContract.PracticeEntry.COLUMN_SERIES,
                        ExerciseContract.PracticeEntry.COLUMN_REPS
                },
                new int[]{
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
                Toast.makeText(getActivity(), "Dawaj dalej!", Toast.LENGTH_SHORT).show();
            }
        });

        todayExercisesList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                DailyExercisesFragment.super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
                if (view.getId()==R.id.list_view_exercises) {
                    MenuInflater inflater = getActivity().getMenuInflater();
                    inflater.inflate(R.menu.menu_exercise_entry, contextMenu);
                }
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
    public void onResume() {
        super.onResume();
        logger.log(Level.INFO, "resumed");
        getLoaderManager().restartLoader(DAILY_EXERCISE_LOADER, null, this);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        logger.log(Level.INFO, " onCreateLoader");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        todayDate = sdf.format(Calendar.getInstance().getTime());

        Uri practicesUri = ExerciseContract.PracticeEntry.buildPracticeForDateAndUser(LoginActivity.activeUserEmail, todayDate);
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
        logger.log(Level.INFO, " onLoadFinished");
        mExerciseAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mExerciseAdapter.swapCursor(null);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:
                getContext().getContentResolver().delete(
                        ExerciseContract.PracticeEntry.buildPracticeForId(Long.toString(info.id)),
                        null,
                        null
                );
                logger.log(Level.INFO, "deleted Practice id:" + info.id);
                this.onResume();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
