package com.example.roman.calculator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
    private static final String TAG = "RegistrationActivity";
    private EditText _fNameText;
    private EditText _lNameText;
    private EditText _loginText;
    private EditText _passwordText;
    private EditText _emailText;
    private EditText _phoneText;
    private Button _regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        _fNameText = (EditText) findViewById(R.id.input_first_name);
        _lNameText = (EditText) findViewById(R.id.input_last_name);
        _loginText = (EditText) findViewById(R.id.input_login_reg);
        _passwordText = (EditText) findViewById(R.id.input_password_reg);
        _emailText = (EditText) findViewById(R.id.input_email);
        _phoneText = (EditText) findViewById(R.id.input_phone);
        _regButton = (Button) findViewById(R.id.btn_send_reg);
        _regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });
    }

    private void registration() {
        Log.d(TAG, "Registration");

        if (!validate()) {
            onRegFailed();
            return;
        }

        _regButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        onRegSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onRegSuccess() {
        _regButton.setEnabled(true);
        Toast.makeText(this, "Hello, " + _fNameText.getText() + " " + _lNameText.getText(), Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    public void onRegFailed() {
        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();

        _regButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String login = _loginText.getText().toString();
        String password = _passwordText.getText().toString();
        String fName = _fNameText.getText().toString();
        String lName = _lNameText.getText().toString();

        String email = _emailText.getText().toString();
        String phone = _phoneText.getText().toString();
        if (login.isEmpty()) {
            _loginText.setError("enter a valid login");
            valid = false;
        } else {
            _loginText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (fName.isEmpty()) {
            _fNameText.setError("enter a valid First Name");
            valid = false;
        } else {
            _fNameText.setError(null);
        }

        if (lName.isEmpty()) {
            _lNameText.setError("enter a valid Last Name");
            valid = false;
        } else {
            _lNameText.setError(null);
        }

        if (phone.isEmpty()) {
            _phoneText.setError("enter a valid phone");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        return valid;
    }
}
