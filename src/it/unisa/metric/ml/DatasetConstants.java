package it.unisa.metric.ml;

/**
 * A class that contains the constants used in the defect predition module
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 */
public class DatasetConstants {
	
	/**
	 * Dataset directory name
	 */
	public static final String DIRECTORY_NAME = "./datasets/";
	
	/**
	 * Training dataset path
	 */
	static final public String TRAINING_DATASET_PATH = DIRECTORY_NAME+"train.arff";
	
	
	/**
	 * Training dataset path
	 */
	static final public String VALIDATION_DATASET_PATH = DIRECTORY_NAME+"validation.arff";
	
	/**
	 * Predictions dataset path
	 */
	static final public String PREDICTION_DATASET_PATH = DIRECTORY_NAME+"predictions.arff";

	public static final String RESULTS_PATH = DIRECTORY_NAME+"results.txt";


	
}
