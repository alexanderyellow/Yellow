package com.yellow.adviceby.activities.login;

import android.app.Activity;
import android.content.Intent;

import java.util.Observable;

/**
 * Created by SheykinAV on 28.09.2015.
 */
public abstract class Connection extends Observable {

    public enum Source {
        FACEBOOK("FACEBOOK"),
        GOOGLE("GOOGLE");

        private String source;

        private Source(String source) {
            this.source = source;
        }
    }

    protected static State currentState;

    public static Connection getConnection(Source source, Activity activity) {
        switch (source) {
            case GOOGLE:
                return GoogleConnection.getInstance(activity);
            case FACEBOOK:
                return FacebookConnection.getInstance(activity);
            default:
                return null;
        }
    }

    protected void changeState(State state) {
        currentState = state;
        setChanged();
        notifyObservers(state);
    }

    public void signIn() {}
    public void signIn(Activity activity) {
        if(activity == null){
            signIn();
        }
    }
    public void signOut(){}
    public void onActivityResult(int requestCode, int resultCode, Intent data){}

}
