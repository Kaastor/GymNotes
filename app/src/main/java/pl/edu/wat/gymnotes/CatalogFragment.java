package pl.edu.wat.gymnotes;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import pl.edu.wat.gymnotes.data.ExerciseContract;

public class CatalogFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private String mExercise;
    private static final int CATALOG_LOADER = 0;

    private static final String[] EXERCISE_COLUMNS = {
            ExerciseContract.ExerciseEntry.TABLE_NAME + ExerciseContract.ExerciseEntry._ID,
            ExerciseContract.ExerciseEntry.COLUMN_NAME,
            ExerciseContract.ExerciseEntry.COLUMN_DESCRIPTION
    };

    public static final int COL_EXERCISE_ID = 0;
    public static final int COL_EXERCISE_NAME = 1;
    public static final int COL_EXERCISE_DESC = 2;



    public CatalogFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CATALOG_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_grid_catalog, container, false);


        GridView gridview = (GridView) rootView.findViewById(R.id.exercises_grid_view);
        gridview.setAdapter(new ImageAdapter(getActivity().getBaseContext()));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = "Informacje o ćwiczeniu";

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, info);
                startActivity(intent);
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

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
