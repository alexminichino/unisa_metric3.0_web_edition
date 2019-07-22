package it.unisa.metric.web.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import it.unisa.metric.web.WebConstants;

import org.apache.commons.fileupload.FileItem;


/**
 * Servlet implementation class Upload
 */
@WebServlet("/Upload")
@MultipartConfig(fileSizeThreshold=1024*1024*2,              // 2MB
maxFileSize=1024*1024* WebConstants.MAX_UPLOAD_FILE_SIZE,    // 10MB
maxRequestSize=1024*1024* WebConstants.MAX_UPLOAD_REQUEST)   // 50MB
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * handles file upload
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// gets absolute path of the web application
		String appPath = WebConstants.APPLICATION_PATH;
		// constructs path of the directory to save uploaded file
		String savePath = appPath + File.separator + WebConstants.UPLOAD_DIRECTORY;

		//Base Url 
		String baseUrl = request.getRequestURL().toString().split("/Upload")[0];
		String urlSep = "/";

		// creates the save directory if it does not exists
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

		//name of project
		String projectName="";


		//List of saved files
		ArrayList<String> extractedFiles= new ArrayList<>();

		//process only if its multipart content
		if(ServletFileUpload.isMultipartContent(request)){
			try {
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

				//Retrive projectname
				for(FileItem item : multiparts){
					if(item.isFormField() && item.getFieldName().equals("project_name")){
						projectName = item.getString();
					}
				}

				for(FileItem item : multiparts){
					if(!item.isFormField()){
						String name = new File(item.getName()).getName();
						name = replaceBlanks(projectName)+"."+name.split("\\.")[1];
						item.write( new File(savePath + File.separator+ name));
						extractedFiles.add(name);
					}
				}

				//File uploaded successfully
				response.setStatus(HttpServletResponse.SC_OK);

			} catch (Exception ex) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File Upload Failed");

			}          

		}
		else{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Sorry this Servlet only handles file upload request");
		}

		request.setAttribute("message", "Upload has been done successfully!");
		response.getWriter().append(response.encodeURL(baseUrl+urlSep+"Dispatcher?page=processing&files="+String.join(",", extractedFiles)));

	}
	
	
	private String replaceBlanks(String s) {
		return s.replaceAll(" ", "_");
	}




}
