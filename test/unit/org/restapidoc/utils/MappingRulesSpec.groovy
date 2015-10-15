package org.restapidoc.utils

import org.restapidoc.pojo.RestApiParamDoc
import spock.lang.Specification


/**
 * Created by lguerin on 04/06/15.
 */
class MappingRulesSpec extends Specification {

    MappingRules mappingRules

    def setup() {
        mappingRules = new MappingRules()
    }

    def "Camel upper case controller name"() {
        expect:
        expected == mappingRules.camelUpperCase(controller)

        where:
        controller            | expected
        "BookController"      | "bookController"
        "MyBookController"    | "myBookController"
        ""                    | ""
    }

    def "Does path match all params"() {
        expect:
        match == mappingRules.matchAllParams(path, params)

        where:
        match | params                  | path
        true  | ["bookId"]              | "/api/custom/{bookId}/{action}"
        false | ["bookId", "authorId"]  | "/api/custom/{bookId}/{action}"
        true  | ["bookId", "authorId"]  | "/api/custom/{bookId}/by/{authorId}/{action}"
        false | ["bookId"]              | "/api/custom/{bookId}/by/{authorId}/{action}"
        true  | ["bookId", "authorId"]  | "/api/custom/{bookId}/by/{authorId}/{action}.{format}"
    }

    def "Match rules by action name"() {
        given:
        mappingRules.addRule "bookController", "save", "/api/book.{format}", "POST", "json"
        mappingRules.addRule "bookController", "show", "/api/book/{id}.{format}", "GET", "json"
        mappingRules.addRule "bookController", "update", "/api/book/{id}.{format}", "PUT", "json"
        mappingRules.addRule "bookController", "delete", "/api/book/{id}.{format}", "DELETE", "json"
        mappingRules.addRule "bookController", "listByAuthor", "/api/author/{id}/book.{format}", "GET", "json"

        expect:
        path == mappingRules.matchRule(controller, action, "json")?.getPath()

        where:
        controller                  | action          | path
        "bookController"            | "show"          | "/api/book/{id}.{format}"
        "bookController"            | "listByAuthor"  | "/api/author/{id}/book.{format}"
        "bookController"            | "XXX"           | null
        "XXX"                       | ""              | null
    }

    def "Match rules by URL params"() {
        given:
        mappingRules.addRule "restCustomController", "", "/api/custom/{bookId}/chapter/{chapterId2}/{action}.{format}", "*", "json"
        mappingRules.addRule "restCustomController", "", "/api/custom/{bookId}/{action}.{format}", "*", "json"
        mappingRules.addRule "restCustomController", "", "/api/custom/{bookId}/{action}.{format}", "DELETE", "json"
        List<RestApiParamDoc> urlParams = []
        urlParams.add(new RestApiParamDoc(name: "bookId"))

        when:
        def match = mappingRules.matchRule("restCustomController", "", urlParams, "json")

        then:
        match != null
        match.path == "/api/custom/{bookId}/{action}.{format}"

        when: 'We have duplicate paths for the same controller with a different verb'
        mappingRules.addRule "restCustomController", "", "/api/custom/{bookId}/{action}.{format}", "PUT",  "json"
        match = mappingRules.matchRule("restCustomController", "", urlParams, "json")

        then: 'Get the first path who match'
        match != null
        match.path == "/api/custom/{bookId}/{action}.{format}"

        when: 'We have many paths that match given params for the same controller but with a different format'
        mappingRules.addRule "restCustomController", "", "/api/custom/xml/{bookId}/{action}.{format}", "PUT",  "xml"
        match = mappingRules.matchRule("restCustomController", "", urlParams, "xml")

        then: 'Get the path whith the right format'
        match != null
        match.path == "/api/custom/xml/{bookId}/{action}.{format}"
    }
}