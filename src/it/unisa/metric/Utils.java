package it.unisa.metric;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
/**
 * Has print utilities for application.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class Utils {
	
	/**
	 * Formats and prints a message.
	 * @param message Message to show.
	 */
	public static void print(String message) {
		
		boolean multiple = message.contains("\n");
		String format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Timestamp(new java.util.Date().getTime()));
		
		if(multiple) {
			System.out.println("[" + format + "] " + Constants.appAcro + ":[");
			System.out.println(message);
			System.out.println("[" + format + "] " + Constants.appAcro + ":]");
			
		} else
			System.out.println("[" + format + "] " + Constants.appAcro + ":: " + message);
	}

	/**
	 * Formats and prints an exception.
	 * @param e Exception to show.
	 */
	public static void print(Exception e) {
		print("ERROR: " + e.getMessage());
	}


}
