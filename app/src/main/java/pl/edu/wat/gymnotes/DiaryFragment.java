package pl.edu.wat.gymnotes;


import android.database.MatrixCursor;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.edu.wat.gymnotes.data.ExerciseContract;

public class DiaryFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final int DIARY_LOADER = 0;

    private SimpleCursorAdapter mDiaryAdapter;
    private ListView diaryList;


    public DiaryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_diary, container, false);

        mDiaryAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_date, null,
                new String[]{
                        ExerciseContract.PracticeEntry.COLUMN_DATE
                },
                new int[]{
                        R.id.list_item_diary_date
                },
                0
        );
        diaryList = rootView.findViewById(R.id.list_view_dates);
        diaryList.setAdapter(mDiaryAdapter);

        diaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), "Tutaj szczegoly!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DIARY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri datesUri = ExerciseContract.PracticeEntry.buildDistinctPracticesDates();
        return new CursorLoader(
                getActivity(),
                datesUri,
                new String[]{
                        ExerciseContract.PracticeEntry._ID,
                        ExerciseContract.PracticeEntry.COLUMN_DATE},
                null,
                null,
                ExerciseContract.PracticeEntry.COLUMN_DATE + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        MatrixCursor newCursor = new MatrixCursor(new String[]{
                ExerciseContract.PracticeEntry._ID,
                ExerciseContract.PracticeEntry.COLUMN_DATE}); // Same projection used in loader
        if (data.moveToFirst()) {
            String date = "00-00-0000";
            do {
                if (!data.getString(1).equals(date)) {
                    newCursor.addRow(new String[]{"0", data.getString(1)}); // match the original cursor fields
                    date =data.getString(1);
                }
            } while (data.moveToNext());
        }
        mDiaryAdapter.swapCursor(newCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDiaryAdapter.swapCursor(null);
    }

}
