<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@page import="file.FileObject"%>
<%@page import="client.Client"%>
<%@page import="client.Server"%>
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
        <button class="navbar-toggler " type="button" data-toggle="collapse" data-target="#collapsibleNavId" aria-controls="collapsibleNavId"aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="collapsibleNavId">
            <ul style="font-size:35px;" class="navbar-nav mr-auto mt-2 mt-lg-0">
                <li style="color:white;" class="nav-item mr-2">
                    User Login
                </li>
            </ul>
        </div>
    </nav>
    <div style="padding:25px;">
        <form action="CreateAccount" method="POST">
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <input type="text" class="form-control" name="username" placeholder="Username" required>
                </div>
            </div>
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <input type="password" class="form-control" name="password" placeholder="Password" required>
                </div>
            </div>
            <input type="submit" class="btn btn-success" name="loginBtn" value="Create Account">
        </form>
    </div>
    <%
    	Boolean invalidCreateAccount = (Boolean)session.getAttribute("invalidCreateAccount");
		
		if(invalidCreateAccount != null){
			if(invalidCreateAccount){
				%><script>function showAlert(){alert("<%out.write("Create Account Failed: Account already exists.");%>");}</script><%
			}else{
				%><script>function showAlert(){}</script><%
			}
		}else{
			%><script>function showAlert(){}</script><%
		}
    %>
    <!-- JS stuff -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
  </body>
</html>