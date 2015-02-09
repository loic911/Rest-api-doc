package org.restapidoc

import grails.converters.JSON
import org.jsondoc.core.pojo.ApiBodyObjectDoc
import org.jsondoc.core.pojo.ApiDoc
import org.jsondoc.core.pojo.ApiHeaderDoc
import org.jsondoc.core.pojo.ApiObjectDoc
import org.restapidoc.pojo.*

/**
 * Created by stevben on 16/12/13.
 */
class JSONUtils {

    static def registerMarshallers() {

        JSON.registerObjectMarshaller(ApiObjectDoc) {
            def returnArray = [:]
            println "ApiObjectDoc"
            returnArray['jsondocId'] = it.jsondocId
            returnArray['description'] = it.description
            returnArray['name'] = it.name
            returnArray['fields'] = it.fields
            return returnArray
        }
        JSON.registerObjectMarshaller(RestApiObjectDoc) {
            def returnArray = [:]
            returnArray['jsondocId'] = it.jsondocId
            returnArray['description'] = it.description
            returnArray['name'] = it.name
            returnArray['fields'] = it.fields
            return returnArray
        }

        JSON.registerObjectMarshaller(ApiDoc) {
            def returnArray = [:]
            returnArray['jsondocId'] = it.jsondocId
            returnArray['name'] = it.name
            returnArray['description'] = it.description
            returnArray['methods'] = it.methods
            return returnArray
        }


        JSON.registerObjectMarshaller(RestApiResponseObjectDoc) {
            def returnArray = [:]
            returnArray['jsondocId'] = it.jsondocId
            returnArray['mapKeyObject'] = it.mapKeyObject
            returnArray['mapValueObject'] = it.mapValueObject
            returnArray['object'] = it.object
            return returnArray
        }



        JSON.registerObjectMarshaller(RestApiParamDoc) {
            def returnArray = [:]
            returnArray['jsondocId'] = it.jsondocId
            returnArray['name'] = it.name
            returnArray['description'] = it.description
            returnArray['type'] = it.type
            returnArray['required'] = it.required
            returnArray['allowedvalues'] = it.allowedvalues
            returnArray['format'] = it.format
            return returnArray
        }




        JSON.registerObjectMarshaller(RestApiObjectFieldDoc) {
            def returnArray = [:]
            returnArray['jsondocId'] = it.jsondocId
            returnArray['name'] = it.name
            returnArray['description'] = it.description
            returnArray["type"] = it.type
            returnArray["multiple"] = it.multiple
            returnArray["description"] = it.description
            returnArray["format"] = it.format
            returnArray["allowedvalues"] = it.allowedvalues
            returnArray["mandatory"] = it.mandatory
            returnArray["useForCreation"] = it.useForCreation
            returnArray["defaultValue"] = it.defaultValue
            returnArray["presentInResponse"] = it.presentInResponse
            return returnArray
        }

        JSON.registerObjectMarshaller(ApiHeaderDoc) {
            def returnArray = [:]
            returnArray['jsondocId'] = it.jsondocId
            returnArray['name'] = it.name
            returnArray['description'] = it.description
            return returnArray
        }


        JSON.registerObjectMarshaller(RestApiMethodDoc) {
            def returnArray = [:]
            returnArray['jsondocId'] = it.jsondocId
            returnArray['path'] = it.path
            returnArray['description'] = it.description
            if (it.restVerb == RestApiVerb.GET)
                returnArray['verb'] = "GET"
            if (it.restVerb == RestApiVerb.PUT)
                returnArray['verb'] = "PUT"
            if (it.restVerb == RestApiVerb.POST)
                returnArray['verb'] = "POST"
            if (it.restVerb == RestApiVerb.DELETE)
                returnArray['verb'] = "DELETE"
            if (it.restVerb == RestApiVerb.PATCH)
                returnArray['verb'] = "PATCH"
            if (it.restVerb == RestApiVerb.ALL)
                returnArray['verb'] = "*"
            returnArray['produces'] = it.produces
            returnArray['consumes'] = it.consumes
            returnArray['headers'] = it.headers
            returnArray['pathparameters'] = it.pathparameters
            returnArray['queryparameters'] = it.queryparameters
            returnArray['bodyobject'] = it.bodyobject
            returnArray['response'] = it.response
            returnArray['apierrors'] = it.apierrors
            returnArray['methodName'] = it.methodName
            return returnArray
        }

        JSON.registerObjectMarshaller(RestApiErrorDoc) {
            def returnArray = [:]
            returnArray['jsondocId'] = it.jsondocId
            returnArray['code'] = it.code
            returnArray['description'] = it.description
            return returnArray
        }

        JSON.registerObjectMarshaller(ApiBodyObjectDoc) {
            def returnArray = [:]

            returnArray['jsondocId'] = it.jsondocId
            returnArray['object'] = it.getObject()
            returnArray['multiple'] = it.getMultiple()
            returnArray['mapKeyObject'] = it.getMapKeyObject()
            returnArray['mapValueObject'] = it.getMapValueObject()
            returnArray['map'] = it.getMap()

            return returnArray
        }


    }
}
