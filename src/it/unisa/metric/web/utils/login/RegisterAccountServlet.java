package it.unisa.metric.web.utils.login;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class RegisterAccountServlet
 */
@WebServlet("/RegisterAccountServlet")
public class RegisterAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterAccountServlet() {
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
		
		//lettura parametri dalla form 
		String name=request.getParameter("nameRegister");
		String surname=request.getParameter("surnameRegister");
		String company=request.getParameter("companyRegister");
		String username=request.getParameter("userRegister");
		String password=request.getParameter("passwordRegister");
		
		//creazione nuovo oggetto account
		Account newAc= new Account(name,surname,company,username, password);
		ArrayList<Account> listaAccount=new ArrayList<Account>();
		//Scrittura su file

		if (AccountManager.userExists(username)) {
			request.setAttribute("errorUsername", true);
			request.getRequestDispatcher("Dispatcher?page=newAccount").include(request, response);
		}
		else{
			AccountManager.registerAccount(newAc);
			request.getRequestDispatcher("Dispatcher?page=login").include(request, response);
		}
	}

}
