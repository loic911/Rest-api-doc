package org.restapidoc.utils

import grails.util.Holders
import groovy.util.logging.Log
import org.restapidoc.annotation.RestApi
import org.restapidoc.annotation.RestApiBodyObject
import org.restapidoc.annotation.RestApiErrors
import org.restapidoc.annotation.RestApiHeaders
import org.restapidoc.annotation.RestApiMethod
import org.restapidoc.annotation.RestApiObject
import org.restapidoc.annotation.RestApiObjectField
import org.restapidoc.annotation.RestApiParams
import org.restapidoc.annotation.RestApiResponseObject
import org.restapidoc.pojo.RestApiBodyObjectDoc
import org.restapidoc.pojo.RestApiDoc
import org.restapidoc.pojo.RestApiErrorDoc
import org.restapidoc.pojo.RestApiObjectDoc
import org.restapidoc.pojo.RestApiParamDoc
import org.restapidoc.pojo.RestApiParamType
import org.restapidoc.pojo.RestApiResponseObjectDoc
import org.jsondoc.core.pojo.ApiBodyObjectDoc
import org.jsondoc.core.pojo.ApiDoc
import org.jsondoc.core.pojo.ApiParamType
import org.jsondoc.core.util.JSONDocUtils
import org.restapidoc.pojo.RestApiMethodDoc
import org.restapidoc.pojo.RestApiVerb

import java.beans.Introspector
import java.lang.reflect.Method

@Log
public class JSONDocUtilsLight extends JSONDocUtils {

    String DEFAULT_TYPE
    String CONTROLLER_PREFIX
    String CONTROLLER_SUFFIX
    def DEFAULT_ERROR_ALL
    def DEFAULT_ERROR_GET
    def DEFAULT_ERROR_POST
    def DEFAULT_ERROR_PUT
    def DEFAULT_ERROR_DELETE
    def VERB_PER_METHOD_PREFIX
    def PATH_PER_METHOD_PREFIX
    String DEFAULT_FORMAT
    String GRAILS_DOMAIN_DEFAULT_TYPE
    def DEFAULT_PARAMS_QUERY_ALL
    def DEFAULT_PARAMS_QUERY_SINGLE
    def DEFAULT_PARAMS_QUERY_MULTIPLE
    def DOMAIN_OBJECT_FIELDS


    public JSONDocUtilsLight(grailsApplication) {
        DEFAULT_TYPE = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultResponseType
        CONTROLLER_PREFIX = grailsApplication.mergedConfig.grails.plugins.restapidoc.controllerPrefix
        CONTROLLER_SUFFIX = grailsApplication.mergedConfig.grails.plugins.restapidoc.controllerSuffix
        DEFAULT_ERROR_ALL = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultErrorAll
        DEFAULT_ERROR_GET = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultErrorGet
        DEFAULT_ERROR_POST = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultErrorPost
        DEFAULT_ERROR_PUT = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultErrorPut
        DEFAULT_ERROR_DELETE = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultErrorDelete
        VERB_PER_METHOD_PREFIX = grailsApplication.mergedConfig.grails.plugins.restapidoc.verbPerMethodPrefix
        PATH_PER_METHOD_PREFIX = grailsApplication.mergedConfig.grails.plugins.restapidoc.pathPerMethodPrefix
        DEFAULT_FORMAT = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultFormat
        GRAILS_DOMAIN_DEFAULT_TYPE = grailsApplication.mergedConfig.grails.plugins.restapidoc.grailsDomainDefaultType
        DOMAIN_OBJECT_FIELDS = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultObjectFields
        DEFAULT_PARAMS_QUERY_ALL = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultParamsQueryAll
        DEFAULT_PARAMS_QUERY_SINGLE = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultParamsQuerySingle
        DEFAULT_PARAMS_QUERY_MULTIPLE = grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultParamsQueryMultiple
    }

//    

    /**
     * Build Doc for controller method
     * @param classes Controllers classes
     * @return Controller method doc
     */
    @Override
    public Set<ApiDoc> getApiDocs(Set<Class<?>> classes, def grailsApplication) {
        Set<ApiDoc> apiDocs = new TreeSet<ApiDoc>();

        //build map with ["controller.action" => path and verb]
        log.info "Build path map..."
        BuildPathMap buildPathMap = new BuildPathMap()
        MappingRules rules = buildPathMap.build(grailsApplication)

        //For each controller, build doc from annotation and build method doc
        for (Class<?> controller : classes) {
            RestApiDoc apiDoc = RestApiDoc.buildFromAnnotation(controller.getAnnotation(RestApi.class));
            apiDoc.setMethods(getApiMethodDocs(controller,rules));
            apiDocs.add(apiDoc);
        }
        return apiDocs;
    }

    /**
     * Build doc for domain
     * @param classes Domain classes
     * @return Domain object doc
     */
    @Override
    public Set<RestApiObjectDoc> getApiObjectDocs(Set<Class<?>> classes,def customResponseDoc) {

        Set<RestApiObjectDoc> pojoDocs = new TreeSet<RestApiObjectDoc>();
        for (Class<?> pojo : classes) {
            RestApiObject annotation = pojo.getAnnotation(RestApiObject.class);
            RestApiObjectDoc pojoDoc = RestApiObjectDoc.buildFromAnnotation(annotation, true,pojo,GRAILS_DOMAIN_DEFAULT_TYPE,DOMAIN_OBJECT_FIELDS);
            if(annotation.show()) {
                pojoDocs.add(pojoDoc);
            }
        }

        //if response is not "standard" (with json) simply use CustomResponseDoc class
        if(customResponseDoc) {
            customResponseDoc.declaredFields.each { field ->
                println "field=$field"
                if(field.isAnnotationPresent(RestApiObjectField.class)) {
                    def annotation = field.getAnnotation(RestApiObjectField.class)
                    RestApiObjectDoc pojoDoc = RestApiObjectDoc.buildFromAnnotation(field.name,annotation.description(),false,customResponseDoc, GRAILS_DOMAIN_DEFAULT_TYPE,DOMAIN_OBJECT_FIELDS,true);
                    pojoDocs.add(pojoDoc);
                }
            }
        }
        return pojoDocs;
    }

    /**
     * Build method doc object for all controller methods
     */
    @Override
    private List<RestApiMethodDoc> getApiMethodDocs(Class<?> controller, MappingRules rules) {
        log.info "\tProcess controller ${controller} ..."
        List<RestApiMethodDoc> apiMethodDocs = new ArrayList<RestApiMethodDoc>();
        Method[] methods = controller.getMethods();

        for (Method method : methods) {

            if(method.isAnnotationPresent(RestApiMethod.class)) {
                log.info "\t\tProcess method ${method} ..."


                //Retrieve the path/verb to go to this method
                MappingRulesEntry rule = rules.getRule(controller.simpleName,method.name)
                String verb = method.getAnnotation(RestApiMethod.class).verb().name()
                String path

                def annotation = method.getAnnotation(RestApiMethod.class)
                if(!annotation.path().equals("Undefined")) {
                    //path is defined in the annotation
                    path = method.getAnnotation(RestApiMethod.class).path()
                } else if(rule){
                    //path is defined in the urlmapping
                    path = rule.path

                } else {
                    //nothing is defined
                    String controllerName = controller.simpleName
                    if(controllerName.endsWith(CONTROLLER_SUFFIX)) {
                        controllerName = controllerName.substring(0,controllerName.size()-CONTROLLER_SUFFIX.size())
                    }
                    controllerName = splitCamelToBlank(Introspector.decapitalize(controllerName))

                    String actionWithPathParam = ""
                    PATH_PER_METHOD_PREFIX.each {
                        if(method.name.startsWith(it.key)) {
                            actionWithPathParam = "/"+actionWithPathParam + it.value
                        }
                    }
                    println "controllerName=$controllerName"
                    println "actionWithPathParam=$actionWithPathParam"

                    path = "/"+controllerName + actionWithPathParam + ".${DEFAULT_FORMAT}"
                }

                if(annotation.verb()!=RestApiVerb.NULL) {
                    //verb is defined in the annotation
                    verb = method.getAnnotation(RestApiMethod.class).verb().name().toUpperCase()
                } else if(rule){
                    //verb is defined in the urlmapping
                    verb = rule.verb

                } else {
                    //if no explicit url mapping rules, take dynamic rule
                    VERB_PER_METHOD_PREFIX.each {
                        if(method.name.startsWith(it.key)) {
                            verb = it.value
                        }
                    }
                }


                RestApiMethodDoc apiMethodDoc = RestApiMethodDoc.buildFromAnnotation(method.getAnnotation(RestApiMethod.class),path,verb,DEFAULT_TYPE);
                apiMethodDoc.methodName = method.name

                if(method.isAnnotationPresent(RestApiHeaders.class)) {
                    apiMethodDoc.setHeaders(RestApiMethodDoc.buildFromAnnotation(method.getAnnotation(RestApiHeaders.class)));
                }

                def urlParams = []
                def queryParameters = []
                if(method.isAnnotationPresent(RestApiParams.class)) {
                    urlParams = RestApiParamDoc.buildFromAnnotation(method.getAnnotation(RestApiParams.class), RestApiParamType.PATH)
                    queryParameters = RestApiParamDoc.buildFromAnnotation(method.getAnnotation(RestApiParams.class), RestApiParamType.QUERY)
                }

                DEFAULT_PARAMS_QUERY_ALL.each {
                    queryParameters.add(new RestApiParamDoc(it.name, it.description, it.type, "false", new String[0], ""))
                }

                if(method.getAnnotation(RestApiMethod.class).listing()) {
                    DEFAULT_PARAMS_QUERY_MULTIPLE.each {
                        queryParameters.add(new RestApiParamDoc(it.name, it.description, it.type, "false", new String[0], ""))
                    }
                } else {
                    DEFAULT_PARAMS_QUERY_SINGLE.each {
                        queryParameters.add(new RestApiParamDoc(it.name, it.description, it.type, "false", new String[0], ""))
                    }
                }

                apiMethodDoc.setPathparameters(urlParams.minus(null));
                apiMethodDoc.setQueryparameters(queryParameters.minus(null));

                if(method.isAnnotationPresent(RestApiBodyObject.class)) {
                    apiMethodDoc.setBodyobject(RestApiBodyObjectDoc.buildFromAnnotation(method));
                } else if(verb.equals("POST") || verb.equals("PUT")) {
                    String currentDomain = getControllerDomainName(controller)
                    apiMethodDoc.setBodyobject(new ApiBodyObjectDoc(currentDomain, "", "", "Unknow", ""));
                }

                if(method.isAnnotationPresent(RestApiResponseObject.class)) {
                    apiMethodDoc.setResponse(RestApiResponseObjectDoc.buildFromAnnotation(method.getAnnotation(RestApiResponseObject.class), method));
                } else {
                    String currentDomain = getControllerDomainName(controller)
                    apiMethodDoc.setResponse(new RestApiResponseObjectDoc(currentDomain, "", "", "Unknow", ""))
                }

                List<RestApiErrorDoc> errors = []
                if(method.isAnnotationPresent(RestApiErrors.class)) {
                    errors = RestApiErrorDoc.buildFromAnnotation(method.getAnnotation(RestApiErrors.class))
                }

                //add default errors
                DEFAULT_ERROR_ALL.each { code ->
                    if(!errors.find{it.code.equals(code.key)}) {
                        errors.add(new RestApiErrorDoc(code.key, code.value));
                    }
                }

                if(verb.equals("GET")) {
                    DEFAULT_ERROR_GET.each { code ->
                        if(!errors.find{it.code.equals(code.key)}) {
                            errors.add(new RestApiErrorDoc(code.key, code.value));
                        }
                    }
                } else if(verb.equals("POST")) {
                    DEFAULT_ERROR_POST.each { code ->
                        if(!errors.find{it.code.equals(code.key)}) {
                            errors.add(new RestApiErrorDoc(code.key, code.value));
                        }
                    }
                } else if(verb.equals("PUT")) {
                    DEFAULT_ERROR_PUT.each { code ->
                        if(!errors.find{it.code.equals(code.key)}) {
                            errors.add(new RestApiErrorDoc(code.key, code.value));
                        }
                    }
                } else if(verb.equals("DELETE")) {
                    DEFAULT_ERROR_DELETE.each { code ->
                        if(!errors.find{it.code.equals(code.key)}) {
                            errors.add(new RestApiErrorDoc(code.key, code.value));
                        }
                    }
                }
                apiMethodDoc.setApierrors(errors)

                log.info "\t\t\tapiMethodDoc: ${apiMethodDoc.description} ..."
                apiMethodDocs.add(apiMethodDoc);
            }
        }
        return apiMethodDocs;
    }

    public static boolean isMultiple(String className) {
        if(className.toLowerCase().equals("list") || className.toLowerCase().equals("map")) {
            return true;
        }
        return false;
    }

    public String getControllerDomainName(Class controller) {
        try {
            Method m = controller.getDeclaredMethod("currentDomainName", Object)
            //if a method currentDomainName exist in controller, get its String
            return controller.newInstance().currentDomainName()
        } catch(NoSuchMethodException e) {
            //if not overrided, compute the domain name thanks to the controller name
            String controllerName = controller.simpleName

            if(controllerName.startsWith(CONTROLLER_PREFIX)) {
                controllerName = controllerName.substring(CONTROLLER_PREFIX.size(),controllerName.size())
            }

            if(controllerName.endsWith(CONTROLLER_SUFFIX)) {
                controllerName = controllerName.substring(0,controllerName.size()-CONTROLLER_SUFFIX.size())
            }
            splitCamelToBlank(Introspector.decapitalize(controllerName))
        }
    }

    static String splitCamelToBlank(String stringToSplit) {
        String result = ""
        for(int i=0;i<stringToSplit.size();i++) {
            def car = stringToSplit[i]
            if(car != car.toUpperCase()) {
                result = result + car
            } else {
                result = result + " " + car.toLowerCase()
            }
        }
        return result.trim()
    }

    static String firstNameUpper(String str) {
        if(str && str.size()>0) {
            return str.substring(0, 1).toUpperCase()+str.substring(1);
        }
        return str
    }
}