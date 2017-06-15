package pl.edu.wat.gymnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */

public class DailyExercisesFragment extends Fragment {

    private ListView todayExercisesList;
    private ArrayAdapter<String> todayExercisesAdapter;
    
    public DailyExercisesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_main, container, false);

        String[] dataArray = {
                "Pompki - 15 powtórzeń",
                "Brzuszki - 15 powtórzeń",
                "Podciąganie - 15 powtórzeń",
                "Pompki szerokie - 15 powtórzeń",
                "Pompki wąskie - 15 powtórzeń",
                "Podciąganie nachwytem- 15 powtórzeń",
                "Podciąganie - podchwytem 15 powtórzeń",
                "Przysiady - 15 powtórzeń",
                "Przysiady - 15 powtórzeń",
                "Sranie - 15 powtórzeń",
                "Plank - 15 powtórzeń",
                "Samoloty - 15 powtórzeń"
        };
        ArrayList<String> dailyExercises = new ArrayList<>(Arrays.asList(dataArray));


        todayExercisesAdapter = new ArrayAdapter<>(getActivity().getBaseContext(),
                R.layout.list_item_exercises, R.id.list_item_exercises_textview, dailyExercises);

        todayExercisesList = rootView.findViewById(R.id.list_view_exercises);
        todayExercisesList.setAdapter(todayExercisesAdapter);

        todayExercisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
}
