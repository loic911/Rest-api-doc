package org.restapidoc

import groovy.util.logging.Log
import org.restapidoc.annotation.RestApi
import org.restapidoc.annotation.RestApiObject
import org.restapidoc.utils.JSONDocUtilsLight

/**
 * Created by stevben on 16/12/13.
 */
@Log
class APIUtils {

    static def buildApiRegistry(grailsApplication) {

        String VERSION = grailsApplication.mergedConfig.grails.plugins.restapidoc.docVersion
        String BASEPATH = grailsApplication.mergedConfig.grails.plugins.restapidoc.basePath
        String POJO_PKG_TO_SCAN = grailsApplication.mergedConfig.grails.plugins.restapidoc.packageToScan

        String CUSTOM_CLASS_NAME = grailsApplication.mergedConfig.grails.plugins.restapidoc.customClassName
        def customDoc = null
        if (CUSTOM_CLASS_NAME) {
            ClassLoader classLoader = APIUtils.getClassLoader();
            println "Custom doc class loading=${CUSTOM_CLASS_NAME}"
            customDoc = classLoader.loadClass(CUSTOM_CLASS_NAME)
        }



        JSONDocUtilsLight builder = new JSONDocUtilsLight(grailsApplication)

        //Retrieve all controlers (for method doc)
        log.info "Retrieve Controller..."
        Set<Class> controllersClasses = new LinkedList<Class>()
        grailsApplication.controllerClasses.findAll { it.clazz.isAnnotationPresent(RestApi) }
                .each { controllerArtefact ->
            def controllerClass = controllerArtefact.getClazz()
            controllersClasses.add(controllerClass)
        }

        //Retrieve all domains (for object doc)
        log.info "Retrieve Domain..."
        Set<Class<?>> objectClasses = new LinkedList<Class<?>>()
        grailsApplication.domainClasses.findAll { it.clazz.isAnnotationPresent(RestApiObject) }.each { domainArtefact ->
            def domainClass = domainArtefact.getClazz()
            objectClasses.add(domainClass)
        }

        //Retrieve all non-domain objects
        log.info "Retrieve non-Domain pojo..."
        if(POJO_PKG_TO_SCAN) {
            objectClasses.addAll ClasspathUtils.getClasses(POJO_PKG_TO_SCAN).findAll { it.isAnnotationPresent(RestApiObject) }
        }


        //Generate doc
        def objectsDoc = builder.getApiObjectDocs(objectClasses, customDoc)
        def controllerDoc = builder.getApiDocs(objectClasses, controllersClasses, grailsApplication)

        log.info "Doc builder is finished..."

        //Register doc
//        ApiRegistry.jsondoc =
        return ["version": VERSION,
                "basePath": BASEPATH,
                "apis": controllerDoc,
                "objects": objectsDoc]

    }
}
