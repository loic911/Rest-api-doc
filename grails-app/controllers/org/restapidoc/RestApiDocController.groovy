package org.restapidoc

import grails.converters.JSON
import grails.util.Holders
import org.apache.commons.io.IOUtils
import org.restapidoc.pojo.RestApiObjectDoc
import org.restapidoc.utils.BuildPathMap
import org.restapidoc.utils.JSONDocUtilsLight
import org.restapidoc.utils.MappingRules


class RestApiDocController {

    def grailsApplication

    def index() {
    }

    def api() {
        InputStream doc = this.class.classLoader.getResourceAsStream(grailsApplication.mergedConfig.grails.plugins.restapidoc.outputFile)
        //File docFile = new File("grails-app/conf/"+grailsApplication.mergedConfig.grails.plugins.restapidoc.outputFile)
        render(IOUtils.toString(doc, "UTF-8"))
    }

//    def build() {
//        String CUSTOM_CLASS_NAME = grailsApplication.mergedConfig.grails.plugins.restapidoc.customClassName
//
//        BuildPathMap buildPathMap = new BuildPathMap()
//        MappingRules rules = buildPathMap.build(grailsApplication)
//
//        def classCustom = null
//        if(CUSTOM_CLASS_NAME) {
//            ClassLoader classLoader = this.class.getClassLoader();
//            log.info "Load class CUSTOM_CLASS_NAME=${CUSTOM_CLASS_NAME}"
//            println "Load class CUSTOM_CLASS_NAME=${CUSTOM_CLASS_NAME}"
//            classCustom = classLoader.loadClass(CUSTOM_CLASS_NAME)
//        }
//
//        def result = APIUtils.buildApiRegistry(grailsApplication,classCustom)
//        File docFile = new File(grailsApplication.mergedConfig.grails.plugins.restapidoc.outputFile)
//        docFile.write((result as JSON).toString(true))
//
//        render "JSON file has been created!<br/><br/>"
//        render "Go to http://.../restApiDoc/?doc_url=http://.../restApiDoc/api#<br/><br/>"
//        render "Example: http://localhost:8080/RestApiDoc-example/restApiDoc/?doc_url=http://localhost:8080/RestApiDoc-example/restApiDoc/api#<br/><br/>"
//
//
//    }
}
