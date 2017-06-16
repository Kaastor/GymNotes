package pl.edu.wat.gymnotes;

import android.os.Bundle;
import android.widget.Toast;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "ASD2", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }
}
