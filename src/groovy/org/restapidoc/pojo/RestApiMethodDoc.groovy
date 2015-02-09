package org.restapidoc.pojo

import org.jsondoc.core.pojo.ApiHeaderDoc
import org.jsondoc.core.pojo.ApiMethodDoc
import org.restapidoc.annotation.RestApiHeader
import org.restapidoc.annotation.RestApiHeaders
import org.restapidoc.annotation.RestApiMethod

/**
 * RestApiMethodDoc must be used instead of ApiMethodDoc to use a light rest api doc
 * @author Loïc Rollus
 *
 */
public class RestApiMethodDoc extends ApiMethodDoc {

    private RestApiVerb restVerb

    public final static String UNDEFINED = "Undefined"

    String methodName = null

    /**
     * Build a method doc from annotation
     * @param annotation Method annotation
     * @param path HTTP REST path to the method
     * @param verb HTTP verb for the method path
     * @return A method doc object
     */
    public
    static RestApiMethodDoc buildFromAnnotation(RestApiMethod annotation, String path, String verb, String defaultCons) {
        RestApiMethodDoc apiMethodDoc = new RestApiMethodDoc();

        def objVerb = retrieveVerb(verb)
        String newPath = path.trim()

        if (!annotation.path().equals(UNDEFINED)) {
            //path has been overrided in urlmapping
            newPath = annotation.path()
            objVerb = annotation.verb()
        }

        def prod = Arrays.asList(annotation.produces())
        def cons = Arrays.asList(annotation.consumes())

        if (cons.isEmpty() && (objVerb == RestApiVerb.POST || objVerb == RestApiVerb.PUT || objVerb == RestApiVerb.PATCH)) {
            //if no cons definition and POST/PUT method => auto put json
            cons = [defaultCons]
        }
        if (!cons.isEmpty() && (cons.first() == null || cons.first().equals(""))) {
            //if force set cons to null/empty string, no cons definition
            cons = []
        }

        if (prod.isEmpty()) {
            //if no cons definition => auto put json for all verb
            prod = [defaultCons]
        }
        if (!prod.isEmpty() && (prod.first() == null || prod.first().equals(""))) {
            //if force set cons to null/empty string, no cons definition
            prod = []
        }

        apiMethodDoc.setPath(newPath);
        apiMethodDoc.setDescription(annotation.description());
        apiMethodDoc.restVerb = objVerb;
        apiMethodDoc.setProduces(prod);
        apiMethodDoc.setConsumes(cons);

        return apiMethodDoc;
    }

    public static List<ApiHeaderDoc> buildFromAnnotation(RestApiHeaders annotation) {
        List<ApiHeaderDoc> docs = new ArrayList<ApiHeaderDoc>();
        for (RestApiHeader restApiHeader : annotation.headers()) {
            docs.add(new ApiHeaderDoc(restApiHeader.name(), restApiHeader.description()));
        }
        return docs;
    }
	
    public static RestApiVerb retrieveVerb(String verb) {
        String verbUpper = verb.toUpperCase()
        if(verbUpper && verbUpper.trim().equals("*")) {
            return RestApiVerb.ALL
        }
        return RestApiVerb.valueOf(verb.toUpperCase())
    }
}
