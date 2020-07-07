package io.github.emrecankoc.firewrapper;

@FirestoreDocument
public class DocumentWithMultipleID {
    
    @FirestoreID
    private String ID;
    @FirestoreID
    private String key;

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    
}