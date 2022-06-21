package client;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchFile
 */
@WebServlet("/SearchFile")
public class SearchFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchFile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Client client = Server.getClient(request.getSession().getId());
		String searchQuery = request.getParameter("searchBarMain");
		
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> files = client.getAllFiles(client.getUser());
		
		System.out.println(searchQuery.length());
		
		if(searchQuery.length() > 0) {
			for(String f : files) {
				if(f.contains(searchQuery)) {
					result.add(f);
				}
			}
		}
		
		String resultString = "";
		
		for(int i = 0; i < result.size(); i++) {
			resultString += i == 0 ? result.get(i) : "," + result.get(i);
		}
		
		System.out.println(resultString.length());
		System.out.println(result.size());
		
		request.getSession().setAttribute("fileSearchQueried",true);
		request.getSession().setAttribute("searchQuery",searchQuery);
		request.getSession().setAttribute("fileSearchResult",resultString);

		response.sendRedirect(request.getContextPath() + "/ControlPanel.jsp");
		
		
	}

}
