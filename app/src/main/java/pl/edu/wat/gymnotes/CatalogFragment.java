package pl.edu.wat.gymnotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class CatalogFragment extends Fragment {


    public CatalogFragment() {
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
                Toast.makeText(getActivity(), "asd",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
