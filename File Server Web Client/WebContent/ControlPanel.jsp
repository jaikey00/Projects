<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@page import="file.FileObject"%>
<%@page import="client.Client"%>
<%@page import="client.Server"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
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

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
	
	<title>File System Web Client</title>
  </head>
  <body onload="showAlert()">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    	<a class="navbar-brand" href="#"><%out.write(client.getUser());%></a>
        <button class="navbar-toggler " type="button" data-toggle="collapse" data-target="#collapsibleNavId" aria-controls="collapsibleNavId"aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="collapsibleNavId">
            <ul style="font-size:35px;" class="navbar-nav mr-auto mt-2 mt-lg-0">
                <li class="nav-item mr-2 ">
                    <a class="nav-link" href="Home.jsp">Home<span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item mr-2 active">
                    <a class="nav-link" href="ControlPanel.jsp">Control Panel</a>
                </li>
            </ul>
        </div>
    </nav>

    <div>
        <div class="jumbotron">
            <h1 class="display-3">Upload a file to the server.</h1>
            <hr class="my-2">
            <div class="input-group">
            <form action="UploadFile" method="POST" enctype="multipart/form-data">
			  <div class="custom-file">
				    <input type="file" class="custom-file-input" name="fileUploadField">
				    <label class="custom-file-label" for="fileUploadField">Choose file</label>
			  </div>
            <input type="submit" class="btn btn-success btn-lg mt-2" value="Upload">
            </form>
            </div>
        </div>
        <div class="jumbotron">
            <h1 class="display-3">Download or Remove a file from the server.</h1>
            <p class="lead">Choose file</p>
            <hr class="my-2">
            <%
            	Boolean searchQueried = (Boolean)session.getAttribute("fileSearchQueried");
            	String searchQuery = (String)session.getAttribute("searchQuery");
            	String searchResult = (String)session.getAttribute("fileSearchResult");
            	List<String> files = searchQueried != null && searchQueried ? Arrays.asList(searchResult.split(",")) : client.getAllFiles(client.getUser());
            	if(searchQueried != null && searchQueried && searchQuery.length() == 0 && searchResult.length() == 0){
            		files = client.getAllFiles(client.getUser());
            	}
            %>
            <div style="padding-left:50px;padding-right:50px;padding-top:15px;">
                <h1>Files</h1>
                <form action="SearchFile" method="post">
		            <div class="input-group md-form form-sm form-2 pl-0">
		                <input name="searchBarMain" style="height:48px;" class="form-control my-0 py-1" type="text" placeholder="Search" value="<%out.write(searchQueried != null && searchQueried?searchQuery:"");%>" aria-describedby="searchHelp">
		                <div class="input-group-append">
		                    <input type="submit" name="submit" class="btn btn-primary btn-lg" value="Search"></input>
		                </div>
		            </div>
		        </form>
		        <%
		        	session.setAttribute("fileSearchQueried", false);
		        	session.setAttribute("searchQuery", "");
		        %>
                <div style="margin-top:2%;">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Name</th><th>Remove</th><th>Download</th>
                            </tr>
                        </thead>
                        <tbody>
	                        <%
		                        if(files.size() > 0){
		                        	for(int i = 0; i < files.size(); i++){
		                        		String f = files.get(i);
		                        		if(f.length() > 0){
			                        		%><tr><%
			                        		%><td><%out.write(f);%></td><%
			                        		%><td><form action="RemoveFile" method="POST"><input type="text" class="d-none" name="btnFileNameRemove" value="<%out.write(f);%>"><input type="submit" class="btn btn-danger btn-sml" id="remove" role="button"  value="Remove"></form></td><%
			                        		%><td><form action="DownloadFile" method="POST"><input type="text" class="d-none" name="btnFileNameDownloadCP" value="<%out.write(f);%>"><input type="submit" class="btn btn-primary btn-sml" id="remove" role="button"  value="Download"></form></td><%
			                        		%></tr><%
		                        		}else{
		 		               	    	   %>
				              	    	   	<tr>
				              	    	   	<td>No files were found from the search query.</td>
				              	    	   	</tr>
				              	    	   <%
		                        		}
		                        	}
		                        }else{
		               	    	   %>
		              	    	   	<tr>
		              	    	   	<td>No files were found.</td>
		              	    	   	</tr>
		              	    	   <%
		                        }
	                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
	<%
		Boolean alertStatus = (Boolean)session.getAttribute("alertStatus");
		String alertMsg = (String)session.getAttribute("alertMessage");
		String fileName = (String)session.getAttribute("alertFileName");
		session.setAttribute("alertStatus", false);
		session.setAttribute("alertMessage","");
		session.setAttribute("alertFileName","");
		alertMsg = alertMsg != null ? alertMsg.replace("#","\\\""+ fileName +"\\\"") : "";
		if(alertStatus != null){
			if(alertStatus){
				%><script>function showAlert(){alert("<%out.write(alertMsg);%>");}</script><%
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
	<script>$(".custom-file-input").on("change", function() {var fileName = $(this).val().split("\\").pop();$(this).siblings(".custom-file-label").addClass("selected").html(fileName);});</script>
  
  </body>
</html>