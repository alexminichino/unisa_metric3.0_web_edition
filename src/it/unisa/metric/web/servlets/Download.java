package it.unisa.metric.web.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.metric.web.WebConstants;

@WebServlet("/Download")
public class Download extends HttpServlet {
	private final int ARBITARY_SIZE = 1048;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// reads input file from an absolute path
		String fileName = req.getParameter("logfile");
		File downloadFile = new File(WebConstants.APPLICATION_PATH+File.separator+WebConstants.LOGS_DIRECTORY+File.separator+ fileName);
		FileInputStream inStream = new FileInputStream(downloadFile);

		// if you want to use a relative path to context root:
		String relativePath = getServletContext().getRealPath("");

		// obtains ServletContext
		ServletContext context = getServletContext();

		// gets MIME type of the file
		String mimeType = context.getMimeType(fileName);
		if (mimeType == null) {        
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}

		// modifies response
		resp.setContentType(mimeType);
		resp.setContentLength((int) downloadFile.length());

		// forces download
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		resp.setHeader(headerKey, headerValue);

		// obtains response's output stream
		OutputStream outStream = resp.getOutputStream();

		byte[] buffer = new byte[4096];
		int bytesRead = -1;

		while ((bytesRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inStream.close();
		outStream.close();
	}
}