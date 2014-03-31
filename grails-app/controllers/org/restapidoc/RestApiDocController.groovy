package org.restapidoc

import grails.converters.JSON
import grails.util.Holders
import org.restapidoc.utils.BuildPathMap
import org.restapidoc.utils.MappingRules


class RestApiDocController {

    def grailsApplication

    def index() {
    }

    def api() {
        File docFile = new File(grailsApplication.mergedConfig.grails.plugins.restapidoc.outputFile)
        render(docFile.text)
    }

    def build() {
        String CUSTOM_CLASS_NAME = grailsApplication.mergedConfig.grails.plugins.restapidoc.customClassName

        BuildPathMap buildPathMap = new BuildPathMap()
        MappingRules rules = buildPathMap.build(grailsApplication)
        render rules.rules

        def classCustom = null
        if(CUSTOM_CLASS_NAME) {
            ClassLoader classLoader = this.class.getClassLoader();
            log.info "Load class CUSTOM_CLASS_NAME=${CUSTOM_CLASS_NAME}"
            println "Load class CUSTOM_CLASS_NAME=${CUSTOM_CLASS_NAME}"
            classCustom = classLoader.loadClass(CUSTOM_CLASS_NAME)
        }

        def result = APIUtils.buildApiRegistry(grailsApplication,classCustom)
        File docFile = new File(grailsApplication.mergedConfig.grails.plugins.restapidoc.outputFile)
        docFile.write((result as JSON).toString(true))
    }

}
