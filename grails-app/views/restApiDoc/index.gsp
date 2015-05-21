<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>RestApiDoc</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <meta name="layout" content="${layout}">
    </head>
    <body>
        <div class="rest-api-content">
            <div class="navbar navbar-fixed-top navbar-inverse">
                <div class="container-fluid">
                    <div class="navbar-header"><a class="navbar-brand" href="#">RestApiDoc</a></div>
                    <form class="navbar-form navbar-left">
                        <input id="jsondocfetch" class="form-control" type="text" placeholder="Insert here the RestApiDoc URL" autocomplete="off">
                        <button id="getDocButton" type="button" class="btn btn-default">Get documentation</button>
                    </form>
                </div>
            </div>
    
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-3 col-md-3">
                        <div id="maindiv" style="display:none;"></div>
                        <div id="apidiv" style="display:none;"></div>
                        <div id="objectdiv" style="display:none;"></div>
                    </div>
    
                    <div class="col-sm-5 col-md-5">
                        <div id="content"></div>
                    </div>
    
                    <div class="col-sm-4 col-md-4">
                        <div id="testContent"></div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>