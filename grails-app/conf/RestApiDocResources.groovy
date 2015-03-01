println "Configuring resources for restapidoc"

modules = {
    'restapidoc_html5shiv' {
        defaultBundle 'restapidoc_html5shiv'
        resource id: 'restapidoc_html5shiv-html5shiv-js', url: [plugin: 'restapidoc', dir: 'js', file: 'html5shiv/html5shiv.js'],
                wrapper: { s -> "<!--[if lt IE 9]>$s<![endif]-->" }, disposition: 'head'
    }
    'restapidoc' {
        defaultBundle 'restapidoc'
        dependsOn 'jquery, bootstrap'

        resource id: 'restapidoc-css', url: [plugin: 'restapidoc', dir: 'css', file: 'restapidoc/restapidoc.css']

        resource id: 'restapidoc-handlebars-runtime-js', url: [plugin: 'restapidoc', dir: 'js', file: 'restapidoc/handlebars.runtime-v3.0.0.js']
        resource id: 'restapidoc-jlinq-js', url: [plugin: 'restapidoc', dir: 'js', file: 'restapidoc/jlinq.js']
        resource id: 'restapidoc-prettify-js', url: [plugin: 'restapidoc', dir: 'js', file: 'restapidoc/prettify.js']
        resource id: 'restapidoc-restapidoc-handlebars-js', url: [plugin: 'restapidoc', dir: 'js', file: 'restapidoc/restapidoc-handlebars.js']
        resource id: 'restapidoc-restapidoc-js', url: [plugin: 'restapidoc', dir: 'js', file: 'restapidoc/restapidoc.js']
    }
}