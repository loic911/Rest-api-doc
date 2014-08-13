import grails.converters.JSON

includeTargets << grailsScript('_GrailsPackage')
includeTargets << grailsScript("_GrailsBootstrap")



target(restApiDoc: "Build Rest Api Doc in a JSON file") {
    depends(compile,configureProxy, packageApp, classpath, loadApp, configureApp)

    def apiUtils = classLoader.loadClass('org.restapidoc.APIUtils')
    def jsonUtils = classLoader.loadClass('org.restapidoc.JSONUtils')

    try {
        println("Start build JSON doc...")

       jsonUtils.registerMarshallers ()

        def result = apiUtils.buildApiRegistry(grailsApp)

        File docFile = new File("grails-app/conf/"+grailsApp.mergedConfig.grails.plugins.restapidoc.outputFile)

        println("Write in file " + docFile.absolutePath+"...")

        docFile.write((result as JSON).toString(true))
    } catch(Exception e) {
        println e
        e.printStackTrace()
    }

}

setDefaultTarget(restApiDoc)