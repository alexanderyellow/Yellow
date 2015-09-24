package com.yellow.adviceby.activities.login;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.lang.ref.WeakReference;
import java.util.Observable;

/**
 * Created by SheykinAV on 23.09.2015.
 * http://stackoverflow.com/questions/22368520/how-to-correctly-use-google-plus-sign-in-with-multiple-activities
 * https://github.com/jpventura/google-play-services/blob/wip/Identity_Lessons_Final/UdacityPlus2_1/app/src/main/java/com/lmoroney/udacityplus2_1/common/GoogleConnection.java
 */
public class GoogleConnection extends Observable
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_CODE = 0;

    private static GoogleConnection sGoogleConnection;

    private WeakReference<Activity> activityWeakReference;
    private GoogleApiClient.Builder googleApiClientBuilder;
    private GoogleApiClient googleApiClient;
    private ConnectionResult connectionResult;
    private static State currentState;

    private GoogleConnection(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);

        googleApiClientBuilder =
                new GoogleApiClient.Builder(activityWeakReference.get().getApplicationContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(Plus.API, Plus.PlusOptions.builder().build())
                        .addScope(new Scope(Scopes.PLUS_LOGIN));

        googleApiClient = googleApiClientBuilder.build();
        currentState = State.CLOSED;
    }

    private void changeState(State state) {
        Log.i("GoogleConnection", "getInstance.State = " + state.toString());
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
    public void onConnected(Bundle hint) {
        Log.i("GoogleConnection", "onConnected");
        changeState(State.OPENED);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        changeState(State.CLOSED);
        connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("GoogleConnection", "onConnectionFailed");
        if (currentState.equals(State.CLOSED) && connectionResult.hasResolution()) {
            Log.i("GoogleConnection", "onConnectionFailed.if");
            changeState(State.CREATED);
            this.connectionResult = connectionResult;
            try {
                activityWeakReference.get().startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        REQUEST_CODE, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {

            }
        } else {
            Log.i("GoogleConnection", "onConnectionFailed.else");
            connect();
            Toast.makeText(activityWeakReference.get().getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
        //    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 1);
        //    connect();
        }
    }

    public void onActivityResult(int result) {
        Log.i("GoogleConnection", "onActivityResult");
        if (result == Activity.RESULT_OK) {
            Log.i("GoogleConnection", "onActivityResult.if");
            // If the error resolution was successful we should continue
            // processing errors.
            changeState(State.CREATED);
        } else {
            Log.i("GoogleConnection", "onActivityResult.else");
            // If the error resolution was not successful or the user canceled,
            // we should stop processing errors.
            changeState(State.CLOSED);
        }

        // If Google Play services resolved the issue with a dialog then
        // onStart is not called so we need to re-attempt connection here.
        onSignIn();
    }

    public String getAccountName() {
        return Plus.AccountApi.getAccountName(googleApiClient);
    }

    protected void onSignIn() {
        Log.i("GoogleConnection", "onSignIn");
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()) {
            Log.i("GoogleConnection", "onSignIn.if");
            googleApiClient.connect();
        }
    }

    protected void onSignOut() {
        Log.i("GoogleConnection", "onSignOut");
        if (googleApiClient.isConnected()) {
            Log.i("GoogleConnection", "onSignOut.if");
            // We clear the default account on sign out so that Google Play
            // services will not return an onConnected callback without user
            // interaction.

            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();
            changeState(State.CLOSED);
        //    googleApiClient.connect();
        }
    }

    protected void onSignUp() {
        // We have an intent which will allow our user to sign in or
        // resolve an error.  For example if the user needs to
        // select an account to sign in with, or if they need to consent
        // to the permissions your app is requesting.

        try {
            // Send the pending intent that we stored on the most recent
            // OnConnectionFailed callback.  This will allow the user to
            // resolve the error currently preventing our connection to
            // Google Play services.
            changeState(State.OPENING);
            connectionResult.startResolutionForResult(activityWeakReference.get(), REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            // The intent was canceled before it was sent.  Attempt to connect to
            // get an updated ConnectionResult.
            changeState(State.CREATED);
            googleApiClient.connect();
        }
    }

    protected void onRevokeAccessAndDisconnect() {
        // After we revoke permissions for the user with a GoogleApiClient
        // instance, we must discard it and create a new one.
        Plus.AccountApi.clearDefaultAccount(googleApiClient);

        // Our sample has caches no user data from Google+, however we
        // would normally register a callback on revokeAccessAndDisconnect
        // to delete user data so that we comply with Google developer
        // policies.
        Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
    //    googleApiClient = googleApiClientBuilder.build();
    //    googleApiClient.connect();
        changeState(State.CLOSED);
    }

}
