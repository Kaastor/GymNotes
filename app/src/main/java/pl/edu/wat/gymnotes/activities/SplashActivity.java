package pl.edu.wat.gymnotes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import pl.edu.wat.gymnotes.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        LoginActivity.activeUserEmail = getApplicationContext().getSharedPreferences("userLogin",
                MODE_PRIVATE).getString("userEmail", "");

        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences(
                "userLogin", Context.MODE_PRIVATE);

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        if (preferences.getString("userEmail", "").equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_splash);
    }
}
