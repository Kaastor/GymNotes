package pl.edu.wat.gymnotes.helper;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import pl.edu.wat.gymnotes.R;

public class InputValidation {

    private Context context;
    private String email;
    private String password;

    public InputValidation(Context context) {
        this.context = context;
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
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
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

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
