package org.restapidoc.pojo

import org.jsondoc.core.pojo.ApiDoc
import org.restapidoc.annotation.RestApi

public class RestApiDoc extends ApiDoc {

    public static RestApiDoc buildFromAnnotation(RestApi api) {
        RestApiDoc apiDoc = new RestApiDoc();
        apiDoc.setDescription(api.description());
        apiDoc.setName(api.name());
        return apiDoc;
    }

}