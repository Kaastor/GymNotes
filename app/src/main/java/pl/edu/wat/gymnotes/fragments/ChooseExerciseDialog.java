package pl.edu.wat.gymnotes.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.data.ExerciseContract;
import pl.edu.wat.gymnotes.data.ExerciseDbHelper;

public class ChooseExerciseDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Logger logger = Logger.getLogger(ChooseExerciseDialog.class.toString());

    private AlertDialog.Builder builder;
    private String[] exercisesArray;
    private List<String> exercisesList = new ArrayList<>();

    private static final int EXERCISE_CHOOSER_LOADER = 0;
    private static final int REQUEST_CODE = 1;

    private static final String[] EXERCISE_COLUMNS = {
            ExerciseContract.ExerciseEntry._ID,
            ExerciseContract.ExerciseEntry.COLUMN_NAME
    };
    private int newEntryExerciseId;

    public ChooseExerciseDialog() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ExerciseDbHelper exerciseDbHelper = new ExerciseDbHelper(getContext());
        logger.log(Level.INFO, "onCreateDialog");
        builder = new AlertDialog.Builder(getActivity());
        getLoaderManager().initLoader(EXERCISE_CHOOSER_LOADER, null, this);
        builder.setTitle("Dodaj ćwiczenie");
        builder
                .setSingleChoiceItems(exercisesArray, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ExerciseDbHelper exerciseDbHelper = new ExerciseDbHelper(getContext());

                        newEntryExerciseId = exerciseDbHelper.getExerciseId(exercisesArray[which]);

                        logger.log(Level.INFO, this.getClass().toString() + " Chosen exercise: " + newEntryExerciseId);
                    }
                });
        newEntryExerciseId = exerciseDbHelper.getExerciseId(exercisesArray[0]);
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Intent intent = new Intent();
        intent.putExtra("newEntryExerciseId", Integer.toString(newEntryExerciseId));
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
        exercisesList.clear();
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
        logger.log(Level.INFO, "Loader exercisesList" + exercisesList);
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
