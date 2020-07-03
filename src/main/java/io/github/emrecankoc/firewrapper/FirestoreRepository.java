package io.github.emrecankoc.firewrapper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class FirestoreRepository<T> implements IFirestoreRepository<T> {

    private static final String PARSE_EXCEPTION = "Exception occured when parsing fields for entity";
    private Class<T> clazz;
    private String collectionName;
    private Field firebaseIDField;
    private Map<String, Method> entityReadMethods;
    private Map<String, Method> entityWriteMethods;
    private Firestore firestore;

    public FirestoreRepository(Firestore firestore, Class<T> clazz) {
        this.firestore = firestore;
        this.clazz = clazz;

        if (!hasNoArgsPublicConstructor(this.clazz)) {
            throw new IllegalArgumentException("NoArgsConstructor not found for " + this.clazz.getSimpleName());
        }

        FirestoreDocument docAnnotation = this.clazz.getAnnotation(FirestoreDocument.class);
        if (docAnnotation == null) {
            throw new IllegalArgumentException("No @" + FirestoreDocument.class.getSimpleName()
                    + " annotation found for " + this.clazz.getSimpleName());
        }

        if (docAnnotation.collection() == null || docAnnotation.collection().isEmpty()) {
            collectionName = this.clazz.getSimpleName();
        } else {
            collectionName = docAnnotation.collection();
        }

        entityReadMethods = new HashMap<>();
        entityWriteMethods = new HashMap<>();

        Field[] fields = this.clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor((String) field.getName(), this.clazz);
                entityReadMethods.put(field.getName(), propertyDescriptor.getReadMethod());
                entityWriteMethods.put(field.getName(), propertyDescriptor.getWriteMethod());
            } catch (IntrospectionException e) {
                // TODO: add log
            }

            FirestoreID annotation = field.getAnnotation(FirestoreID.class);
            if (annotation == null) {
                continue;
            }

            if (firebaseIDField != null) {
                throw new IllegalArgumentException("Multiple @" + FirestoreID.class.getSimpleName()
                        + " annotated field found for " + this.clazz.getSimpleName());
            }

            if (field.getType() != String.class) {
                throw new IllegalArgumentException("@" + FirestoreID.class.getSimpleName() + " field must be string! ("
                        + this.clazz.getSimpleName() + "." + field.getName() + ")");
            }

            firebaseIDField = field;
        }

        if (firebaseIDField == null) {
            throw new IllegalArgumentException("No @" + FirestoreID.class.getSimpleName()
                    + " annotation specified for entity class " + this.clazz.getSimpleName());
        }

    }

    // #region private method region
    private boolean hasNoArgsPublicConstructor(Class<?> clazz) {
        if (clazz.getConstructors().length == 0) {
            // no constructor defined
            return true;
        }
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> entityToMap(T entity) throws FirestoreException {
        try {
            Map<String, Object> data = new HashMap<>();
            for (Field field : entity.getClass().getFields()) {
                data.put(field.getName(), entityReadMethods.get(field.getName()).invoke(entity));
            }
            return data;
        } catch (Exception e) {
            throw new FirestoreException(PARSE_EXCEPTION, e);
        }
    }

    private <R> R mapToEntity(Map<String, Object> data, Class<R> clazz) throws FirestoreException {
        try {
            R entity = (R) clazz.getDeclaredConstructor().newInstance();
            for (Entry<String, Object> item : data.entrySet()) {
                entityWriteMethods.get(item.getKey()).invoke(entity, item.getValue());
            }
            return entity;
        } catch (Exception e) {
            throw new FirestoreException(PARSE_EXCEPTION, e);
        }
    }

    private T mapToEntity(Map<String, Object> data) throws FirestoreException {
        return (T) mapToEntity(data, clazz);
    }
    // #region

    /**
     * Save entity to Firestore then returns id
     * 
     * @param entity
     * @return
     */
    public String save(T entity) throws FirestoreException {
        try {
            String id = (String) entityReadMethods.get(firebaseIDField.getName()).invoke(entity);

            DocumentReference doc = id != null ? getCollection().document(id) : getCollection().document();

            doc.set(entityToMap(entity)).get();
            return doc.getId();
        } catch (InterruptedException | ExecutionException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new FirestoreException(e);
        }
    }

    protected CollectionReference getCollection() {
        return firestore.collection(collectionName);
    }

    public Collection<T> findAll() throws FirestoreException {
        try {
            ApiFuture<QuerySnapshot> query = getCollection().get();
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();
            Collection<T> result = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                result.add(mapToEntity(doc.getData()));
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestoreException(e);
        }

    }

    public T findById(String id) throws FirestoreException, FirestoreNotFoundException {
        try {
            DocumentSnapshot documentSnapshot;
            documentSnapshot = getCollection().document(id).get().get();
            if (documentSnapshot == null) {
                throw new FirestoreNotFoundException();
            }
            return mapToEntity(documentSnapshot.getData());
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestoreException(e);
        }

    }

    public void deleteById(String id) throws FirestoreException {
        try {
            getCollection().document(id).delete().get();
        } catch (Exception e) {
            throw new FirestoreException(e);
        }
    }

    /**
     * Query parameters must be added correct order
     * 
     * @param queryParameters
     * @return
     * @throws FirestoreException
     */
    public <R> Collection<R> findWithQuery(Collection<FirestoreQueryParam> queryParameters, Class<R> clazz)
            throws FirestoreException {
        try {
            Query query = getCollection();
            for (FirestoreQueryParam q : queryParameters) {
                switch (q.getQueryType()) {
                    case EQUAL:
                        query = query.whereEqualTo(q.getFieldPath(), q.getParameter());
                        break;
                    case GREATER:
                        query = query.whereGreaterThan(q.getFieldPath(), q.getParameter());
                    case GREATER_EQUAL:
                        query = query.whereGreaterThanOrEqualTo(q.getFieldPath(), q.getParameter());
                    case LESSER:
                        query = query.whereLessThan(q.getFieldPath(), q.getParameter());
                    case LESSER_EQUAL:
                        query = query.whereLessThanOrEqualTo(q.getFieldPath(), q.getParameter());
                    case ARRAY_CONTAINS:
                        query = query.whereArrayContains(q.getFieldPath(), q.getParameter());
                    case IN:
                        query = query.whereIn(q.getFieldPath(), (List<?>) q.getParameter());

                    default:
                        break;
                }
            }

            List<QueryDocumentSnapshot> documents = query.get().get().getDocuments();
            Collection<R> result = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                result.add(mapToEntity(doc.getData(), clazz));
            }

            return result;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestoreException(e);
        }
    }

    /**
     * Query parameters must be added correct order
     * 
     * @param queryParameters
     * @return
     * @throws FirestoreException
     */
    public Collection<T> findWithQuery(Collection<FirestoreQueryParam> queryParameters) throws FirestoreException {
        return findWithQuery(queryParameters, clazz);
    }

}