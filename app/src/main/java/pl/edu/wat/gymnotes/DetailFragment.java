package pl.edu.wat.gymnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class DetailFragment extends Fragment {


    public DetailFragment() {
       }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_view, container, false);
        Intent intent = getActivity().getIntent();
        if(intent != null ){
            String[] exerciseInfo = intent.getStringArrayExtra("exerciseDescription");
            Toast.makeText(getActivity(), exerciseInfo[0], Toast.LENGTH_SHORT).show();
            ((TextView) rootView.findViewById(R.id.detail_name_text)).setText(exerciseInfo[0]);
            ((TextView) rootView.findViewById(R.id.detail_desc_text)).setText(exerciseInfo[1]);
        }
        return rootView;
    }


}
