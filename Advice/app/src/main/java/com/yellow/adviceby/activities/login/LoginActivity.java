package com.yellow.adviceby.activities.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yellow.adviceby.R;
import com.yellow.adviceby.activities.AdviceActivity;
import com.yellow.adviceby.activities.CreateAccountActivity;
import com.yellow.adviceby.db.DBUserHandler;
import com.yellow.adviceby.model.User;

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements Observer, OnClickListener {

    private Button signInGoogleButton;
    private Button signInFacebookButton;
    private LinearLayout signInButton;
    private LinearLayout getStartedButton;

    private GoogleConnection googleConnection;
    private ProgressDialog progress;

    @Override
    public void update(Observable observable, Object data) {
        if (observable != googleConnection) {
            return;
        }
        switch ((State) data) {
            case CREATED:
        //        progress.dismiss();
                onSignedOutUI();
                break;
            case OPENING:
        //        progress.show();
                break;
            case OPENED:
        //        progress.dismiss();
                Intent intent = new Intent(LoginActivity.this, AdviceActivity.class);
                startActivity(intent);

                // We are signed in!
                // Retrieve some profile information to personalize our app for the user.
                try {
                    String emailAddress = googleConnection.getAccountName();
                    Log.i("Lalalal", emailAddress);

                } catch (Exception ex) {
                    String exception = ex.getLocalizedMessage();
                    String exceptionString = ex.toString();
                }
                finish();
                break;
            case CLOSED:
        //        progress.dismiss();
                onSignedOutUI();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInGoogleButton = (Button) findViewById(R.id.plus_sign_in_button);
        signInFacebookButton = (Button) findViewById(R.id.facebook_sign_in_button);
        signInButton = (LinearLayout) findViewById(R.id.sign_in_btn);
        getStartedButton = (LinearLayout) findViewById(R.id.get_started_btn);

        signInGoogleButton.setOnClickListener(this);
        signInFacebookButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        getStartedButton.setOnClickListener(this);

        googleConnection = GoogleConnection.getInstance(this);
        googleConnection.addObserver(this);

        progress = new ProgressDialog(this);
        progress.setTitle("Signing in");
        progress.setMessage("Waiting...");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Button on click listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_sign_in_button:
        //        startActivityForResult(new Intent(LoginActivity.this, GoogleLoginActivity.class), 1);
                googleConnection.connect();
                break;
            case R.id.facebook_sign_in_button:
        //        startActivityForResult(new Intent(LoginActivity.this, FacebookLoginActivity.class), 2);
        //        googleConnection.disconnect();
                break;
            case R.id.sign_in_btn:
                LoginDialog loginDialog = new LoginDialog(LoginActivity.this);
                loginDialog.show();
            //    DialogFragment newFragment = new LoginDialogFragment();
            //    newFragment.show(getSupportFragmentManager(), "signin");
            //    Intent intent1 = new Intent(LoginActivity.this, AdviceActivity.class);
            //    startActivity(intent1);
                break;
            case R.id.get_started_btn:

                /**
                 * Choose account
                 */
            //    Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
            //            false, null, null, null, null);
                //startActivityForResult(intent, 3);
            //    startActivity(intent);

                Intent intent2 = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void dbUpdate(User user) {
        DBUserHandler dbUserHandler = new DBUserHandler(this);
        if(dbUserHandler.hasRecord()) {
            dbUserHandler.update(user);
        } else {
            dbUserHandler.create(user);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("LoginActivity", "onDestroy");
        super.onDestroy();
        googleConnection.deleteObserver(this);
    //    googleConnection.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("LoginActivity", "onActivityResult");
        if (GoogleConnection.REQUEST_CODE == requestCode) {
            Log.i("LoginActivity", "onActivityResult.if");
            googleConnection.onActivityResult(resultCode);
        }
    }

    private void onSignedOutUI() {
        // Update the UI to reflect that the user is signed out.
    }

}