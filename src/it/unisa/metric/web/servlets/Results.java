package it.unisa.metric.web.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.nlp.io.EncodingPrintWriter.out;
import it.unisa.metric.web.Analysis;
import it.unisa.metric.web.WebConstants;
import it.unisa.metric.web.utils.FileUtils;
import it.unisa.metric.web.utils.MD5;

/**
 * Servlet implementation class Results
 */
@WebServlet("/Results")
public class Results extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Results() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Analysis analysis = getAnalysisFromFile(request);
		RequestDispatcher view = request.getRequestDispatcher("/Dispatcher?page=resultPage&avarages="+analysis.getJSONAverages()+"&name="+analysis.getName()+"&date="+new SimpleDateFormat("dd MM yyyy HH:mm").format(analysis.getDate())+"&logfile="+analysis.getLogFilename()+"&analysisId="+request.getParameter("analysisId"));  
        view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Analysis analysis = getAnalysisFromFile(request);
		PrintWriter out = response.getWriter();
	     
		out.println(analysis.getJsonComentTree());
	}
	
	private Analysis getAnalysisFromFile(HttpServletRequest request) {
		String analysisId = request.getParameter("analysisId");
		String sessionID  = MD5.getMd5((String) request.getSession().getAttribute("user"));
		return FileUtils.deserializeObj(WebConstants.RESULTS_PATH+ sessionID+File.separator+analysisId,"analysis.metric");
	}

}
