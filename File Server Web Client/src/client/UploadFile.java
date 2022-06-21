package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;

/**
 * Servlet implementation class AddFile
 */
@WebServlet("/UploadFile")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 5,
		maxFileSize = 1024 * 1024 * 100,
		maxRequestSize = 1024 * 1024 * 100
)
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFile() {
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
			
			Part file = request.getPart("fileUploadField");

			String fileName = file.getSubmittedFileName();
			client.sendCommandAddFile(client.getUser(),fileName,request.getParts());
			
			request.getSession().setAttribute("alertStatus", true);
			request.getSession().setAttribute("alertFileName", fileName);
			request.getSession().setAttribute("alertMessage", "File # was uploaded successfully!");
			response.sendRedirect(request.getContextPath() + "/ControlPanel.jsp");
		}catch(Exception e) {
			request.getSession().setAttribute("alertStatus", true);
			request.getSession().setAttribute("alertMessage", "File failed to upload to the server.");
			response.sendRedirect(request.getContextPath() + "/ControlPanel.jsp");
		}
		
	}

}
