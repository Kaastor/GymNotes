package pl.edu.wat.gymnotes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.ArraySet;
import android.view.LayoutInflater;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.edu.wat.gymnotes.data.ExerciseContract;


public class AddTrainingDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int clickedItem;
    private AlertDialog.Builder builder;
    private String[] exercisesArray;
    private List<String> exercisesList = new ArrayList<>();

    private static final int ADD_TRAINING_LOADER = 0;
    private static final String[] EXERCISE_COLUMNS = {
            ExerciseContract.ExerciseEntry._ID,
            ExerciseContract.ExerciseEntry.COLUMN_NAME
    };
    public static final int COL_EXERCISE_ID = 0;
    public static final int COL_EXERCISE_NAME = 1;

    public AddTrainingDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        getLoaderManager().initLoader(ADD_TRAINING_LOADER, null, this);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Use the Builder class for convenient dialog construction
        builder.setView(inflater.inflate(R.layout.dialog_add_training, null));
        builder.setTitle("Dodaj ćwiczenie");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.setSingleChoiceItems(exercisesArray, clickedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
            }
        });
        return builder.create();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ADD_TRAINING_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri exercisesUri = ExerciseContract.ExerciseEntry.buildExercises();
        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                exercisesUri,
                EXERCISE_COLUMNS,
                null,
                null,
                null
        );
        Cursor data = cursorLoader.loadInBackground();
        if (data.moveToFirst()){
            do{
                exercisesList.add(data.getString(data.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME)));
            }while(data.moveToNext());
        }
        exercisesArray = exercisesList.toArray(new String[exercisesList.size()]);

        return  cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}

