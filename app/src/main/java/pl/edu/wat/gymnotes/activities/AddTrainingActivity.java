package pl.edu.wat.gymnotes.activities;


import android.os.Bundle;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.R;

public class AddTrainingActivity extends BaseActivity{

    private Logger logger = Logger.getLogger(AddTrainingActivity.class.toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.log(Level.INFO, this.getLocalClassName() + " Created.");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_training;
    }
}
