package com.example.roman.calculator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Z63VC4AC9gWPXLXqTNXnwQIq7";
    private static final String TWITTER_SECRET = "JH8lAxJeUU156ZJhk0sJwM3zwMSMYKZxMhC5L9ezoNEyUfPeYL";


    private EditText _loginText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private TextView _forgottenLink;
    private CallbackManager callbackManager;
    private User currentUser;
    private SharedPreferences sPref;
    private TwitterAuthClient mTwitterAuthClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        mTwitterAuthClient = new TwitterAuthClient();
        callbackManager = CallbackManager.Factory.create();
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
                                        Toast.makeText(LoginActivity.this, "Hello, " + currentUser.getFirst_name() + " " + currentUser.getLast_name(), Toast.LENGTH_LONG).show();
                                        saveLoginType(Consts.FACEBOOK);
                                        startMainActivity();

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

        _loginText = (EditText) findViewById(R.id.input_login);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _signupLink = (TextView) findViewById(R.id.link_signup);
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, Consts.REQUEST_SIGNUP);
            }
        });
        _forgottenLink = (TextView) findViewById(R.id.link_forgotten);
        _forgottenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecoveryActivity.class);
                startActivity(intent);
            }
        });



    }

    public void login() {
        Log.d(Consts.TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String login = _loginText.getText().toString();
        final String password = _passwordText.getText().toString();


        currentUser = new User();
        currentUser.setFirst_name("admin");
        currentUser.setLast_name("admin");
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (login.equals("admin") && password.equals("admin")) {
                            saveLoginType(Consts.STANDART);
                            onLoginSuccess();
                        }else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Consts.REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                saveLoginType(Consts.STANDART);
                startMainActivity();
            }
        }

        if (requestCode == Consts.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(Consts.TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            saveLoginType(Consts.GOOGLE);
            currentUser = new User();
            currentUser.setEmail(acct.getEmail());
            currentUser.setFirst_name(acct.getDisplayName());
            Toast.makeText(LoginActivity.this, "Hello, " + currentUser.getFirst_name(), Toast.LENGTH_LONG).show();
            startMainActivity();
        } else {

        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), TabActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Toast.makeText(LoginActivity.this, "Hello, " + currentUser.getFirst_name() + " " + currentUser.getLast_name(), Toast.LENGTH_LONG).show();
        startMainActivity();
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

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(GoogleAPIClientHelper.getClient(this));
        startActivityForResult(signInIntent, Consts.RC_SIGN_IN);
    }

    public void onTwLoginClick(View view) {
        mTwitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                saveLoginType(Consts.TWITTER);
                Toast.makeText(LoginActivity.this, "Hello, " + twitterSessionResult.data.getUserName(), Toast.LENGTH_LONG).show();
                startMainActivity();
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }

    public void onFbLoginClick(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void saveLoginType(String type) {
        sPref = getApplicationContext().getSharedPreferences(Consts.PREFERENCE_FILE, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Consts.LOGIN_TYPE, type);
        ed.commit();
    }

    private String loadLoginType() {
        sPref = getApplicationContext().getSharedPreferences(Consts.PREFERENCE_FILE, MODE_PRIVATE);
        String savedText = sPref.getString(Consts.LOGIN_TYPE, "");
        return savedText;
    }
}