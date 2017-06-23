package pl.edu.wat.gymnotes.fragments;

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

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.R;


public class DetailFragment extends Fragment {

    private Logger logger = Logger.getLogger(DetailFragment.class.toString());

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_view, container, false);
        Intent intent = getActivity().getIntent();
        if(intent != null ){
            String[] exerciseInfo = intent.getStringArrayExtra("exerciseDescription");

            logger.log(Level.INFO, "create view for exercise: " + exerciseInfo[0]);

            String imageBefore = "before_" + exerciseInfo[0] ;
            String imageAfter =  "after_" + exerciseInfo[0] ;
            Toast.makeText(getActivity(), exerciseInfo[0], Toast.LENGTH_SHORT).show();

            ImageView viewBefore =  ((ImageView) rootView.findViewById(R.id.detail_exercise_before));
            viewBefore.setImageResource(getResources().getIdentifier(imageBefore, "drawable", getContext().getPackageName()));
            viewBefore.setImageResource(getResources().getIdentifier(imageBefore, "drawable", getContext().getPackageName()));

            ImageView viewAfter =  ((ImageView) rootView.findViewById(R.id.detail_exercise_after));
            viewAfter.setImageResource(getResources().getIdentifier(imageAfter, "drawable", getContext().getPackageName()));
            viewAfter.setImageResource(getResources().getIdentifier(imageAfter, "drawable", getContext().getPackageName()));

            ((TextView) rootView.findViewById(R.id.detail_name_text)).setText(exerciseInfo[1]);
            ((TextView) rootView.findViewById(R.id.detail_desc_text)).setText(exerciseInfo[2]);
        }
        return rootView;
    }


}
