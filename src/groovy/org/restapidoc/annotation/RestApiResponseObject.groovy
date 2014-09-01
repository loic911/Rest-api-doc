package org.restapidoc.annotation

import java.lang.annotation.*

/**
 * This annotation is to be used on your method and represents the returned value
 * @author Fabio Maffioletti
 *
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiResponseObject {

    /**
     * ApiObject Idenfitier
     * @return
     */
    public String objectIdentifier() default "";

}