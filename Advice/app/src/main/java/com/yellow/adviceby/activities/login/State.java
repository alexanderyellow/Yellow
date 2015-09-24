package com.yellow.adviceby.activities.login;

/**
 * Created by SheykinAV on 23.09.2015.
 */
public enum State {
    CREATED {
        @Override
        void connect(GoogleConnection googleConnection) {
            googleConnection.onSignUp();
        }

        @Override
        void disconnect(GoogleConnection googleConnection) {
            googleConnection.onSignOut();
        }
    },
    OPENING {
    },
    OPENED {
            @Override
            void disconnect(GoogleConnection googleConnection) {
                googleConnection.onSignOut();
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
