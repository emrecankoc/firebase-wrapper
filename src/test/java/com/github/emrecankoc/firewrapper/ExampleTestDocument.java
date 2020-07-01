package com.github.emrecankoc.firewrapper;

@FirestoreDocument(collection = "EXAMPLE_TEST_DOCUMENT")
public class ExampleTestDocument {
    
    @FirestoreID
    private String id;
    private String testStringField;
    private Long testLongField;
    private Integer testIntField;

    public ExampleTestDocument() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestStringField() {
        return testStringField;
    }

    public void setTestStringField(String testStringField) {
        this.testStringField = testStringField;
    }

    public Long getTestLongField() {
        return testLongField;
    }

    public void setTestLongField(Long testLongField) {
        this.testLongField = testLongField;
    }

    public Integer getTestIntField() {
        return testIntField;
    }

    public void setTestIntField(Integer testIntField) {
        this.testIntField = testIntField;
    }
    
}