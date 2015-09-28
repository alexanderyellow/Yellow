package com.yellow.adviceby.activities.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.Observable;

/**
 * Created by SheykinAV on 01.09.2015.
 */
public class FacebookConnection extends Observable{

    private static final String TAG = "FacebookConnection";
    public static final String SOURCE = "fb";

    private CallbackManager callbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private static State currentState;

    private static FacebookConnection facebookLogin;

    public FacebookConnection(final Activity activity) {
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i(TAG, "onSuccess:");
                        Toast.makeText(activity.getApplicationContext(), "onSuccess",
                                Toast.LENGTH_LONG).show();
                        changeState(State.SUCCESS);

                    }

                    @Override
                    public void onCancel() {
                        Log.i(TAG, "onCancel:");
                        Toast.makeText(activity.getApplicationContext(), "onCancel",
                                Toast.LENGTH_LONG).show();
                        changeState(State.CANCEL);
                    /*    Intent intent = new Intent(FacebookLoginActivity.this, LoginActivity.class);
                        startActivity(intent); */
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.i(TAG, "onError:");
                   /*     Intent intent = new Intent(FacebookLoginActivity.this, LoginActivity.class);
                        startActivity(intent); */
                        Toast.makeText(activity.getApplicationContext(), "onError",
                                Toast.LENGTH_LONG).show();
                        changeState(State.ERROR);
                    }

                });

        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

    }

    private void changeState(State state) {
        Log.i("GoogleConnection", "changeState.State = " + state.toString());
        currentState = state;
        setChanged();
        notifyObservers(state);
    }

    public static FacebookConnection getInstance(Activity activity) {
        if (null == facebookLogin) {
            Log.i(TAG, "getInstance.null");
            facebookLogin = new FacebookConnection(activity);
        }

        return facebookLogin;
    }

    public void signIn(Activity activity) {
        Log.i(TAG, "signIn");
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
    }

    public void signOut() {
        LoginManager.getInstance().logOut();
    }

/*    @Override
    protected void onResume() {
        super.onResume();
        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);
    }
*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
/*
    @Override
    public void onPause() {
        super.onPause();
        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }
*/
}
