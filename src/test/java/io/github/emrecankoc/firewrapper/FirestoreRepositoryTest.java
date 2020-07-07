package io.github.emrecankoc.firewrapper;

import static org.junit.Assert.assertNotNull;

import com.google.cloud.firestore.Firestore;

import org.junit.Test;
import org.mockito.Mock;

public class FirestoreRepositoryTest {

    @Mock
    Firestore firestoreMock;
    
    @Test
    public void initialize_repo_without_crash() {
        FirestoreRepository<ExampleTestDocument> repo = new FirestoreRepository<>(firestoreMock, ExampleTestDocument.class);
        assertNotNull(repo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initialize_repo_with_nondocument_class_expect_crash() {
        new FirestoreRepository<>(firestoreMock, String.class);
        
    }
   
    @Test(expected = IllegalArgumentException.class)
    public void initialize_repo_without_id_expect_crash() {
        new FirestoreRepository<>(firestoreMock, DocumentWithoutFirestoreID.class);
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void initialize_repo_without_proper_const_expect_crash() {
        new FirestoreRepository<>(firestoreMock, DocumentWithoutProperConstructor.class);
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void initialize_repo_with_multiple_id_expect_crash() {
        new FirestoreRepository<>(firestoreMock, DocumentWithMultipleID.class);
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void initialize_repo_with_id_should_be_string_expect_crash() {
        new FirestoreRepository<>(firestoreMock, DocumentWithNonStringID.class);
        
    }
}