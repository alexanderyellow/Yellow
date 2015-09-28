package com.yellow.adviceby.activities.login;

/**
 * Created by SheykinAV on 24.09.2015.
 */
public enum State {

    SUCCESS {

    },

    CANCEL {

    },

    ERROR {

    },

    SIGN_IN {
        @Override
        void disconnect(GoogleConnection googleConnection) {
            googleConnection.signOut();
        }

        @Override
        void revokeAccessAndDisconnect(GoogleConnection googleConnection) {
            googleConnection.onRevokeAccessAndDisconnect();
        }
    },
    IN_PROGRESS {
        @Override
        void connect(GoogleConnection googleConnection) {
            googleConnection.signIn();
        }

        @Override
        void disconnect(GoogleConnection googleConnection) {
            googleConnection.signOut();
        }
    },
    SIGNED_IN {
        @Override
        void connect(GoogleConnection googleConnection) {
            googleConnection.signIn();
        }

        @Override
        void revokeAccessAndDisconnect(GoogleConnection googleConnection) {
            googleConnection.onRevokeAccessAndDisconnect();
        }
    },

    CLOSED {
        @Override
        void connect(GoogleConnection googleConnection) {
            googleConnection.signIn();
        }
    };

    void connect(GoogleConnection googleConnection) {
    }

    void disconnect(GoogleConnection googleConnection) {
    }

    void revokeAccessAndDisconnect(GoogleConnection googleConnection) {
    }

}
