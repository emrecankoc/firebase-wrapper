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

   
}