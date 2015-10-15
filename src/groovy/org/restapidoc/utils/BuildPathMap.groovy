package org.restapidoc.utils

import grails.util.Holders
import org.codehaus.groovy.grails.commons.UrlMappingsArtefactHandler
import org.codehaus.groovy.grails.web.mapping.UrlMapping
import org.codehaus.groovy.grails.web.mapping.reporting.AnsiConsoleUrlMappingsRenderer

import java.util.regex.Pattern

/**
 * Thanks to URL MAPPING files, build a map that helps to get path/verb for a specific controller action
 *
 * Created by lrollus on 1/10/14.
 */
class BuildPathMap extends AnsiConsoleUrlMappingsRenderer {

    /**
     * Build a MappingRules (map controller.action => path + verb) thanks to url mapping
     */
    MappingRules build(def grailsApplication) {

        def mappings = Holders.getGrailsApplication().getArtefacts(UrlMappingsArtefactHandler.TYPE)
        def evaluator = Holders.getGrailsApplication().classLoader.loadClass("org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingEvaluator").newInstance(Holders.getGrailsApplication().classLoader.loadClass('org.springframework.mock.web.MockServletContext').newInstance())
        def allMappings = []

        for (m in mappings) {
            List grailsClassMappings
            if (Script.isAssignableFrom(m.getClazz())) {
                grailsClassMappings = evaluator.evaluateMappings(m.getClazz())
            } else {
                grailsClassMappings = evaluator.evaluateMappings(m.getMappingsClosure())
            }
            allMappings.addAll(grailsClassMappings)
        }
        return createUrlMappingMap(allMappings, grailsApplication)

    }

    private MappingRules createUrlMappingMap(List<UrlMapping> urlMappings, def grailsApplication) {

        MappingRules rules = new MappingRules()

        final mappingsByController = urlMappings.groupBy { UrlMapping mapping -> mapping.controllerName }
        def longestMapping = establishUrlPattern(urlMappings.max { UrlMapping mapping -> establishUrlPattern(mapping, false).length() }, false).length() + 5
        final controllerNames = mappingsByController.keySet().sort()

        for (controller in controllerNames) {
            final controllerUrlMappings = mappingsByController.get(controller)
            for (UrlMapping urlMapping in controllerUrlMappings) {
                def urlPattern = establishUrlPattern(urlMapping, isAnsiEnabled, longestMapping)
                // urlMapping can be either a string or a closure that returns the map result
                if (urlMapping?.actionName instanceof Map) {
                    urlMapping?.actionName.each { actName ->
                        urlPattern = urlPattern.replace("\${", "{") //replace ${format} with {format}
                        rules.addRule(controller.toString(), actName.value, cleanString(urlPattern), actName.key, grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultFormat)
                    }
                }
                else {
                    String actionName = urlMapping.actionName?.toString() ?: ""
                    rules.addRule(controller.toString(), actionName, cleanString(urlPattern), urlMapping.httpMethod, grailsApplication.mergedConfig.grails.plugins.restapidoc.defaultFormat)
                }
            }
        }
        return rules
    }
    //string from url mapping are dirty (escape char,...)
    public static String cleanString(String dirtyString) {
        Pattern escapeCodePattern = Pattern.compile(
                "\u001B"        // escape code
                        + "\\["
                        + "\\d+"
                        + "(;\\d+)*"
                        + "[@-~]"
                        + "|\\\$" // Url mapping returns parameters formatted like this : ${parameter}.
        );
        escapeCodePattern.matcher(dirtyString).replaceAll("").trim();
    }
}
