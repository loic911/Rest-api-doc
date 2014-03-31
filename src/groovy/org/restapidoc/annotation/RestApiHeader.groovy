package org.restapidoc.annotation
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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