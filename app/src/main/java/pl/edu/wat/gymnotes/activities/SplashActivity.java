package pl.edu.wat.gymnotes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.R;
import pl.edu.wat.gymnotes.network.ExerciseSyncAdapter;

public class SplashActivity extends AppCompatActivity {

    private Logger logger = Logger.getLogger(SplashActivity.class.toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        logger.log(Level.INFO, "onCreate");

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
            logger.log(Level.INFO, "Send intent to LoginActivity");
            finish();
            logger.log(Level.INFO, "finished");
        }
        else{
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
            logger.log(Level.INFO, "Send intent to NavigationActivity");
            finish();
            logger.log(Level.INFO, "finished");
        }
        setContentView(R.layout.activity_splash);
        logger.log(Level.INFO, "Init ExerciseSyncAdapter");
        ExerciseSyncAdapter.initializeSyncAdapter(this);
    }
}
