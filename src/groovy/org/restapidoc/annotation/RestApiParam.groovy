package org.restapidoc.annotation

import org.restapidoc.pojo.RestApiParamType

import java.lang.annotation.*

/**
 * This annotation is to be used inside an annotation of type ApiParams
 * @author Fabio Maffioletti
 *
 */
@Documented
@Target(value = ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiParam {

    /**
     * The name of the url parameter, as expected by the server
     * @return
     */
    public String name();

    /**
     * A description of what the parameter is needed for
     * @return
     */
    public String description() default "";

    /**
     * Whether this parameter is required or not. Default value is true
     * @return
     */
    public boolean required() default true;

    /**
     * An array representing the allowed values this parameter can have. Default value is *
     * @return
     */
    public String[] allowedvalues() default [];

    /**
     * An enum representing the allowed values this parameter can have. Default value is *
     * @return
     */
    public Class<? extends Enum> allowedEnumValues() default Enum;

    /**
     * The format from the parameter (ex. yyyy-MM-dd HH:mm:ss, ...)
     * @return
     */
    public String format() default "";

    /**
     * Whether this is a path parameter or a query parameter
     * @return
     */
    public RestApiParamType paramType();

    /**
     * The type of the parameter (string, integer)
     * @return
     */
    public String type() default "";

}
