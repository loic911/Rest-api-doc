class RestApiDocGrailsPlugin {
    // the plugin version
    def version = "0.6"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.3 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "grails-app/domain/org/restapidoc/test/TestObject.groovy",
        "grails-app/controllers/org/restapidoc/test/TestController.groovy",
        "grails-app/controllers/org/restapidoc/test/AnotherTestController.groovy"
    ]





    // TODO Fill in these fields
    def title = "RestApiDoc Plugin" // Headline display name of the plugin
    def author = "Loïc Rollus"
    def authorEmail = "loicrollus@gmail.com"
    def description = '''\
The RestApiDoc plugin allows to document your Grails Rest API. Thanks to some Annotations (@), you will be ready to build a full API report (with a playground to perform test request). The plugin is based on jsondoc.

This plugin allows you to document methods (description, HTTP path/verb, parameters, response type...) and resources (description, all fields,...). It does a lot of stuff for you (computing path/verb for the method, retrieving field type,...)
'''

    // URL to the plugin's documentation
    def documentation = "http://loic911.github.io/restapidoc/"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
    def developers = [ [ name: "Benjamin Stevens", email: "b.stevens@ulg.ac.be" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GITHUB", url: "https://github.com/loic911/restapidoc/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/loic911/restapidoc" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
