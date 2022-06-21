package client;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ClientServlet
 */
@WebServlet("/RemoveFile")
public class RemoveFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveFile() {
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

		try {
			Client client = Server.getClient(request.getSession().getId());
			String fileName = request.getParameter("btnFileNameRemove");
			client.sendCommandRemoveFile(client.getUser(),fileName);
			
			request.getSession().setAttribute("alertStatus", true);
			request.getSession().setAttribute("alertFileName", fileName);
			request.getSession().setAttribute("alertMessage", "File # was removed successfully!");
			response.sendRedirect(request.getContextPath() + "/ControlPanel.jsp");
		}catch(Exception e) {
			request.getSession().setAttribute("alertStatus", true);
			request.getSession().setAttribute("alertMessage", "File failed to be removed from the server.");
			response.sendRedirect(request.getContextPath() + "/ControlPanel.jsp");
		}
		
	}

}
