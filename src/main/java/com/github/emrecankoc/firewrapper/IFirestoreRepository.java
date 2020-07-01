package com.github.emrecankoc.firewrapper;

import java.util.Collection;

public interface IFirestoreRepository<T> {
    
    public String save(T entity) throws FirestoreException;
    public T findById(String id) throws FirestoreException, FirestoreNotFoundException;
    public Collection<T> findAll() throws FirestoreException;
    public Collection<T> findWithQuery(Collection<FirestoreQueryParam> query) throws FirestoreException;
    public <R> Collection<R> findWithQuery(Collection<FirestoreQueryParam> query, Class<R> clazz) throws FirestoreException;
}