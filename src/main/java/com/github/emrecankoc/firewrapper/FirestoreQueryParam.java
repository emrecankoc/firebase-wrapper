package com.github.emrecankoc.firewrapper;

import com.google.cloud.firestore.FieldPath;

public class FirestoreQueryParam {
    private FieldPath fieldPath;
    private Object parameter;
    private FirestoreQueryType queryType;

    public FieldPath getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(FieldPath fieldPath) {
        this.fieldPath = fieldPath;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public FirestoreQueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(FirestoreQueryType queryType) {
        this.queryType = queryType;
    }
    
}