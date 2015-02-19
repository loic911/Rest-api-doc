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

        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
              <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
            <![endif]-->
    </head>
    <body>
        <div class="navbar navbar-fixed-top navbar-inverse">
            <div class="container">
                <div class="container-fluid">
                    <div class="navbar-header"><a class="navbar-brand" href="#">RestApiDoc</a></div>
                    <form class="navbar-form pull-left">
                        <input id="jsondocfetch" class="span5 form-control" type="text" placeholder="Insert here the RestApiDoc URL" autocomplete="off">
                        <button id="getDocButton" class="btn btn-default">Get documentation</button>
                    </form>
                </div>
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
    </body>
</html>