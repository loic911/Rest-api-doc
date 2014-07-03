package org.restapidoc.annotation

import java.lang.annotation.*

/**
 * This annotation is to be used on your "service" class, for example controller classes in Spring MVC.
 * @author Fabio Maffioletti
 *
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApi {

    /**
     * A description of what the API does
     * @return
     */
    public String description();

    /**
     * The name of the API
     * @return
     */
    public String name();

}