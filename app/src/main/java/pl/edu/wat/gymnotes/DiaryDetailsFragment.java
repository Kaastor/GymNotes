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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import pl.edu.wat.gymnotes.data.ExerciseContract;


public class DiaryDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView diaryDetailsList;
    private SimpleCursorAdapter mDiaryDetailsAdapter;
    private static final int DIARY_DETAILS_LOADER = 0;


    public DiaryDetailsFragment() {
    }

    String date;

    private static final String[] PRACTICE_COLUMNS = {
            "p." + ExerciseContract.PracticeEntry._ID,
            "p." + ExerciseContract.PracticeEntry.COLUMN_DATE,
            "e." + ExerciseContract.ExerciseEntry.COLUMN_NAME,
            "p." + ExerciseContract.PracticeEntry.COLUMN_SERIES,
            "p." + ExerciseContract.PracticeEntry.COLUMN_REPS,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_diary_details, container, false);

        mDiaryDetailsAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_dairy_details, null,
                new String[]{
                        ExerciseContract.ExerciseEntry.COLUMN_NAME,
                        ExerciseContract.PracticeEntry.COLUMN_SERIES,
                        ExerciseContract.PracticeEntry.COLUMN_REPS
                },
                new int[]{
                        R.id.list_item_dairy_exercise,
                        R.id.list_item_dairy_series,
                        R.id.list_item_dairy_reps
                },
                0
        );
        diaryDetailsList = rootView.findViewById(R.id.list_view_diary_details);
        diaryDetailsList.setAdapter(mDiaryDetailsAdapter);

        diaryDetailsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), "Mogłeś się bardziej postarać!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DIARY_DETAILS_LOADER, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if(intent != null ){
            date = intent.getStringExtra("data");
            Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
        }

        Uri practicesUri = ExerciseContract.PracticeEntry.buildPracticeForDate(date);
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
        mDiaryDetailsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mDiaryDetailsAdapter.swapCursor(null);
    }
}
