package com.yellow.adviceby.activities.login;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.yellow.adviceby.R;
import com.yellow.adviceby.activities.AdviceActivity;
import com.yellow.adviceby.activities.MainActivity;

public class LoginActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    CallbackManager callbackManager;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private Button signInGoogleButton;
    private Button signInFacebookButton;
    private LinearLayout signInButton;
    private LinearLayout getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();

        signInGoogleButton = (Button) findViewById(R.id.plus_sign_in_button);
        signInFacebookButton = (Button) findViewById(R.id.facebook_sign_in_button);
        signInButton = (LinearLayout) findViewById(R.id.sign_in_btn);
        getStartedButton = (LinearLayout) findViewById(R.id.get_started_btn);

        signInGoogleButton.setOnClickListener(this);
        signInFacebookButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        getStartedButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            //      Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            //      Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Button on click listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_sign_in_button:
                onSignInClicked();
                break;
            case R.id.facebook_sign_in_button:
                startActivityForResult(new Intent(LoginActivity.this, FacebookLoginActivity.class), 1);
                break;
            case R.id.sign_in_btn:
                Intent intent1 = new Intent(LoginActivity.this, AdviceActivity.class);
                startActivity(intent1);
                break;
            case R.id.get_started_btn:
                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("g+_connection_info", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d("g+_connection_info", "onConnected:" + bundle);
        mShouldResolve = false;

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
/*
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            Log.d("g+_connection_info", currentPerson.getDisplayName());
            Log.d("g+_connection_info", currentPerson.getImage().getUrl());
            Log.d("g+_connection_info", currentPerson.getUrl());
        }
        */
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.
        Log.d("g+_connection_error", "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            Toast.makeText(getApplicationContext(), GooglePlayServicesUtil.getErrorString(connectionResult.getErrorCode()),
                    Toast.LENGTH_LONG).show();
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e("g+_connection_error", "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                Log.d("g+_connection_error", "onConnectionFailed:" + connectionResult);
                Toast.makeText(getApplicationContext(), GooglePlayServicesUtil.getErrorString(connectionResult.getErrorCode()),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("g+_connection_info", "onConnectionSuspended:");
        mGoogleApiClient.connect();
    }

    /**
     * Check if the device supports Google Play Services.  It's best
     * practice to check first rather than handling this as an error case.
     *
     * @return whether the device supports Google Play Services
     */
    private boolean supportsGooglePlayServices() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS;
    }

}