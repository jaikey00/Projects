import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ClientServlet
 */
@WebServlet("/ViewFile")
public class ViewFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewFile() {
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
		String fileName = request.getParameter("btnFileNameView");
		
		String fileContent = "";
		
		String location = ViewFile.class.getProtectionDomain().getCodeSource().getLocation().toString();
		location = location.substring(6,location.indexOf("QueryIntentSearchEngine/")+24);
		
		File f = new File(location+"inputs/"+fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		StringBuilder sb = new StringBuilder();
		
		String line;
		while((line = br.readLine()) != null) {
			sb.append(line+"\n");
		}
		
		request.getSession().setAttribute("fileName", fileName);
		request.getSession().setAttribute("fileContent", sb.toString());
		
		response.sendRedirect(request.getContextPath() + "/FileView.jsp");
		
	}

}
