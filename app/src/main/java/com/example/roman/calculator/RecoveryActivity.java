package com.example.roman.calculator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RecoveryActivity extends Activity {
    private EditText _emailText;
    private Button _recButton;
    private static final String TAG = "RecoveryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        _emailText = (EditText) findViewById(R.id.input_email_rec);
        _recButton = (Button) findViewById(R.id.btn_send_recovery);
        _recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recovery();
            }
        });
    }

    private void recovery() {
        Log.d(TAG, "Recovery");

        if (!validate()) {
            onRecFailed();
            return;
        }

        _recButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RecoveryActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onRecSuccess();
                        // onRecFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onRecSuccess() {
        _recButton.setEnabled(true);
        finish();
    }

    public void onRecFailed() {
        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();

        _recButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        return valid;
    }
}
