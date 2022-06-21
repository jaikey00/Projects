<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@page import="objects.Document"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
	
	<title>Search Engine Web Client</title>
  </head>
  <body onload="setIsLoadingFalse()">
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
            <h1 class="display-3">Search for a document</h1>
            <hr class="my-2">
            <%
            	String searchQuery = (String)session.getAttribute("searchQuery");
            	ArrayList<Document> docs = (ArrayList<Document>)session.getAttribute("fileSearchResult");

            %>
            <div style="padding-left:50px;padding-right:50px;padding-top:15px;">
                <h1>Query</h1>
                <form action="SearchFile" method="POST">
		            <div class="input-group md-form form-sm form-2 pl-0">
		                <input name="searchBarMain" style="height:48px;" class="form-control my-0 py-1" type="text" placeholder="Search" value="<%out.write(searchQuery != null ? searchQuery : "");%>" aria-describedby="searchHelp">
		                <div class="input-group-append">
		                    <input type="submit" name="submit" class="btn btn-primary btn-lg" onclick="setIsLoadingTrue()" value="Search"></input>
		                </div>
		            </div>
		        </form>
                <div style="margin-top:2%;">
                    <table class="table" id="loading">
                        <tbody>
		                    <tr>
		                       	<td>
		                        	<div class="spinner-border text-primary" role="status">
										<span class="sr-only">Loading...</span>
									</div>
								</td>
							</tr>
						</tbody>
                    </table>
                    <table class="table" id="tableContent">
                        <tbody>
	                    	<%
		                        if(docs != null && docs.size() > 0){
		                        	for(int i = 0; i < docs.size(); i++){
		                        		Document d = docs.get(i);
		                        		%><tr><%
		                        		%><td>
		                        			<h4>Document Name: <span style="font-size:20px;"><%out.write(d.name+"\n");%></span></h4>
		                        			<h4>Document ID: <span style="font-size:20px;"><%out.write(d.doc_id+"\n");%></span></h4>
		                        			<h4>Document Score: <span style="font-size:20px;"><%out.write(d.score+"\n");%></span></h4>
		                        		  </td>
		                        		<%
		                        		%><td><form action="ViewFile" method="POST"><input type="text" class="d-none" name="btnFileNameView" value="<%out.write(d.name);%>"><input type="submit" class="btn btn-primary btn-sml" id="view" role="button"  value="View"></form></td><%
		                        		%></tr><%
		                        	}
		                        }else if(docs == null){
			               	    	   %>
			              	    	   	<tr>
			              	    	   		<td>Make a search to see some results!</td>
			              	    	   	</tr>
			              	    	   <%
		                        }else if(docs.size() == 0){
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

    <!-- JS stuff -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
	<script>$(".custom-file-input").on("change", function() {var fileName = $(this).val().split("\\").pop();$(this).siblings(".custom-file-label").addClass("selected").html(fileName);});</script>
  	<script>
  		function setIsLoadingTrue(){
  			document.getElementById("tableContent").style.display="none";
  			document.getElementById("loading").style.display="inline";
  			document
  		}
  		
  		function setIsLoadingFalse(){
  			console.log("ASDASDASDSA");
  			document.getElementById("loading").style.display="none";
  		}
  	</script>
  </body>
</html>