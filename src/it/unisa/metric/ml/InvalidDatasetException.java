package it.unisa.metric.ml;

/**
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 *
 */
public class InvalidDatasetException extends Exception {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 1L; 
	
	/**
	 * Constructor with a message.
	 * @param message Message to show with exception.
	 */
	public InvalidDatasetException(String message) {
		super(message);
	}
}
