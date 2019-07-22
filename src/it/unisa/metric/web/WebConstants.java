package it.unisa.metric.web;

import java.io.File;

/**
 * Global parameters for the web-application.
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 */
public class WebConstants {
	
	
	/**
	 * Project path
	 */
	public static final String APPLICATION_PATH=System.getenv("METRIC_APPLICATION_PATH");
	
	/**
	 * Web socket url
	 */
	public static final String SOCKET_URL="ws://localhost:8080/ProgettoIGES/websocket";
	
	/**
	 * Max size of single file uploaded
	 */
	public static final int MAX_UPLOAD_FILE_SIZE=150;
	
	/**
	 * Max size of request
	 */
	public static final int MAX_UPLOAD_REQUEST=150;
	
	
	/**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    public static final String UPLOAD_DIRECTORY = "UPLOADED_FILES";
    
    /**
     * Name of the directory where unzipped files will be saved
     */
    public static final String UNZIPPED_DIRECTORY = "UNZIPPED_FILES";
    
    /**
     * Name of the directory where results will be saved
     */
    public static final String RESULTS_DIRECTORY = "RESULTS";
    
    
    /**
     * Name of the directory where users profile will be saved
     */
    public static final String PROFILES_DIRECTORY = "PROFILES";
    
    
    
    
    /**
     * Name of the directory where logs will be saved
     */
    public static final String LOGS_DIRECTORY = "logs";
    
    /**
     * Name of the directory of tests files
     */
    public static final String TESTING_DIRECTORY = "TESTING_FILES";
    
    
    
    /**
     * Limit of websocket buffer
     */
    public static final int WEBSOCKET_BUFFER_LIMIT=2000;
    
    
    /**
     * Path where results will be saved
     */
    public static final String RESULTS_PATH= APPLICATION_PATH + File.separator + RESULTS_DIRECTORY+File.separator;
    
    
    /**
     * Path directory where uploaded files will be saved
     */
    public static final String UPLOAD_PATH= APPLICATION_PATH + File.separator + UPLOAD_DIRECTORY+File.separator;
    
    
    /**
     * Path directory where user profiles will be saved
     */
    public static final String USERS_PROFILE_PATH= APPLICATION_PATH + File.separator + PROFILES_DIRECTORY+File.separator;

    
    
    /**
     * Path directory of tests files  
     */
    public static final String TESTING_DIRECTORY_PATH= APPLICATION_PATH + File.separator + TESTING_DIRECTORY+File.separator;

}
