package io.github.emrecankoc.firewrapper;

@FirestoreDocument
public class DocumentWithNonStringID {
    
    @FirestoreID
    private Integer id;
    private String key;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    
}