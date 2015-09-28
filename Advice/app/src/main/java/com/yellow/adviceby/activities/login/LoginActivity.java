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
import com.yellow.adviceby.db.DBUserHandler;
import com.yellow.adviceby.model.User;

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements Observer, OnClickListener {

    private static final String SOURCE = "source";

    private Button signInGoogleButton;
    private Button signInFacebookButton;
    private LinearLayout signInButton;
    private LinearLayout getStartedButton;

    private GoogleConnection googleConnection;
    private ProgressDialog progress;

    private FacebookConnection facebookConnection;

    @Override
    public void update(Observable observable, Object data) {

        if (observable != googleConnection && observable != facebookConnection) {
            Log.i("LoginActivity", "return = " + observable);
            return;
        }
        switch ((State) data) {
            case SUCCESS:
                Log.i("LoginActivity", "SUCCESS");
                Intent intent1 = new Intent(LoginActivity.this, AdviceActivity.class);
                intent1.putExtra(SOURCE, FacebookConnection.SOURCE);
                startActivity(intent1);
                finish();
                break;
            case SIGN_IN:
                progress.show();
                break;
            case SIGNED_IN:
                Log.i("LoginActivity", "SIGNED_IN");
                progress.dismiss();
                Intent intent2 = new Intent(LoginActivity.this, AdviceActivity.class);
                intent2.putExtra(SOURCE, GoogleConnection.SOURCE);
                startActivity(intent2);
                finish();

           /*     try {
                    String emailAddress = googleConnection.getAccountName();
                    Log.i("Lalalal", emailAddress);

                } catch (Exception ex) {
                    String exception = ex.getLocalizedMessage();
                    String exceptionString = ex.toString();
                }
                finish();  */
                break;
            case CLOSED:
                progress.dismiss();
                break;
            case IN_PROGRESS:
                progress.dismiss();
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



        facebookConnection = FacebookConnection.getInstance(this);
        facebookConnection.addObserver(this);

        googleConnection = GoogleConnection.getInstance(this);
        googleConnection.addObserver(this);

        initProgressDialog();
    }

    private void initProgressDialog() {
        progress = new ProgressDialog(this);
        progress.setTitle("Signing in");
        progress.setMessage("Waiting...");
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
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
                googleConnection.connect();
                break;
            case R.id.facebook_sign_in_button:
                facebookConnection.signIn(this);
            //    startActivityForResult(new Intent(LoginActivity.this, FacebookLoginActivity.class), 2);
            //    googleConnection.revokeAccessAndDisconnect();
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
                facebookConnection.signOut();
                /**
                 * Choose account
                 */
                //    Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                //            false, null, null, null, null);
                //startActivityForResult(intent, 3);
                //    startActivity(intent);

            //    Intent intent2 = new Intent(LoginActivity.this, CreateAccountActivity.class);
            //    startActivity(intent2);
                break;
        }
    }

    private void dbUpdate(User user) {
        DBUserHandler dbUserHandler = new DBUserHandler(this);
        if (dbUserHandler.hasRecord()) {
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
        facebookConnection.deleteObserver(this);
        //    googleConnection.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("LoginActivity", "onActivityResult = " + resultCode + "/" + requestCode);
        if (GoogleConnection.RC_SIGN_IN == requestCode && resultCode != RESULT_CANCELED) {
            Log.i("LoginActivity", "onActivityResult.if");
            googleConnection.onActivityResult(resultCode, resultCode, data);
        } else if(resultCode != RESULT_CANCELED) {
            facebookConnection.onActivityResult(requestCode, resultCode, data);
        }


    }

}