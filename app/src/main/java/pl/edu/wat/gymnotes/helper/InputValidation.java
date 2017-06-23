package pl.edu.wat.gymnotes.helper;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import pl.edu.wat.gymnotes.R;
import pl.edu.wat.gymnotes.data.ExerciseDbHelper;

public class InputValidation {

    private String email;
    private String password;
    private ExerciseDbHelper dbHelper;
    private Context context;

    public InputValidation(Context context) {
        this.context = context;
        this.dbHelper = new ExerciseDbHelper(context);
    }

    public View loginFieldsValidation(AutoCompleteTextView mEmailView, EditText mPasswordView){
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) ) {
            mPasswordView.setError(context.getResources().getString(R.string.error_field_required));
            focusView = mPasswordView;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(context.getResources().getString(R.string.error_invalid_password));
            focusView = mPasswordView;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(context.getResources().getString(R.string.error_field_required));
            focusView = mEmailView;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(context.getResources().getString(R.string.error_invalid_email));
            focusView = mEmailView;
        }

        return focusView;
    }

    public View userLoginValidation(AutoCompleteTextView mEmailView, EditText mPasswordView){
        View focusView = null;
        mEmailView.setError(null);
        mPasswordView.setError(null);

        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        if (dbHelper.checkUser(email, password)) {
            mEmailView.setError(context.getResources().getString(R.string.error_wrong_login_or_pass));
            focusView = mEmailView;
        }
        return focusView;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
