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
        //InputStream doc = this.class.classLoader.getResourceAsStream(grailsApplication.mergedConfig.grails.plugins.restapidoc.outputFile)
        File docFile = new File(grailsApplication.mergedConfig.grails.plugins.restapidoc.outputFileReading)
        render(docFile.text)
    }
}
