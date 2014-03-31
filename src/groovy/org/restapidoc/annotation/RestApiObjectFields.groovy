package org.restapidoc.annotation

import java.lang.annotation.*

@Documented
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestApiObjectFields {

    public RestApiObjectField[] params();

}
