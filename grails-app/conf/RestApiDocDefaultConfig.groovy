import org.springframework.http.MediaType

grails {
    plugins{
        restapidoc {

            docVersion = "0.1"

            basePath = "Fill with basePath config" //"http://localhost:8080/RestApiDoc" //app.name

            outputFile = "restapidoc.json"
            customClassName = null  //"org.restapidoc.CustomResponseDoc"

            defaultFormat = "json"

            defaultResponseType = MediaType.APPLICATION_JSON_VALUE
            controllerPrefix = ""
            controllerSuffix = "Controller"

            defaultObjectFields = [ ]

            defaultErrorAll = [
                    "400": "Bad Request: missing parameters or bad message format",
                    "401": "Unauthorized: must be auth",
                    "403": "Forbidden: role error",
                    "404": "Object not found"
            ]

            defaultErrorGet = [
                    "400": "Bad Request: missing parameters or bad message format",
                    "401": "Unauthorized: must be auth",
                    "403": "Forbidden: role error",
                    "404": "Object not found"
            ]

            defaultErrorPost = [
                    "409": "Object already exist"
            ]

            defaultErrorPut = [
                    "409": "Object already exist"
            ]

            defaultErrorDelete = [:]


            verbPerMethodPrefix = [
                    "show" : "GET",
                    "list" : "GET",
                    "save" : "POST",
                    "add" : "POST",
                    "update" : "PUT",
                    "edit" : "PUT",
                    "delete" : "DELETE",
                    "remove" : "DELETE"
            ]

            pathPerMethodPrefix =[
                    "show" : "show/{id}",
                    "list" : "list",
                    "add" : "add",
                    "update" : "udpate/{id}",
                    "delete" : "delete/{id}"
            ]

            grailsDomainDefaultType = null //= null will appear 'domain' type


            defaultParamsQueryAll = []
            defaultParamsQuerySingle = []
            defaultParamsQueryMultiple = [
                    //                [name:"max",description:"Pagination: Number of record per page (default 0 = no pagination)",type:"int"],
                    //                [name:"offset",description:"Pagination: Offset of first record (default 0 = first record)",type:"int"]
            ]
        }
    }
}