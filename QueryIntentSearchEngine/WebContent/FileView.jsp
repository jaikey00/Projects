<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
	
	<title>Search Engine Web Client</title>
  </head>
  <body onload="showAlert()">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    	<a class="navbar-brand" href="#"></a>
        <button class="navbar-toggler " type="button" data-toggle="collapse" data-target="#collapsibleNavId" aria-controls="collapsibleNavId"aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="collapsibleNavId">
            <ul style="font-size:35px;" class="navbar-nav mr-auto mt-2 mt-lg-0">
                <li class="nav-item mr-2 ">
                    <a class="nav-link" href="Search.jsp">Search<span class="sr-only">(current)</span></a>
                </li>
            </ul>
        </div>
    </nav>

    <div>
        <div class="jumbotron">
            <h1 class="display-3"><% out.write((String)session.getAttribute("fileName"));%> file view</h1>
            <hr class="my-2">
            <div style="padding-left:50px;padding-right:50px;padding-top:15px;">
                <div style="margin-top:2%;">
					<pre><% out.write((String)session.getAttribute("fileContent")); %></pre>
                </div>
            </div>
        </div>
    </div>

    <!-- JS stuff -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
	<script>$(".custom-file-input").on("change", function() {var fileName = $(this).val().split("\\").pop();$(this).siblings(".custom-file-label").addClass("selected").html(fileName);});</script>
  
  </body>
</html>