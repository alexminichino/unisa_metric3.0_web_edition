package it.unisa.metric.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.metric.web.utils.login.Account;
import it.unisa.metric.web.utils.login.AccountManager;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String user= request.getParameter("userName");
		String pass=request.getParameter("passWord");
		
		AccountManager mangaer = new AccountManager();
		if (mangaer.login(new Account(user, pass))) {
			HttpSession session=request.getSession();
			session.setAttribute("user", user);
			request.getRequestDispatcher("History?page=dashboard").include(request, response);
		}
		
		else{
			request.setAttribute("errorLogin", true);
			request.getRequestDispatcher("Dispatcher?page=login").include(request, response);
		}

		
	}

}
