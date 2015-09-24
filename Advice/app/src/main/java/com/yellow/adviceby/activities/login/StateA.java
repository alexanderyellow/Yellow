package com.yellow.adviceby.activities.login;

/**
 * Created by SheykinAV on 24.09.2015.
 */
public enum StateA {

    OPENING {
    },
    SIGN_IN {
        @Override
        void disconnect(GoogleLoginActivity googleConnection) {
            googleConnection.onSignOut();
        }

        @Override
        void revokeAccessAndDisconnect(GoogleLoginActivity googleConnection) {
            googleConnection.onRevokeAccessAndDisconnect();
        }
    },
    IN_PROGRESS {
        @Override
        void connect(GoogleLoginActivity googleConnection) {
            googleConnection.onSignIn();
        }

        @Override
        void disconnect(GoogleLoginActivity googleConnection) {
            googleConnection.onSignOut();
        }
    },
    SIGNED_IN {
        @Override
        void connect(GoogleLoginActivity googleConnection) {
            googleConnection.onSignIn();
        }

        @Override
        void revokeAccessAndDisconnect(GoogleLoginActivity googleConnection) {
            googleConnection.onRevokeAccessAndDisconnect();
        }
    },

    CLOSED {
        @Override
        void connect(GoogleLoginActivity googleConnection) {
            googleConnection.onSignIn();
        }
    };

    void connect(GoogleLoginActivity googleConnection) {
    }

    void disconnect(GoogleLoginActivity googleConnection) {
    }

    void revokeAccessAndDisconnect(GoogleLoginActivity googleConnection) {
    }

}
