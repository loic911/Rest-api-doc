RestApiDoc
==========

Full doc: http://loic911.github.io/restapidoc/

#Plugin Configuration

##Including the Grails plugin

```groovy
plugins {
  ...
  compile ":rest-api-doc:0.6.2"
  ...
}
```

##Including the resources when using the Asset Pipeline
To include your bootstrap resources add the following to your application's css
or js file.

Javascript `grails-app/assets/javascripts/application.js`:
```javascript
//= require restapidoc
```

Stylesheet `grails-app/assets/javascripts/application.css`:
```css
/*
*= require restapidoc
*/
```

##Including the resources when using the Resources plugin
### Edit your Sitemesh layout
You can use an existing layout for the restapidoc pages, or create a separate one. The RestApiDoc plugin will use the main.gsp
layout. If you wish to use another layout, you can configure which layout to use by setting grails.plugins.restapidoc.layout
in `grails-app/conf/Config.groovy`
`grails-app/views/layouts/<layout>.gsp`:
```gsp
<!DOCTYPE html>
<html lang="en">
<head>
    <g:layoutTitle/>
    <r:require modules="restapidoc"/>
    <r:layoutResources/>
</head>
<body>
    <g:layoutBody/>
    <r:layoutResources/>
</body>
</html>
```

#Known issues
if debug is set to true in any of the environments, then when running that environment using run-app, restapidoc will be unable to find the
Resource Plugin module assets.