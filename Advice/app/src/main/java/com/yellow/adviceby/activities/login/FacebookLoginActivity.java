package com.yellow.adviceby.activities.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.ShareDialog;
import com.yellow.adviceby.R;
import com.yellow.adviceby.activities.LoginActivity;
import com.yellow.adviceby.activities.MainActivity;

import java.util.Arrays;

/**
 * Created by SheykinAV on 01.09.2015.
 */
public class FacebookLoginActivity extends FragmentActivity {

  //  private PendingAction pendingAction = PendingAction.NONE;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
/*
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(FacebookLoginActivity.this)
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }; */
/*
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("f+_connection_info", "onSuccess:");
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onCancel() {
            Log.d("f+_connection_info", "onCancel:");
            Intent intent = new Intent(FacebookLoginActivity.this, LoginActivity.class);
            startActivity(intent);

            if (pendingAction != PendingAction.NONE) {
                showAlert();
                pendingAction = PendingAction.NONE;
            }

        }

        @Override
        public void onError(FacebookException exception) {
            Log.d("f+_connection_info", "onError:");
            if (pendingAction != PendingAction.NONE
                    && exception instanceof FacebookAuthorizationException) {
                showAlert();
                pendingAction = PendingAction.NONE;
            }
        }

        private void showAlert() {
            new AlertDialog.Builder(FacebookLoginActivity.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
     //   LoginManager.getInstance().registerCallback(callbackManager, mCallback);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("f+_connection_info", "onSuccess:");
                        AccessToken accessToken = loginResult.getAccessToken();
                        Profile profile = Profile.getCurrentProfile();
                        Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("f+_connection_info", "onCancel:");
                        Intent intent = new Intent(FacebookLoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("f+_connection_info", "onError:");
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(FacebookLoginActivity.this)
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
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

/*
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                //updateUI();
                // It's possible that we were waiting for Profile to be populated in order to
                // post a status update.
                handlePendingAction();
            }
        };
*/

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

    }
/*
    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
        }
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("f+_connection_info", "onActivityResult:");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

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

}
