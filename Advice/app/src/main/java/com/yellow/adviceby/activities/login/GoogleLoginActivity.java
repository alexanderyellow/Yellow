package com.yellow.adviceby.activities.login;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.yellow.adviceby.activities.AdviceActivity;
import com.yellow.adviceby.db.DBUserHandler;
import com.yellow.adviceby.model.User;

public class GoogleLoginActivity extends FragmentActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "signin";

    private static final int STATE_SIGNED_IN = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private int mSignInProgress;

    private PendingIntent mSignInIntent;
    private int mSignInError;

    private static final int RC_SIGN_IN = 0;

    private DBUserHandler dbUserHandler;
    //   private ProgressDialog pd;

    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                        //        .setAccountName("alexandryellow@gmail.com")
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();

                mSignInProgress = STATE_SIGN_IN;

        dbUserHandler = new DBUserHandler(this);
/*
        pd = new ProgressDialog(getApplicationContext());
        pd.setTitle("Processing...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);  */
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
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "onConnectionSuspended");
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");

        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_SIGNED_IN;
        Intent intent = new Intent(GoogleLoginActivity.this, AdviceActivity.class);
     /*   Bundle bundle = new Bundle();
        bundle.putSerializable("b", (Serializable) mGoogleApiClient);
        intent.putExtra("i", bundle); */
        startActivity(intent);

        User user = new User(1, true, "g+");
        dbUserHandler.create(user);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());

        if (mSignInProgress != STATE_IN_PROGRESS) {
            Log.i(TAG, "onConnectionFailed: ConnectionResult.if()");
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in button
                // so we should continue processing errors until the user is signed in
                // or they click cancel.
                Log.i(TAG, "onConnectionFailed: ConnectionResult.resolveSignInError() = "
                        + result.getErrorCode());
                resolveSignInError();
            }
        }
    }

    private void resolveSignInError() {
        Log.i(TAG, "onConnectionFailed: ConnectionResult.resolveSignInError()");
        if (mSignInIntent != null) {
            Log.i(TAG, "onConnectionFailed: ConnectionResult.resolveSignInError.if()");
            // We have an intent which will allow our user to sign in or
            // resolve an error.  For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback.  This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // Google Play services wasn't able to provide an intent for some
            // error types, so we show the default Google Play services error
            // dialog which may still start an intent on our behalf if the
            // user can resolve the issue.
            Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Log.i(TAG, "onActivityResult");
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResult.RESULT_OK");
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    Log.i(TAG, "onActivityResult.RESULT_NOT_OK");
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
                    mSignInProgress = STATE_SIGNED_IN;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    Log.i(TAG, "onActivityResult.isn'tConnected");
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }

                break;
        }
    }

}
