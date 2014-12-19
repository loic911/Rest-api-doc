package org.restapidoc.annotation

import java.lang.annotation.*

/**
 * This annotation is to be used on your method and represents the parameter passed in the body of the requests
 * @author Fabio Maffioletti
 *
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiBodyObject {

    /**
     *The name of Body Object
     *if name = "null", do not need a body
     * @return
     */
    public String name();

}
