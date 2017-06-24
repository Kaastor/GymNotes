package pl.edu.wat.gymnotes.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.R;
import pl.edu.wat.gymnotes.data.ExerciseDbHelper;
import pl.edu.wat.gymnotes.network.ExerciseSyncAdapter;
import pl.edu.wat.gymnotes.util.InputValidation;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    private Logger logger = Logger.getLogger(LoginActivity.class.toString());

    public static String activeUserEmail;
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private InputValidation validation;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private LinearLayout mainLayout;
    private Button mEmailSignInButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        logger.log(Level.INFO, "onCreate");
        logger.log(Level.INFO, "Init ExerciseSyncAdapter");
        ExerciseSyncAdapter.initializeSyncAdapter(this);

        setContentView(R.layout.activity_login);
        initLayouts();

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                logger.log(Level.INFO, "Send intent to RegisterActivity");
            }
        });

    }

    private void initLayouts(){
        mainLayout = (LinearLayout)findViewById(R.id.email_login_form);
        validation = new InputValidation(getApplicationContext());
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mProgressView = findViewById(R.id.login_progress);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        registerButton = (Button) findViewById(R.id.email_register_button);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        View focusView = validation.loginFieldsValidation(mEmailView, mPasswordView);

        if (focusView != null) {
            logger.log(Level.WARNING, "Fields was filled wrong");
            // There was an error
            focusView.requestFocus();
        } else {
            focusView = validation.userLoginValidation(mEmailView, mPasswordView);
            if(focusView == null) {
                logger.log(Level.INFO, "User logged in successfully");
                onLogInActions();
//                mAuthTask = new UserLoginTask(validation.getEmail(), validation.getPassword());
//                mAuthTask.execute((Void) null);
            }
            else{
                focusView.requestFocus();
            }
        }
    }

    private void onLogInActions(){
        showProgress(true);

        getApplicationContext().getSharedPreferences("userLogin",
                MODE_PRIVATE).edit().putString("userEmail", mEmailView.getText().toString()).apply();

        activeUserEmail = mEmailView.getText().toString();
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        this.finish();
        logger.log(Level.INFO, "finished");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        hideKeyboard();
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void hideKeyboard(){
        logger.log(Level.INFO, "Keyboard hidden");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return false;
            }

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    //poprawne
//                    return pieces[1].equals(mPassword);
//                }
//            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

