package com.github.emrecankoc.firewrapper;

public class FirestoreException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 5902212603704524945L;

    public FirestoreException(String message, Throwable reason) {
        super(message, reason);
    }

    public FirestoreException(String message) {
        super(message);
    }

    public FirestoreException(Throwable reason) {
        super(reason);
    }
}