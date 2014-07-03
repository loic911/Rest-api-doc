package org.restapidoc.pojo

import org.restapidoc.annotation.RestApiError
import org.restapidoc.annotation.RestApiErrors

/**
 * Created by lrollus on 4/10/14.
 */
public class RestApiErrorDoc {
    public String jsondocId = UUID.randomUUID().toString();
    private String code;
    private String description;

    public static List<RestApiErrorDoc> buildFromAnnotation(RestApiErrors annotation) {
        List<RestApiErrorDoc> apiMethodDocs = new ArrayList<RestApiErrorDoc>();
        for (RestApiError apiError : annotation.apierrors()) {
            apiMethodDocs.add(new RestApiErrorDoc(apiError.code(), apiError.description()));
        }
        return apiMethodDocs;
    }

    public RestApiErrorDoc(String code, String description) {
        this.code = code
        this.description = description
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}