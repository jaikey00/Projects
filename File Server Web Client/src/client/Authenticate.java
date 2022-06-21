package client;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Authenticate
 */
@WebServlet("/Authenticate")
public class Authenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authenticate() {
        super();
        // TODO Auto-generated constructor stub
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
		Client client = Server.getAuthClient();
		
		String user = request.getParameter("username");
		char[] pass = request.getParameter("password").toCharArray();
		
		
		if(client.sendCommandAuthenticate(user,pass)) {
			Server.addClient(request.getSession().getId(),user);
			System.out.println("Connected new client with session ID: "+request.getSession().getId());
			response.sendRedirect(request.getContextPath() + "/Home.jsp");
		}else {
			request.getSession().setAttribute("loginInvalid",true);
			response.sendRedirect(request.getContextPath() + "/Login.jsp");
		}
	}

}
