package pl.edu.wat.gymnotes.activities;

import android.os.Bundle;

import pl.edu.wat.gymnotes.R;
import pl.edu.wat.gymnotes.activities.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }
}
