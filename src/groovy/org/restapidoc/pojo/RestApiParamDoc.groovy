package org.restapidoc.pojo

import org.restapidoc.annotation.RestApiParam
import org.restapidoc.annotation.RestApiParams

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType

import org.jsondoc.core.util.JSONDocUtils;

public class RestApiParamDoc {
	public String jsondocId = UUID.randomUUID().toString();
	private String name;
	private String description;
	private String type;
	private String required;
	private String[] allowedvalues;
	private String format;

	public RestApiParamDoc(String name, String description, String type, String required, String[] allowedvalues, String format) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.required = required;
		this.allowedvalues = allowedvalues;
		this.format = format;
	}

	public static List<RestApiParamDoc> getApiParamDocs(Method method, RestApiParamType paramType) {
		List<RestApiParamDoc> docs = new ArrayList<RestApiParamDoc>();
		Annotation[][] parametersAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parametersAnnotations.length; i++) {
			for (int j = 0; j < parametersAnnotations[i].length; j++) {
				if (parametersAnnotations[i][j] instanceof RestApiParam) {
					RestApiParamDoc apiParamDoc = buildFromAnnotation((RestApiParam) parametersAnnotations[i][j], getParamObjects(method, i), paramType);
					if(apiParamDoc != null) {
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
				if(type instanceof WildcardType) {
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

	public static RestApiParamDoc buildFromAnnotation(RestApiParam annotation, String type, RestApiParamType paramType) {
		if(annotation.paramType().equals(paramType)) {
			return new RestApiParamDoc(annotation.name(), annotation.description(), type, String.valueOf(annotation.required()), annotation.allowedvalues(), annotation.format());
		}
		return null;
	}

    public static RestApiParamDoc buildFromAnnotation(RestApiParam annotation, RestApiParamType paramType) {
        if(annotation.paramType().equals(paramType)) {
            return new RestApiParamDoc(annotation.name(), annotation.description(), annotation.type(), String.valueOf(annotation.required()), annotation.allowedvalues(), annotation.format());
        }
        return null;
    }

    public static List<RestApiParamDoc> buildFromAnnotation(RestApiParams annotation, RestApiParamType paramType) {
        List<RestApiParamDoc> docs = new ArrayList<RestApiParamDoc>();
        for (RestApiParam apiParam : annotation.params()) {
            docs.add(buildFromAnnotation(apiParam, paramType));
        }
        return docs;
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