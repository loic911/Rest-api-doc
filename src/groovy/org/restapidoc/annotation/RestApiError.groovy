package org.restapidoc.annotation

import java.lang.annotation.*

/**
 * This annotation is to be used inside an annotation of type ApiErrors
 * @see ApiErrors
 * @author Fabio Maffioletti
 *
 */
@Documented
@Target(value = ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiError {

    /**
     * The error code returned
     * @return
     */
    public String code();

    /**
     * A description of what the error code means
     * @return
     */
    public String description();

}