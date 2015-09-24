package com.yellow.adviceby.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

    private class ConnectionChecking extends AsyncTask<DBUserHandler, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(DBUserHandler... dbUserHandlers) {
        //    dbUserHandlers[0].delete();
            User user = dbUserHandlers[0].read();
//            Log.i("doInBackground", String.valueOf(user.getIsConnected()));
            return (user != null ? user.getIsConnected() : 0);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i("onPostExecute", String.valueOf(result));

            Intent intent;

            if(result.equals(1)) {
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
