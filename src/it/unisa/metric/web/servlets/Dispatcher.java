package it.unisa.metric.web.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class Dispatcher
 */
@WebServlet("/Dispatcher")
public class Dispatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String BASE_URL_REQUEST="/dispatcher.jsp?page=";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dispatcher() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestPage= request.getParameter("page");
        HttpSession session=request.getSession();
        String nextPage;
		if (session==null || session.getAttribute("user") == null && !requestPage.equals("login")  && !requestPage.equals("newAccount"))
		{
			nextPage="login";	
		}
		
		else {
			if(requestPage==null || requestPage=="")
				nextPage="dashboard";
			else
				nextPage = requestPage;
		}
		RequestDispatcher view = request.getRequestDispatcher(BASE_URL_REQUEST+nextPage);
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
