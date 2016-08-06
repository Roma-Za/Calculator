package com.example.roman.calculator;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;

        import android.content.Intent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.roman.calculator.models.User;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.GraphRequest;
        import com.facebook.GraphResponse;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_REC = 0;
    private EditText _loginText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private TextView _forgottenLink;
    private CallbackManager callbackManager;
    private Activity _activity;
    private User currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        _activity = this;
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {

                                        currentUser = new User();
                                        try {
                                            currentUser.setEmail(object.getString("email"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            currentUser.setFb_id(object.getString("id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            currentUser.setFirst_name(object.getString("first_name"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            currentUser.setLast_name(object.getString("last_name"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(LoginActivity.this,"Hello, "+currentUser.getFirst_name()+ " "+ currentUser.getLast_name(), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                                        startActivityForResult(intent, REQUEST_SIGNUP);
                                        _activity.finish();

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        setContentView(R.layout.activity_login);

        _loginText = (EditText)findViewById(R.id.input_login);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _signupLink = (TextView)findViewById(R.id.link_signup);
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        _forgottenLink = (TextView)findViewById(R.id.link_forgotten);
        _forgottenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecoveryActivity.class);
                startActivityForResult(intent, REQUEST_REC);
            }
        });


}


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String login = _loginText.getText().toString();
        String password = _passwordText.getText().toString();

        if (login.equals("admin")&&password.equals("admin")) {
            currentUser = new User();
            currentUser.setFirst_name("admin");
            currentUser.setLast_name("admin");
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {

                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Toast.makeText(LoginActivity.this, "Hello, " + currentUser.getFirst_name() + " " + currentUser.getLast_name(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), TabActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String login = _loginText.getText().toString();
        String password = _passwordText.getText().toString();

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

        return valid;
    }


    public void onGLoginClick(View view) {
    }

    public void onTwLoginClick(View view) {
    }

    public void onFbLoginClick(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }
}