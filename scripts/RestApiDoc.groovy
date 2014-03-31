import grails.converters.JSON
import org.restapidoc.APIUtils
import org.restapidoc.ApiRegistry
import org.restapidoc.JSONUtils
import org.restapidoc.utils.BuildPathMap
import org.restapidoc.utils.MappingRules
import org.codehaus.groovy.grails.commons.UrlMappingsArtefactHandler
import org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationHolder
import org.codehaus.groovy.grails.web.converters.configuration.DefaultConverterConfiguration
import org.codehaus.groovy.grails.web.converters.marshaller.json.CollectionMarshaller
import org.codehaus.groovy.grails.web.converters.marshaller.json.GenericJavaBeanMarshaller
import org.codehaus.groovy.grails.web.converters.marshaller.json.MapMarshaller
import org.codehaus.groovy.grails.web.json.JSONObject

includeTargets << grailsScript("_GrailsBootstrap")

//DOESN'T WORK YET!!!!!....JSON is not well output...

target(restApiDoc: "Build Rest Api Doc in a JSON file") {
    depends(classpath, compile, loadApp)

    DefaultConverterConfiguration<JSON> defaultConverterConfig = new  DefaultConverterConfiguration<JSON>()
    defaultConverterConfig.registerObjectMarshaller(new CollectionMarshaller())
    defaultConverterConfig.registerObjectMarshaller(new MapMarshaller())
    defaultConverterConfig.registerObjectMarshaller(new GenericJavaBeanMarshaller())

    ConvertersConfigurationHolder.setTheadLocalConverterConfiguration(JSON.class, defaultConverterConfig);

    def apiUtils = classLoader.loadClass('org.restapidoc.APIUtils')
    def jsonUtils = classLoader.loadClass('org.restapidoc.JSONUtils')

    try {
    println("Start build JSON doc...")

   jsonUtils.registerMarshallers ()

    def result = apiUtils.buildApiRegistry(grailsApp,null)
    File docFile = new File("doc.json")

    println((result as JSON).toString())
    println(new JSONObject(result).toString())

    docFile.write(new JSONObject(result).toString())
    } catch(Exception e) {
        println e
    }

}

setDefaultTarget(restApiDoc)
