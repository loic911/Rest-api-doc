package org.restapidoc.pojo

import grails.util.Holders
import groovy.util.logging.Log
import org.jsondoc.core.pojo.ApiObjectDoc
import org.jsondoc.core.pojo.ApiObjectFieldDoc
import org.restapidoc.annotation.RestApiObject
import org.restapidoc.annotation.RestApiObjectField
import org.restapidoc.annotation.RestApiObjectFields
import org.restapidoc.utils.JSONDocUtilsLight

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * RestApiMethodDoc must be used instead of ApiMethodDoc to use a light rest api doc
 * @author Loïc Rollus
 *
 */
@Log
public class RestApiObjectDoc extends ApiObjectDoc {

    public RestApiObjectDoc(String name, String description, List<RestApiObjectFieldDoc> fields) {
        super(name, description, fields);
    }

    /**
     * Build an object Doc from a domain annotation
     */
    @SuppressWarnings("rawtypes")
    public static RestApiObjectDoc buildFromAnnotation(RestApiObject annotation, boolean isDomain, Class clazz,
                                                       def grailsDomainDefaultType, def defaultObjectFields) {
        buildFromAnnotation(annotation.name(), annotation.description(), isDomain, clazz, grailsDomainDefaultType, defaultObjectFields, false)
    }

    /**
     * Build an object Doc from param data
     * @param custom Not a real grails domain
     */
    @SuppressWarnings("rawtypes")
    public static RestApiObjectDoc buildFromAnnotation(String name, String description, boolean isDomain, Class clazz,
                                                       def grailsDomainDefaultType,
                                                       def defaultObjectFields, boolean custom) {
        List<ApiObjectFieldDoc> fieldDocs = new ArrayList<ApiObjectFieldDoc>();

        //map that store: key=json field name and value = [type: field class, description: field desc,...]
        Map<String, Map<String, String>> annotationsMap = new TreeMap<String, Map<String, String>>()
        log.info "\tProcess domain ${name} ..."
//        def domain = Holders.getGrailsApplication().getDomainClasses().find {
//            it.shortName.equals(clazz.simpleName)
//        }

        //build map field (with super class too)
        if (isDomain) {
            //its a grails domain

            //analyse all fields for each classes and superclass
            Class classToProcess = clazz
            while (classToProcess.simpleName != "Object") {
                log.info "classToProcess=$classToProcess"
                //move from sub class to parent class while parent class is not the Java Object class
                fillAnnotationMap(classToProcess, annotationsMap, null, grailsDomainDefaultType, defaultObjectFields)
                classToProcess = classToProcess.superclass
            }

            //all fields from json
            def fieldList

            try {
                //build map with json by calling getDataFromDomain
                Method m = clazz.getDeclaredMethod("getDataFromDomain", Object);
                def arrayWithNull = new String[1]
                arrayWithNull[0] = null
                //invoke getDataFromDomain with null parameter
                fieldList = m.invoke(null, arrayWithNull).collect { it.key };

            } catch (NoSuchMethodException e) {
                //if no getDataFromDomain method, we suppose that all class field documented are in json
                fieldList = annotationsMap.collect { it.key }
            }

            log.info "\t\tFind ${fieldList.size()} fields..."


            fieldList.each {
                log.info "\t\tFind field ${it}..."
                def metadata = annotationsMap.get(it)
                def type = "Undefined"
                def desc = "Undefined"
                def useForCreation = true
                def mandatory = false
                def defaultValue = "Undefined"
                def presentInResponse = true

                if (metadata) {
                    type = metadata.type
                    desc = metadata.description
                    useForCreation = metadata.useForCreation
                    mandatory = metadata.mandatory
                    defaultValue = metadata.defaultValue
                    presentInResponse = metadata.presentInResponse
                }
                fieldDocs.add(buildFieldDocs(it.toString(), desc, type, useForCreation, mandatory, defaultValue, presentInResponse));
                annotationsMap.remove(it)
            }

        } else {
            //its custom response doc, don't use json
            fillAnnotationMap(clazz, annotationsMap, name, grailsDomainDefaultType, defaultObjectFields)
        }

        //not in json but defined in project domain
        annotationsMap.each {
            def value = it.value
            fieldDocs.add(buildFieldDocs(it.key.toString(), value['description'], value['type'], value['useForCreation'], value['mandatory'], value['defaultValue'], false));
        }

        return new RestApiObjectDoc(custom ? "[" + name + "]" : name, description, fieldDocs);
    }

    static RestApiObjectFieldDoc buildFieldDocs(String name, String description, String type, Boolean useForCreation, Boolean mandatory, String defaultValue, Boolean presentInResponse) {
        RestApiObjectFieldDoc apiPojoFieldDoc = new RestApiObjectFieldDoc();
        apiPojoFieldDoc.setName(name)
        apiPojoFieldDoc.setDescription(description)
        apiPojoFieldDoc.setType(type)
        apiPojoFieldDoc.setMultiple(String.valueOf(JSONDocUtilsLight.isMultiple(type)))
        apiPojoFieldDoc.useForCreation = useForCreation
        apiPojoFieldDoc.mandatory = mandatory
        apiPojoFieldDoc.defaultValue = defaultValue
        apiPojoFieldDoc.presentInResponse = presentInResponse
        return apiPojoFieldDoc;
    }

    //take class and fill the map with field metadata (from annotation)
    static void fillAnnotationMap(
            def domainClass,
            def annotationsMap, String fieldname, def grailsDomainDefaultType, def defaultObjectFields) {

        //add fiels from class
        domainClass.declaredFields.each { field ->
            if (fieldname == null || field.name.equals(fieldname)) {
                if (fieldname == null) {
                    //if fieldname!=null => custom field from CustomResponseDoc, so skip this annotation
                    if (field.isAnnotationPresent(RestApiObjectField.class)) {
                        def annotation = field.getAnnotation(RestApiObjectField.class)
                        addAnnotationToMap(annotationsMap, field, annotation, grailsDomainDefaultType)
                    }
                }
                if (field.isAnnotationPresent(RestApiObjectFields.class)) {
                    def annotation = field.getAnnotation(RestApiObjectFields.class)
                    annotation.params().each { apiObjectFieldsLight ->
                        addAnnotationToMap(annotationsMap, field, apiObjectFieldsLight, grailsDomainDefaultType)
                    }
                }
            }
        }

        //add default fields
        if (isGrailsDomain(domainClass.name)) {
            defaultObjectFields.each {
                annotationsMap.put(it.name, it)
            }
        }
    }

    //add field metadata to a map. Use field data if annotation data is missing
    static def addAnnotationToMap(Map<String, Map<String, String>> map, Field field, RestApiObjectField annotation,
                                  def grailsDomainDefaultType) {
        def annotationData = [:]

        if (annotation.allowedType().equals("")) {
            //if field is domain class (book.author) => author_id is a long
            if (isGrailsDomain(field.type.name)) {
                if (grailsDomainDefaultType) {
                    annotationData['type'] = grailsDomainDefaultType
                } else {
                    annotationData['type'] = field.type.simpleName
                }
            } else {
                //auto retrieve the type the field
                String[] typeChecks = RestApiObjectFieldDoc.getFieldObject(field);
                annotationData['type'] = JSONDocUtilsLight.firstNameUpper(typeChecks[0]);
            }
        } else {
            //if user set field type in annotation, get this value
            annotationData['type'] = annotation.allowedType()
        }


        if (annotation.apiFieldName().equals("")) {
            annotationData['name'] = field.getName()
        } else {
            annotationData['name'] = annotation.apiFieldName()
        }

        annotationData["description"] = annotation.description()

        annotationData["useForCreation"] = annotation.useForCreation()
        annotationData["mandatory"] = annotation.useForCreation() ? annotation.mandatory() : false

        annotationData["presentInResponse"] = annotation.presentInResponse()

        if (annotationData["useForCreation"] && !annotationData["mandatory"]) {
            annotationData["defaultValue"] = findDefaultValue(annotationData['type'].toString(), annotation.defaultValue())

        }
        map.put(annotationData['name'], annotationData)
    }

    /**
     * Get the default value for a type in java (0 for number, false for bool,...)
     */
    static String findDefaultValue(String type, String defaultValue) {
        if (!defaultValue.equals("")) {
            return defaultValue
        } else {
            if (type.equals("long") || type.equals("int") || type.equals("integer")) return "0 or null if domain"
            if (type.equals("list")) return "[]"
            if (type.equals("boolean")) return "false"

        }
        return "Undefined"
    }

    public static boolean isGrailsDomain(String fullName) {
        def domain = Holders.getGrailsApplication().getDomainClasses().find {
            it.fullName.equals(fullName)
        }
        return domain != null
    }

}