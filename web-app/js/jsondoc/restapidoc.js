var model;

function checkURLExistence() {
    var value = $("#jsondocfetch").val();
    if(value.trim() == '') {
        alert("Please insert a valid URL");
        return false;
    } else {
        return fetchdoc(value);
    }
}

function updateMethodBodyForm() {
    var methodBodyType = $("#bodyTypeSelect").val();
    if (methodBodyType == "String body"){
        $("#methodMultipartBody").hide();
        $("#methodStringBody").show();
    } else if (methodBodyType == "Multipart body"){
        $("#methodStringBody").hide();
        $("#methodMultipartBody").show();
    }
}

$("#jsondocfetch").keypress(function(event) {
    if (event.which == 13) {
        checkURLExistence();
        return false;
    }
});

$("#getDocButton").click(function() {
    checkURLExistence();
    return false;
});

function printResponse(data, res, url) {
    if(res.responseXML != null) {
        $("#response").text(formatXML(res.responseText));
    } else {
        $("#response").text(JSON.stringify(data, undefined, 2));
    }

    prettyPrint();
    $("#responseStatus").text(res.status);
    $("#responseHeaders").text(res.getAllResponseHeaders());
    $("#requestURL").text(url);
    $('#testButton').button('reset');
    $("#resInfo").show();
}

function formatXML(xml) {
    var formatted = '';
    var reg = /(>)(<)(\/*)/g;
    xml = xml.replace(reg, '$1\r\n$2$3');
    var pad = 0;
    jQuery.each(xml.split('\r\n'), function(index, node) {
        var indent = 0;
        if (node.match( /.+<\/\w[^>]*>$/ )) {
            indent = 0;
        } else if (node.match( /^<\/\w/ )) {
            if (pad != 0) {
                pad -= 1;
            }
        } else if (node.match( /^<\w[^>]*[^\/]>.*$/ )) {
            indent = 1;
        } else {
            indent = 0;
        }

        var padding = '';
        for (var i = 0; i < pad; i++) {
            padding += '  ';
        }

        formatted += padding + node + '\r\n';
        pad += indent;
    });

    return formatted;
}

function fetchdoc(jsondocurl) {
    $.ajax({
        url : jsondocurl,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        success : function(data) {
            model = data;
            // var main = Handlebars.compile($("#main").html());
            var main = Handlebars.templates['main'];
            var mainHTML = main(data);
            $("#maindiv").html(mainHTML);
            $("#maindiv").show();

            // var apis = Handlebars.compile($("#apis").html());
            var apis = Handlebars.templates['apis'];
            var apisHTML = apis(data);
            $("#apidiv").html(apisHTML);
            $("#apidiv").show();

            $("#apidiv a").each(function() {
                $(this).click(function() {
                    var api = jlinq.from(data.apis).equals("jsondocId", this.id).first();
                    // var methods = Handlebars.compile($("#methods").html());
                    var methods = Handlebars.templates['methods'];
                    var methodsHTML = methods(api);
                    $("#content").html(methodsHTML);
                    $("#content").show();
                    $("#apiName").text(api.name);
                    $("#apiDescription").text(api.description);
                    $("#testContent").hide();

                    $('#content a[rel="method"]').each(function() {
                        $(this).click(function() {
                            var method = jlinq.from(api.methods).equals("jsondocId", this.id).first();
                            // var test = Handlebars.compile($("#test").html());
                            var test = Handlebars.templates['test'];
                            var testHTML = test(method);
                            $("#testContent").html(testHTML);
                            $("#testContent").show();

                            $("#produces input:first").attr("checked", "checked");
                            $("#consumes input:first").attr("checked", "checked");

                            $("#testButton").click(function() {
                                var headers = new Object();
                                $("#headers input").each(function() {
                                    headers[this.name] = $(this).val();
                                });

                                headers["Accept"] = $("#produces input:checked").val();

                                var replacedPath = method.path;
                                var tempReplacedPath = replacedPath; // this is to handle more than one parameter on the url
                                $("#pathparameters input").each(function() {
                                    tempReplacedPath = replacedPath.replace("{"+this.name+"}", $(this).val());
                                    replacedPath = tempReplacedPath;
                                });

                                replacedPath = replacedPath + "?";
                                $("#queryparameters input").each(function() {
                                    if($(this).val() != null && $(this).val().toString().trim() != ""){
                                        replacedPath = replacedPath + "&" + this.name + "=" + $(this).val();
                                    }
                                });

                                $('#testButton').button('loading');

                                var requestData;
                                var cType;
                                var isProcessData;
                                if ($("#bodyTypeSelect").val() == "Multipart body"){
                                    cType = false;
                                    isProcessData = false;
                                    requestData = new FormData(document.forms.namedItem("multipartFilesForm"));
                                } else {
                                    cType = $("#consumes input:checked").val();
                                    isProcessData = true;
                                    requestData = $("#inputJson").val();
                                }

                                var res = $.ajax({
                                    url : model.basePath + replacedPath,
                                    type: method.verb,
                                    data: requestData,
                                    headers: headers,
                                    processData: isProcessData,
                                    contentType: cType,
                                    success : function(data) {
                                        printResponse(data, res, this.url);

                                    },
                                    error: function(data) {
                                        printResponse(data, res, this.url);
                                    }
                                });

                            });

                        });
                    });
                });
            });
            var allLink = $("#apidiv").find("ul.nav").find("a");
            var lastLink = allLink[allLink.length-1];
            lastLink.click();
            // var objects = Handlebars.compile($("#objects").html());
            var objects = Handlebars.templates['objects'];
            var objectsHTML = objects(data);
            $("#objectdiv").html(objectsHTML);
            $("#objectdiv").show();

            $("#objectdiv a").each(function() {
                $(this).click(function() {
                    var o = jlinq.from(data.objects).equals("jsondocId", this.id).first();
                    // var object = Handlebars.compile($("#object").html());
                    var object = Handlebars.templates['object'];
                    var objectHTML = object(o);
                    $("#content").html(objectHTML);
                    $("#content").show();

                    $("#testContent").hide();
                });
            });

        },
        error: function(msg) {
            alert("Error " + msg);
        }
    });
}
$(document).ready(function() {

    var parseQueryString = function() {
        var vars = [], hash;
        var q = document.URL.split('?')[1];
        if(q != undefined){
            q = q.split('&');
            for(var i = 0; i < q.length; i++){
                hash = q[i].split('=');
                vars.push(hash[1]);
                vars[hash[0]] = hash[1];
            }
        }
        return vars;
    }

    var parameters = parseQueryString();
    if (parameters['doc_url']) {
        $('#jsondocfetch').attr('value', parameters['doc_url']);
        $('#getDocButton').click();
    }

});