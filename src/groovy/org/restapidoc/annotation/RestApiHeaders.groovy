package org.restapidoc.annotation

import java.lang.annotation.*

/**
 * This annotation is to be used on your method and contains an array of ApiHeade
 * @see RestApiHeader
 * @author Fabio Maffioletti
 *
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiHeaders {

    /**
     * An array of ApiHeader annotations
     * @see RestApiHeader
     * @return
     */
    public RestApiHeader[] headers();

}