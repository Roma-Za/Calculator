package com.example.roman.calculator;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

public class GoogleAPIClientHelper {
    public static final GoogleApiClient getClient(final Activity activity){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestScopes(new Scope(Scopes.PLUS_ME))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(activity)
                .enableAutoManage((FragmentActivity) activity /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        new AlertDialog.Builder(activity)
                                .setTitle("Connection Failed")
                                .setMessage("Unable to sign in with Google account")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }
}
