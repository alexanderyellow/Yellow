package com.yellow.adviceby.activities.login;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

/**
 * Created by SheykinAV on 22.09.2015.
 */
public class GoogleApplication extends Application
{
    //instantiate object public static
    public static GoogleApiClient mGoogleApiClient;
    public Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                        //        .setAccountName("alexandryellow@gmail.com")
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}