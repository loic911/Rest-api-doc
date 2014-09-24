package org.restapidoc.pojo

import org.jsondoc.core.util.JSONDocUtils
import org.restapidoc.annotation.RestApiParam
import org.restapidoc.annotation.RestApiParams

import java.lang.annotation.Annotation
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

public class RestApiParamDoc {
    public String jsondocId = UUID.randomUUID().toString();
    private String name;
    private String description;
    private String type;
    private String required;
    private String[] allowedvalues;
    private String format;

    public RestApiParamDoc(String name, String description, String type, String required, String[] allowedvalues, Class<? extends Enum> allowedEnumValues, String format) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
        this.allowedvalues = allowedvalues;
        this.format = format;

        // If the allowed values must be extracted from an enum
        Enum[] enumValues = allowedEnumValues.getEnumConstants()
        if (enumValues != null) {
            this.allowedvalues = enumValues.collect { it.toString() }
        }
    }

    public static List<RestApiParamDoc> getApiParamDocs(Method method, RestApiParamType paramType) {
        List<RestApiParamDoc> docs = new ArrayList<RestApiParamDoc>();
        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (parametersAnnotations[i][j] instanceof RestApiParam) {
                    RestApiParamDoc apiParamDoc = buildFromAnnotation((RestApiParam) parametersAnnotations[i][j], getParamObjects(method, i), paramType);
                    if (apiParamDoc != null) {
                        docs.add(apiParamDoc);
                    }
                }
            }
        }

        return docs;
    }

    private static String getParamObjects(Method method, Integer index) {
        Class<?> parameter = method.getParameterTypes()[index];
        Type generic = method.getGenericParameterTypes()[index];
        if (Collection.class.isAssignableFrom(parameter)) {
            if (generic instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) generic;
                Type type = parameterizedType.getActualTypeArguments()[0];
                if (type instanceof WildcardType) {
                    return JSONDocUtils.WILDCARD;
                }
                Class<?> clazz = (Class<?>) type;
                return JSONDocUtils.getObjectNameFromAnnotatedClass(clazz);
            } else {
                return JSONDocUtils.UNDEFINED;
            }
        } else if (method.getReturnType().isArray()) {
            Class<?> classArr = parameter;
            return JSONDocUtils.getObjectNameFromAnnotatedClass(classArr.getComponentType());

        }
        return JSONDocUtils.getObjectNameFromAnnotatedClass(parameter);
    }

    public
    static RestApiParamDoc buildFromAnnotation(RestApiParam annotation, String type, RestApiParamType paramType) {
        if (annotation.paramType().equals(paramType)) {
            return new RestApiParamDoc(annotation.name(), annotation.description(), type, String.valueOf(annotation.required()), annotation.allowedvalues(), annotation.allowedEnumValues(), annotation.format());
        }
        return null;
    }

    public static RestApiParamDoc buildFromAnnotation(RestApiParam annotation, RestApiParamType paramType) {
        if (annotation.paramType().equals(paramType)) {
            return new RestApiParamDoc(annotation.name(), annotation.description(), annotation.type(), String.valueOf(annotation.required()), annotation.allowedvalues(), annotation.allowedEnumValues(), annotation.format());
        }
        return null;
    }

    public static List<RestApiParamDoc> buildFromAnnotation(RestApiParams annotation, RestApiParamType paramType, Method method = null) {
        List<RestApiParamDoc> docs = new ArrayList<RestApiParamDoc>();
        int index = 0
        for (RestApiParam apiParam : annotation.params()) {
            RestApiParamDoc paramDoc = buildFromAnnotation(apiParam, paramType)
            if(paramDoc!=null && paramDoc.type.trim().equals("")) {
                //no type define, try to get the type from the method parameter type
                paramDoc.type = getMethodParameterType(method,index)
            }
            docs.add(paramDoc);
            index++
        }
        return docs;
    }

    private static String getMethodParameterType(Method method, int index) {
        def parametersClass = method.getParameterTypes().collect{it.getSimpleName()}
        if(parametersClass.size()>index) {
            return parametersClass[index]
        }
        return ""
    }

    public RestApiParamDoc() {
        super();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRequired() {
        return required;
    }

    public String[] getAllowedvalues() {
        return allowedvalues;
    }

    public String getFormat() {
        return format;
    }

}