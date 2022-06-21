

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import objects.Document;

/**
 * Servlet implementation class SearchFile
 */
@WebServlet("/SearchFile")
public class SearchFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SearchFile() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rawSearchQuery = request.getParameter("searchBarMain").toLowerCase();
		String[] searchQuery = rawSearchQuery.split(" ");
		
		String location = SearchFile.class.getProtectionDomain().getCodeSource().getLocation().toString();
		location = location.substring(6,location.indexOf("QueryIntentSearchEngine/")+24);

		UAQuery query = new UAQuery(location+"inputs",location+"outputs",6);
		
		ArrayList<Document> result = query.runQuery(searchQuery);

		request.getSession().setAttribute("searchQuery",request.getParameter("searchBarMain"));
		request.getSession().setAttribute("fileSearchResult",result);

		response.sendRedirect(request.getContextPath() + "/Search.jsp");
	}
	
	protected String convertDocArrayToSimpleJson(ArrayList<Document> docs) {
		String JSON = "";
		
		for(int i = 0; i < docs.size(); i++) {
			JSON += i != 0 ? "," : "";
			JSON += "{name:"+docs.get(i).name+",";
			JSON += "doc_id:"+docs.get(i).doc_id+",";
			JSON += "score:"+docs.get(i).score+"}";
		}
		
		return JSON;
	}

}
