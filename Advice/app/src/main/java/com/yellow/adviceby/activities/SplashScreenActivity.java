package com.yellow.adviceby.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yellow.adviceby.R;
import com.yellow.adviceby.activities.login.LoginActivity;
import com.yellow.adviceby.db.DBUserHandler;
import com.yellow.adviceby.model.User;

public class SplashScreenActivity extends AppCompatActivity {

    private DBUserHandler dbUserHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        dbUserHandler = new DBUserHandler(this);
        new ConnectionChecking().execute(dbUserHandler);

    }

    private class ConnectionChecking extends AsyncTask<DBUserHandler, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(DBUserHandler... dbUserHandlers) {
            User user = dbUserHandlers[0].read();
            return (user != null ? user.getIsConnected() : false);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(Boolean result) {

            Intent intent;

            if(result) {
                intent = new Intent(SplashScreenActivity.this, AdviceActivity.class);
                startActivity(intent);
            } else {
                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            finish();
        }
    }

}
