package org.restapidoc.utils

import groovy.util.logging.Log
import org.restapidoc.pojo.RestApiParamDoc

/**
 * Created by lrollus on 1/10/14.
 */
@Log
class MappingRules {

    Map<String, List<MappingRulesEntry>> rules = new TreeMap<String, MappingRulesEntry>()

    public void addRule(String controllerName, String actionName, String path, String verb, String defaultFormat = "") {
        def entries = rules.get(controllerName) ?: new ArrayList<MappingRulesEntry>()
        entries.push(new MappingRulesEntry(path: path, verb: verb, action: actionName, format: defaultFormat))
        rules.put(controllerName, entries)
    }

    /**
     * Match a mapping rule from grails UrlMappings
     * @param controller    Controller name to look at
     * @param action        Controller action to match
     * @param urlParams     Paths params as declared in UrlMappings and constraints
     * @param format        Output format (json, xml, ...)
     * @return
     */
    MappingRulesEntry matchRule(String controller, String action, List<RestApiParamDoc> urlParams = [], String format) {
        MappingRulesEntry matchRule
        String controllerName = camelUpperCase(controller)
        log.info("controller: $controllerName / action: $action")
        List<MappingRulesEntry> entries = rules.get(controllerName)
        if (!entries) return

        // Find by action
        matchRule = matchByAction(controllerName, action)
        if (matchRule) {
            println "Matched rule by action: " + matchRule
            return matchRule
        }
        else {
            // Find by URL params
            matchRule = matchByURLParams(controllerName, urlParams, format)
            println "Matched rule by URL params: " + matchRule
        }
        return matchRule
    }

    /**
     * Macth mapping rule by controller action
     * @param controllerName
     * @param action
     * @return
     */
    private MappingRulesEntry matchByAction(String controllerName, String action) {
        if (!action) return null
        return rules.get(controllerName).find { it.action == action }
    }

    /**
     * Match mapping rule by path params
     * @param controllerName
     * @param urlParams
     * @param format
     * @return
     */
    private MappingRulesEntry matchByURLParams(String controllerName, List<RestApiParamDoc> urlParams, String format = "") {
        if (urlParams) {
            def params = urlParams.collect { it.name }
            def matches = rules.get(controllerName)?.findAll { matchAllParams(it.path, params) }
            if (matches) {
                if (matches.size() > 1) {
                    // Find out if format has been set
                    matches = matches.findAll { it.format == format }
                }
                return matches[0]
            }
            return null
        }
    }

    protected String camelUpperCase(String stringToSplit) {
        if (!stringToSplit) return ""
        String result = stringToSplit[0].toLowerCase()
        for (int i = 1; i < stringToSplit.size(); i++) {
            def car = stringToSplit[i]
            if (car != car.toUpperCase()) {
                result = result + car
            } else {
                result = result + car.toUpperCase()
            }
        }

        return result.trim()
    }

    /**
     * Check if rule path match all given params.
     * Reserved tokens like {action} or {format} are filtered.
     *
     * @param path
     * @param params
     * @return
     */
    protected boolean matchAllParams(String path, List<String> params) {
        def filtered = ['action', 'format']
        def pattern = /\{\w*}/
        def pathParams = (path =~ pattern).findAll { !(param(it) in filtered) }.collect { param(it) }
        return params.sort() == pathParams.sort()
    }

    private String param(String tokenParam) {
        String param = tokenParam.replaceFirst(/\{/, '')
        param = param.replaceFirst(/}/, '')
        return param
    }

    @Override
    public String toString() {
        return "MappingRules{" +
                "rules=" + rules +
                '}';
    }
}

class MappingRulesEntry {

    String path
    String verb
    String action
    String format

    public String toString() {
        return "path = $path , verb = $verb , action = $action , format = $format"
    }
}
