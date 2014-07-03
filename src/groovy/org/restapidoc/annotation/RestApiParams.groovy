package org.restapidoc.annotation

import java.lang.annotation.*


/**
 * This annotation is to be used on your method and contains an array of ApiParam
 * @see ApiParam
 * @author Benjamin Stévens
 *
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiParams {

    /**
     * An array of ApiParam annotations
     * @see ApiParam
     * @return
     */
    public RestApiParam[] params();

}