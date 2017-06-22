package pl.edu.wat.gymnotes;


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

import pl.edu.wat.gymnotes.data.ExerciseContract;

public class ChooseExerciseDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private AlertDialog.Builder builder;
    private String[] exercisesArray;
    private List<String> exercisesList = new ArrayList<>();

    private static final int EXERCISE_CHOOSER_LOADER = 0;
    private static final int REQUEST_CODE = 1;


    public static final int COL_EXERCISE_ID = 0;
    public static final int COL_EXERCISE_NAME = 1;

    private static final String[] EXERCISE_COLUMNS = {
            ExerciseContract.ExerciseEntry._ID,
            ExerciseContract.ExerciseEntry.COLUMN_NAME
    };
    private int newEntryExerciseId;

    public ChooseExerciseDialog() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialog_add_training, null);
        builder = new AlertDialog.Builder(getActivity());

        getLoaderManager().initLoader(EXERCISE_CHOOSER_LOADER, null, this);

//        builder.setView(view);
        builder.setTitle("Dodaj ćwiczenie");
        builder
                .setSingleChoiceItems(exercisesArray, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newEntryExerciseId = which+1; //id in database from 0
                        Intent intent = new Intent();
                        intent.putExtra("newEntryExerciseId", Integer.toString(newEntryExerciseId));
                        getTargetFragment().onActivityResult(
                                getTargetRequestCode(), REQUEST_CODE, intent);
                    }
                });


        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(EXERCISE_CHOOSER_LOADER, null, this);
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
