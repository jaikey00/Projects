<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="client.Client"%>
<%@page import="client.Server"%>
<%
Client client = null;
if(Server.isConnected(session.getId())){
	client = Server.getClient(session.getId());
}else{
	response.sendRedirect(request.getContextPath() + "/Login.jsp");
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

    <title>File System Web Client</title>
  </head>
  <body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    	<a class="navbar-brand" href="#"><%out.write(client.getUser());%></a>
        <button class="navbar-toggler " type="button" data-toggle="collapse" data-target="#collapsibleNavId" aria-controls="collapsibleNavId"aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="collapsibleNavId">
            <ul style="font-size:35px;" class="navbar-nav mr-auto mt-2 mt-lg-0">
                <li class="nav-item mr-2 active">
                    <a class="nav-link" href="Home.jsp">Home<span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item mr-2 ">
                    <a class="nav-link" href="ControlPanel.jsp">Control Panel</a>
                </li>
            </ul>
        </div>
    </nav>
    <div class="jumbotron">
        <h1 class="display-3">Welcome to the File System Client!</h1>
        <p class="lead">In this system, you can view all files and execute commands from the "Control Panel" page!</p>
        <br />
        <p class="lead">Additionally, you can use the "Search" page to search across the system for specific files or file types!</p>
    </div>
    <!-- JS stuff -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
  </body>
</html>