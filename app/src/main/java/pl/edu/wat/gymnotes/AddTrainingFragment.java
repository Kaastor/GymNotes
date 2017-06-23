package pl.edu.wat.gymnotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.edu.wat.gymnotes.activities.LoginActivity;
import pl.edu.wat.gymnotes.data.ExerciseContract;
import pl.edu.wat.gymnotes.data.ExerciseDbHelper;


public class AddTrainingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private String todayDate = sdf.format(Calendar.getInstance().getTime());

    private View rootView;
    private DialogFragment chooseExercise;
    private static final int ADD_TRAINING_LOADER = 0;
    private static final int REQUEST_CODE = 1;

    private EditText series;
    private EditText reps;
    private int newEntryExerciseId = 0;
    private int newEntrySeries;
    private int newEntryReps;
    private String newEntryDate = todayDate;


    public AddTrainingFragment() {
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_training_view, container, false);
        series = rootView.findViewById(R.id.dialog_add_series);
        reps = rootView.findViewById(R.id.dialog_add_reps);

        chooseExercise = new ChooseExerciseDialog();
        chooseExercise.setTargetFragment(this, REQUEST_CODE);
        Button getExercise = rootView.findViewById(R.id.dialog_choose_exercise);
        getExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseExercise.show(getFragmentManager(), "choose");
            }
        });

        Button save = rootView.findViewById(R.id.dialog_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewEntryTrainingToDB();
            }
        });
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            newEntryExerciseId = Integer.parseInt(data.getStringExtra("newEntryExerciseId"));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ADD_TRAINING_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
          return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void saveNewEntryTrainingToDB(){
        if(!(series.getText().toString().equals("") || reps.getText().toString().equals(""))) {
            newEntrySeries = Integer.parseInt(series.getText().toString());
            newEntryReps = Integer.parseInt(reps.getText().toString());
            if (newEntryReps == 0 || newEntrySeries == 0) {
                Snackbar.make(rootView, "Wpisz poprawne wartości", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else {
                newEntrySeries = Integer.parseInt(series.getText().toString());
                newEntryReps = Integer.parseInt(reps.getText().toString());
                if(newEntryExerciseId == 0)
                    newEntryExerciseId = 1;

                saveToDatabase();

                getActivity().finish();
            }
        }
        else
            Snackbar.make(rootView, "Pola nie mogą być puste!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
    }

    private void saveToDatabase(){
        ExerciseDbHelper dbHelper = new ExerciseDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues practiceValues = new ContentValues();
        practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_DATE, newEntryDate);
        practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_USER_KEY, dbHelper.getUserId(LoginActivity.activeUserEmail));
        practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_EX_KEY, newEntryExerciseId);
        practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_SERIES, newEntrySeries);
        practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_REPS, newEntryReps);

        db.insert(ExerciseContract.PracticeEntry.TABLE_NAME, null, practiceValues);
    }
}

