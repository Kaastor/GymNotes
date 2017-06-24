package pl.edu.wat.gymnotes.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.R;
import pl.edu.wat.gymnotes.activities.DetailActivity;
import pl.edu.wat.gymnotes.data.ExerciseContract;
import pl.edu.wat.gymnotes.network.ExerciseSyncAdapter;

public class CatalogFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private Logger logger = Logger.getLogger(CatalogFragment.class.toString());

    private String mExercise;
    private static final int CATALOG_LOADER = 0;

    private static final String[] EXERCISE_COLUMNS = {
            ExerciseContract.ExerciseEntry._ID,
            ExerciseContract.ExerciseEntry.COLUMN_NAME,
            ExerciseContract.ExerciseEntry.COLUMN_DESCRIPTION
    };

    public static final int COL_EXERCISE_ID = 0;
    public static final int COL_EXERCISE_NAME = 1;
    public static final int COL_EXERCISE_DESC = 2;

    private SimpleCursorAdapter mExerciseAdapter;

    public CatalogFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ExerciseSyncAdapter.initializeSyncAdapter(getContext());
        getLoaderManager().initLoader(CATALOG_LOADER, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ExerciseSyncAdapter.initializeSyncAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid_catalog, container, false);

        mExerciseAdapter = new SimpleCursorAdapter(getActivity(), R.layout.grid_item_exercise, null,
                new String[]{
                        ExerciseContract.ExerciseEntry.COLUMN_NAME},
                new int[]{
                        R.id.item_gird_exercise
                },
                0
        );

        GridView gridview = (GridView) rootView.findViewById(R.id.exercises_grid_view);

        gridview.setAdapter(mExerciseAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SimpleCursorAdapter adapter = (SimpleCursorAdapter) adapterView.getAdapter();
                Cursor cursor = adapter.getCursor();
                if(null != cursor && cursor.moveToPosition(position)){
                    String[] exerciseDescription = new String[]{
                            cursor.getString(COL_EXERCISE_ID),
                            cursor.getString(COL_EXERCISE_NAME),
                            cursor.getString(COL_EXERCISE_DESC)
                    };
                    logger.log(Level.INFO, "exerciseDescription: " + exerciseDescription);

                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra("exerciseDescription", exerciseDescription);
                    startActivity(intent);
                    logger.log(Level.INFO, "Send intent to DetailActivity");
                }
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri exercisesUri = ExerciseContract.ExerciseEntry.buildExercises();
        return new CursorLoader(
                getActivity(),
                exercisesUri,
                EXERCISE_COLUMNS,
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
    public void onLoaderReset(Loader<Cursor> loader) {
        mExerciseAdapter.swapCursor(null);
    }
}
