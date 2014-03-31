package org.restapidoc.annotation
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on your method and represents the possible errors returned by the method
 * @see RestApiError
 * @author Fabio Maffioletti
 *
 */
@Documented
@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiErrors {

    /**
     * An array of ApiError annotations
     * @see RestApiError
     * @return
     */
    public RestApiError[] apierrors();

}