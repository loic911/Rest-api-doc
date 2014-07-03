package org.restapidoc.annotation

import org.restapidoc.pojo.RestApiVerb

import java.lang.annotation.*

/**
 * Override ApiMethod to hbe allowed to have have no value for some fields
 * @author Loïc Rollus
 *
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiMethod {

    /**
     * The relative path for this method (ex. /country/get/{name})
     * @return
     */
    public String path() default "Undefined"; //should be RestApiMethodDoc.UNDEFINED!

    /**
     * A description of what the method does
     * @return
     */
    public String description();

    /**
     * The request verb (or method), to be filled with an ApiVerb value (GET, POST, PUT, DELETE)
     * @see RestApiVerb
     * @return
     */
    public RestApiVerb verb() default RestApiVerb.NULL;

    /**
     * An array of strings representing media types produced by the method, like application/json, application/xml, ...
     * @return
     */
    public String[] produces() default []; // [MediaType.APPLICATION_JSON_VALUE]

    /**
     * An array of strings representing media types consumed by the method, like application/json, application/xml, ...
     * @return
     */
    public String[] consumes() default [];

    /**
     * Extensions available for this service
     * If extensions = "jpg","png", there will be two service doc (myservice/picture.jpg and png)
     * @return
     */
    public String[] extensions() default [];

    //is it a listing action? => put max/offset
    public boolean listing() default false;
}
