package com.yellow.adviceby.activities.login;

/**
 * Created by SheykinAV on 24.09.2015.
 */
public enum State {

    SIGN_IN {
        @Override
        void disconnect(GoogleConnection googleConnection) {
            googleConnection.onSignOut();
        }

        @Override
        void revokeAccessAndDisconnect(GoogleConnection googleConnection) {
            googleConnection.onRevokeAccessAndDisconnect();
        }
    },
    IN_PROGRESS {
        @Override
        void connect(GoogleConnection googleConnection) {
            googleConnection.onSignIn();
        }

        @Override
        void disconnect(GoogleConnection googleConnection) {
            googleConnection.onSignOut();
        }
    },
    SIGNED_IN {
        @Override
        void connect(GoogleConnection googleConnection) {
            googleConnection.onSignIn();
        }

        @Override
        void revokeAccessAndDisconnect(GoogleConnection googleConnection) {
            googleConnection.onRevokeAccessAndDisconnect();
        }
    },

    CLOSED {
        @Override
        void connect(GoogleConnection googleConnection) {
            googleConnection.onSignIn();
        }
    };

    void connect(GoogleConnection googleConnection) {
    }

    void disconnect(GoogleConnection googleConnection) {
    }

    void revokeAccessAndDisconnect(GoogleConnection googleConnection) {
    }

}
