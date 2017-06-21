package pl.edu.wat.gymnotes;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
            String imageBefore = "before_" + exerciseInfo[0] ;
            String imageAfter =  "after_" + exerciseInfo[0] ;
            Toast.makeText(getActivity(), exerciseInfo[0], Toast.LENGTH_SHORT).show();

            ImageView viewBefore =  ((ImageView) rootView.findViewById(R.id.detail_exercise_before));
            viewBefore.setImageResource(getResources().getIdentifier(imageBefore, "drawable", getContext().getPackageName()));
            viewBefore.setImageResource(getResources().getIdentifier(imageBefore, "drawable", getContext().getPackageName()));

            ImageView viewAfter =  ((ImageView) rootView.findViewById(R.id.detail_exercise_after));
            viewAfter.setImageResource(getResources().getIdentifier(imageAfter, "drawable", getContext().getPackageName()));
            viewAfter.setImageResource(getResources().getIdentifier(imageAfter, "drawable", getContext().getPackageName()));

            ((TextView) rootView.findViewById(R.id.detail_name_text)).setText(exerciseInfo[0]);
            ((TextView) rootView.findViewById(R.id.detail_desc_text)).setText(exerciseInfo[1]);
        }
        return rootView;
    }


}
