<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>RestApiDoc</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <meta name="layout" content="${layout}" >
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>

        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    </head>
    <body>
        <div class="navbar navbar-fixed-top navbar-inverse">
            <div class="navbar-inner">
                <div class="container-fluid">
                    <a class="brand" href="#">RestApiDoc</a>
                    <form class="navbar-form pull-left">
                        <input id="jsondocfetch" class="span5" type="text" placeholder="Insert here the RestApiDoc URL" autocomplete="off" />
                        <button id="getDocButton" class="btn">Get documentation</button>
                    </form>
                </div>
            </div>
        </div>

        <div class="container-fluid">
            <div class="row-fluid">

                <div class="span3">
                    <div id="maindiv" style="display:none;"></div>
                    <div class="well sidebar-nav" id="apidiv" style="display:none;"></div>
                    <div class="well sidebar-nav" id="objectdiv" style="display:none;"></div>
                </div>

                <div class="span5">
                    <div id="content"></div>
                </div>

                <div class="span4">
                    <div id="testContent"></div>
                </div>
            </div>
        </div>
    </body>
</html>