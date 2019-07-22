package it.unisa.metric;
/**
 * A simple local exception
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class LocalException extends Exception {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with a message.
	 * @param message Message to show with exception.
	 */
	public LocalException(String message) {
		super(message);
	}
		
}
