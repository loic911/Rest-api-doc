println "Configuring resources for restapidoc"

modules = {
    'restapidoc' {
        defaultBundle 'restapidoc'
        
        resource id: 'restapidoc-bootstrap-responsive-css', url: [plugin: 'restapidoc', dir: 'css', file: 'jsondoc/bootstrap-responsive.min.css']
        resource id: 'restapidoc-bootstrap-css', url: [plugin: 'restapidoc', dir: 'css', file: 'jsondoc/bootstrap.min.css']
        resource id: 'restapidoc-font-awesome-css', url: [plugin: 'restapidoc', dir: 'css', file: 'jsondoc/font-awesome.css']
        resource id: 'restapidoc-css', url: [plugin: 'restapidoc', dir: 'css', file: 'jsondoc/restapidoc.css']

        resource id: 'restapidoc-bootstrap-button-js', url: [plugin: 'restapidoc', dir: 'js', file: 'jsondoc/bootstrap-button.js']
        resource id: 'restapidoc-bootstrap-js', url: [plugin: 'restapidoc', dir: 'js', file: 'jsondoc/bootstrap.min.js']
        resource id: 'restapidoc-handlebars-runtime-js', url: [plugin: 'restapidoc', dir: 'js', file: 'jsondoc/handlebars.runtime-v3.0.0.js']
        resource id: 'restapidoc-jlinq-js', url: [plugin: 'restapidoc', dir: 'js', file: 'jsondoc/jlinq.js']
        resource id: 'restapidoc-prettify-js', url: [plugin: 'restapidoc', dir: 'js', file: 'jsondoc/prettify.js']
        resource id: 'restapidoc-restapidoc-handlebars-js', url: [plugin: 'restapidoc', dir: 'js', file: 'jsondoc/restapidoc-handlebars.js']
        resource id: 'restapidoc-restapidoc-js', url: [plugin: 'restapidoc', dir: 'js', file: 'jsondoc/restapidoc.js']
    }
}