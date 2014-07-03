package org.restapidoc.pojo

import org.jsondoc.core.pojo.ApiBodyObjectDoc

public class RestApiBodyObjectDoc extends ApiBodyObjectDoc {
    public String jsondocId = UUID.randomUUID().toString();
//    private String object;
//    private String multiple;
//    private String mapKeyObject;
//    private String mapValueObject;
//    private String map;

//    public static RestApiBodyObjectDoc buildFromAnnotation(Method method) {
//        boolean multiple = false;
//        Integer index = -1; // to implement (ssee jsondoc source)
//        if (index != -1) {
//            Class<?> parameter = method.getParameterTypes()[index];
//            multiple = JSONDocUtils.isMultiple(parameter);
//            return new RestApiBodyObjectDoc(getBodyObject(method, index)[0], getBodyObject(method, index)[1], getBodyObject(method, index)[2], String.valueOf(multiple), getBodyObject(method, index)[3]);
//        }
//        return null;
//    }

    public RestApiBodyObjectDoc(String object, String mapKeyObject, String mapValueObject, String multiple, String map) {
        super(object, mapKeyObject, mapValueObject, multiple, map);
    }

}