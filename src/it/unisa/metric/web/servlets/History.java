package it.unisa.metric.web.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.unisa.metric.web.HistoryItem;
import it.unisa.metric.web.WebConstants;
import it.unisa.metric.web.utils.FileUtils;
import it.unisa.metric.web.utils.MD5;

/**
 * Servlet implementation class History
 */
@WebServlet("/History")
public class History extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public History() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nextPage;
		try {
			String user = (String) request.getSession().getAttribute("user");
			String sessionID  = MD5.getMd5(user );
			ArrayList<HistoryItem> historyItems = new ArrayList<>();

			String dirUrl = request.getContextPath()+"/"+WebConstants.RESULTS_DIRECTORY+"/"+sessionID;
			if(Files.exists(Paths.get(WebConstants.RESULTS_PATH+sessionID), LinkOption.NOFOLLOW_LINKS)) {
				for(String dir: FileUtils.getListOfSubDir(WebConstants.RESULTS_PATH+sessionID)) {
					historyItems.add(new HistoryItem(dir, dirUrl));
				}
				Gson g = new Gson();
				nextPage = request.getParameter("page")+"&historyItems="+g.toJson(historyItems);
			}
			else {
				nextPage = "noData";
			}

		} catch (NullPointerException e) {
			nextPage="login";
		}
		

		RequestDispatcher view = request.getRequestDispatcher("/Dispatcher?page="+nextPage);  
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
