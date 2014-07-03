package org.restapidoc.annotation

import java.lang.annotation.*

/**
 * This annotation is to be used inside an annotation of type ApiHeaders
 * @see RestApiHeader
 * @author Fabio Maffioletti
 *
 */
@Documented
@Target(value = ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiHeader {

    /**
     * The name of the header parameter
     * @return
     */
    public String name();

    /**
     * A description of what the parameter is needed for
     * @return
     */
    public String description();

}