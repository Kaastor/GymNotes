package pl.edu.wat.gymnotes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pl.edu.wat.gymnotes.R;
import pl.edu.wat.gymnotes.data.ExerciseDbHelper;
import pl.edu.wat.gymnotes.util.InputValidation;
import pl.edu.wat.gymnotes.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mAgainPasswordView;

    private InputValidation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        validation = new InputValidation(getApplicationContext());

        mNameView = (EditText) findViewById(R.id.name_registration);
        mEmailView = (EditText) findViewById(R.id.email_registration);
        mPasswordView = (EditText) findViewById(R.id.password_registration);
        mAgainPasswordView = (EditText) findViewById(R.id.password_again_registration);

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
    }

    private void attemptRegistration() {
        View focusView = validation.userRegisterValidation(mEmailView, mPasswordView, mAgainPasswordView);

        if (focusView != null) {
            focusView.requestFocus();
        } else {
            focusView = validation.userExistLoginValidation(mEmailView);
            if(focusView == null) {
                User user = new User(
                        mNameView.getText().toString(),
                        mEmailView.getText().toString(),
                        mPasswordView.getText().toString());
                new ExerciseDbHelper(getApplicationContext()).addUser(user);
                this.finish();
            }
            else{
                focusView.requestFocus();
            }
        }
    }
}
