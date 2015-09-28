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

    private Connection connection;
    private ProgressDialog progress;

    @Override
    public void update(Observable observable, Object data) {

        if (observable != connection) {
            Log.i("LoginActivity", "return = " + observable);
            return;
        }

        Intent intent = new Intent(LoginActivity.this, AdviceActivity.class);

        switch ((State) data) {
            case SUCCESS:
                Log.i("LoginActivity", "SUCCESS");
                intent.putExtra(SOURCE, Connection.Source.FACEBOOK.toString());
                startActivity(intent);
                finish();
                break;
            case SIGN_IN:
                progress.show();
                break;
            case SIGNED_IN:
                Log.i("LoginActivity", "SIGNED_IN");
                progress.dismiss();
                intent.putExtra(SOURCE, Connection.Source.GOOGLE.toString());
                startActivity(intent);
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
                Log.i("LoginActivity", "plus_sign_in_button");
                connection = Connection.getConnection(Connection.Source.GOOGLE, this);
                connection.addObserver(this);
                connection.signIn();
                break;
            case R.id.facebook_sign_in_button:
                connection = Connection.getConnection(Connection.Source.FACEBOOK, this);
                connection.addObserver(this);
                connection.signIn(this);
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
            //    facebookConnection.signOut();
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
        connection.deleteObserver(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("LoginActivity", "onActivityResult = " + resultCode + "/" + requestCode);
        if (GoogleConnection.RC_SIGN_IN == requestCode && resultCode != RESULT_CANCELED) {
            Log.i("LoginActivity", "onActivityResult.if");
            connection.onActivityResult(resultCode, resultCode, data);
        } else if(resultCode != RESULT_CANCELED) {
            connection.onActivityResult(requestCode, resultCode, data);
        }
    }

}