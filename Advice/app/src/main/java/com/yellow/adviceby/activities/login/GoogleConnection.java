package com.yellow.adviceby.activities.login;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.yellow.adviceby.db.DBUserHandler;

import java.lang.ref.WeakReference;
import java.util.Observable;

public class GoogleConnection extends Observable
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleLoginActivity";
    public static final String SOURCE = "g";

    private PendingIntent mSignInIntent;
    private WeakReference<Activity> activityWeakReference;
    private static State currentState;
    private static GoogleConnection sGoogleConnection;
    private GoogleApiClient.Builder mGoogleApiClientBuilder;
    private GoogleApiClient mGoogleApiClient;

    public static final int RC_SIGN_IN = 0;

    private DBUserHandler dbUserHandler;

    private GoogleConnection(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);

        mGoogleApiClientBuilder =
                new GoogleApiClient.Builder(activityWeakReference.get().getApplicationContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(Plus.API, Plus.PlusOptions.builder().build())
                        .addScope(new Scope(Scopes.PLUS_LOGIN));

        mGoogleApiClient = mGoogleApiClientBuilder.build();
        currentState = State.CLOSED;
    }

    private void changeState(State state) {
        Log.i("GoogleConnection", "changeState.StateA = " + state.toString());
        currentState = state;
        setChanged();
        notifyObservers(state);
    }

    public void connect() {
        currentState.connect(this);
    }

    public void disconnect() {
        currentState.disconnect(this);
    }

    public void revokeAccessAndDisconnect() {
        currentState.revokeAccessAndDisconnect(this);
    }

    public static GoogleConnection getInstance(Activity activity) {
        if (null == sGoogleConnection) {
            sGoogleConnection = new GoogleConnection(activity);
        }

        return sGoogleConnection;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "onConnectionSuspended");
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        changeState(State.SIGN_IN);
        connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");

        // Indicate that the sign in process is complete.
        changeState(State.SIGNED_IN);

    /*    User user = new User(1, 1, "g+");
        dbUserHandler.delete();
        dbUserHandler.create(user);  */

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());

        if (!currentState.equals(State.IN_PROGRESS)) {
            Log.i(TAG, "onConnectionFailed: ConnectionResult.if()");
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = result.getResolution();
            //    mSignInError = result.getErrorCode();

            if (currentState.equals(State.SIGN_IN)) {
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
                changeState(State.IN_PROGRESS);
                activityWeakReference.get().startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                changeState(State.SIGN_IN);
                connect();
            }
        } else {
            // Google Play services wasn't able to provide an intent for some
            // error types, so we show the default Google Play services error
            // dialog which may still start an intent on our behalf if the
            // user can resolve the issue.
            changeState(State.CLOSED);
            Toast.makeText(activityWeakReference.get().getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");

        if (resultCode == activityWeakReference.get().RESULT_OK) {
            Log.i(TAG, "onActivityResult.RESULT_OK");
            // If the error resolution was successful we should continue
            // processing errors.
            changeState(State.SIGN_IN);
        } else {
            Log.i(TAG, "onActivityResult.RESULT_NOT_OK");
            // If the error resolution was not successful or the user canceled,
            // we should stop processing errors.
            changeState(State.SIGNED_IN);
        }

        onSignIn();

    }

    protected void onSignIn() {
        Log.i(TAG, "onSignIn = " + mGoogleApiClient.isConnected());
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.i("GoogleConnection", "onSignIn.if");
            changeState(State.SIGN_IN);
            mGoogleApiClient.connect();
        }
    }

    protected void onSignOut() {
        Log.i(TAG, "onSignOut");
        if (mGoogleApiClient.isConnected()) {
            Log.i(TAG, "onSignOut.if");
            // We clear the default account on sign out so that Google Play
            // services will not return an onConnected callback without user
            // interaction.

            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            changeState(State.CLOSED);
        }
    }

    protected void onRevokeAccessAndDisconnect() {
        Log.i(TAG, "onRevokeAccessAndDisconnect");
        // After we revoke permissions for the user with a GoogleApiClient
        // instance, we must discard it and create a new one.
        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);

        // Our sample has caches no user data from Google+, however we
        // would normally register a callback on revokeAccessAndDisconnect
        // to delete user data so that we comply with Google developer
        // policies.
        Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
        mGoogleApiClient.disconnect();
        sGoogleConnection = null;
        changeState(State.CLOSED);
    }

}
