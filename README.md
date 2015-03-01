RestApiDoc
==========

Full doc: http://loic911.github.io/restapidoc/

#Plugin Configuration

##Including the Grails plugin

The plugin depends on both jQuery and Bootstrap, so include them as runtime dependencies:

```groovy
plugins {
  compile ":rest-api-doc:0.6.3"
  runtime ":jquery:1.11.1"
  runtime ':twitter-bootstrap:3.3.2.1'
}
```

##Config.groovy options

At minimum, you need to set the basePath option for the plugin, which often is the Grails URL of the project using the plugin:

```groovy
grails.plugins.restapidoc.basePath = "http://localhost:8080/<projectname>"
```

You also might need to set options for Bootstrap and for jQuery in order for them to work. This depends on your project configuration:
alternative options are available and it is suggested that you read the documentation for both plugins to find out what best suits
your needs. The following options enable a simple project to use the restapidoc plugin out of the box:

```groovy
grails.plugins.twitterbootstrap.defaultBundle = false
grails.views.javascript.library="jquery"
```

##Sitemesh layout selection
You can use an existing layout for the restapidoc pages, or create a separate one. The RestApiDoc plugin will use the `grails-app/views/layouts/main.gsp`
layout. If you wish to use another layout, you can configure which layout to use by setting the `grails.plugins.restapidoc.layout` option.
Again, the included options for jQuery are one of several alternatives, and it is suggested that you read the jQuery plugin documentation
to see which option is more appropriate for your application

`grails-app/conf/Config.groovy`
```groovy
grails.plugins.restapidoc.layout = "restapidoc_layout"
```

##Including the resources when using the Asset Pipeline
To include your bootstrap resources add the following to your application's css
or js file.

Javascript `grails-app/assets/javascripts/application.js`:
```javascript
//= require jquery
//= require bootstrap
//= require restapidoc
//= require_tree .
//= require_self
```

Stylesheet `grails-app/assets/javascripts/application.css`:
```css
/*
*= require bootstrap
*= require restapidoc
*= require_self
*/
```

###Edit your Sitemesh layout
`grails-app/views/layouts/<layout>.gsp`

```gsp
<!DOCTYPE html>
    <head>
        <title><g:layoutTitle/></title>
        <asset:javascript src="application.js"/>
        <asset:stylesheet href="application.css"/>
        <g:layoutHead/>
    </head>
    <body>
        <g:layoutBody/>
        <asset:javascript src="restapidoc/restapidoc.js"/>
    </body>
</html>
```

####Supporting Internet Explorer
If you wish to support Internet Explorer lower than version 9, then you need to add the following just above `<g:layoutHead>`:
```gsp
<!--[if lt IE 9]>
    <asset:javascript src="html5shiv/html5shiv.js"/>
<![endif]-->
```

##Including the resources when using the Resources plugin
###Edit your Sitemesh layout
`grails-app/views/layouts/<layout>.gsp`

```gsp
<!DOCTYPE html>
    <html lang="en">
    <head>
        <title><g:layoutTitle/></title>
        <r:require modules="bootstrap, restapidoc"/>
        <g:layoutHead/>
        <g:javascript library="jquery" plugin="jquery"/>
        <g:javascript library="application"/>
        <r:layoutResources />
    </head>
    <body>
        <g:layoutBody/>
        <r:layoutResources/>
    </body>
</html>
```

####Supporting Internet Explorer
If you wish to support Internet Explorer lower than version 9, then your `<r:requireModules>` line should look like this:

```gsp
<r:require modules="restapidoc_html5shiv, bootstrap, restapidoc"/>
```

#Logging
You can enable logging for the restapidoc plugin by adding the following line to `grails-app/conf/Config.groovy`:

```groovy
log4j.main = {
    info 'org.restapidoc'
}
```

#Known issues
If resource debugging is enabled in any of the environments, then when running the application on that environment restapidoc will be unable to find the
Resource Plugin module assets. Disable resource debugging in `grails-app/conf/Config.groovy` (by default on the development environment) in order to use the plugin:

```groovy
environments {
    development {
        grails.resources.debug = false
    }
}
```