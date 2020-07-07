package io.github.emrecankoc.firewrapper;

@FirestoreDocument
public class DocumentWithoutFirestoreID {
    
    private String fieldOne;
    private String fieldTwo;

    public String getFieldOne() {
        return fieldOne;
    }

    public void setFieldOne(String fieldOne) {
        this.fieldOne = fieldOne;
    }

    public String getFieldTwo() {
        return fieldTwo;
    }

    public void setFieldTwo(String fieldTwo) {
        this.fieldTwo = fieldTwo;
    }

    
}