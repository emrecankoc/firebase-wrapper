package io.github.emrecankoc.firewrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FirestoreDocument {

    /**
     * Name of firestore collection name
     * @return
     */
    public String collection() default "";

}