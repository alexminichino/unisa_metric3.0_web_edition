package it.unisa.metric.ml;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import it.unisa.metric.struct.tree.CommentNode;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.Logistic;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;


/**
 * Class that manages the classifier based on logistic regression
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 */
public class PredictionManager {
	private Logistic logistic;
	private Instances trainingDataset;
	
	
	/**
	 * Constructor
	 * @param trainingDataset training dataset
	 * @throws Exception
	 */
	public PredictionManager(Instances trainingDataset) throws Exception {
			if (trainingDataset.classIndex() == -1)
				trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);
			this.trainingDataset = trainingDataset;
	        // model
	        logistic = new Logistic();
	}
	
	
	/**
	 * Provides training for the classifier
	 * @throws Exception
	 */
	public void train() throws Exception {
		logistic.buildClassifier(trainingDataset);
	}
	
	
	/**
	 * Makes the prediction
	 * @param commentNode comment from where to get the metrics 
	 * @return instance with predicted dependent variable 
	 * @throws Exception
	 */
	public Instance predictBugPresence(CommentNode commentNode) throws Exception {
		Instances unlabeledInstances = new Instances("predictionset", DatasetManager.getAttributeList(), 1);
		unlabeledInstances.setClassIndex(unlabeledInstances.numAttributes() - 1);
		double[] values= {
				commentNode.getVerticalDistanceFromParent(),
				commentNode.getHorizontalDistanceFromParent(),
				commentNode.getRealDistanceFromParent(),
				commentNode.getNumberOfWords(),
				1
		};
		Instance unlabeled = new DenseInstance(unlabeledInstances.numAttributes(),values);
		unlabeledInstances.add(unlabeled);
		double prediction  = logistic.classifyInstance(unlabeledInstances.get(0));
		unlabeledInstances.instance(0).setClassValue(prediction);
		return unlabeledInstances.instance(0);
	}
	
	
	/**
	 * Makes the prediction with boolean result
	 * @param commentNode comment from where to get the metrics
	 * @return Boolean value of prediction
	 * @throws Exception
	 */
	public boolean haveAbug(CommentNode commentNode) throws Exception {
		return predictBugPresence(commentNode).classValue()==1;
	}
	
	
	/**
	 * Performs a model evaluation using a validation dataset
	 * @param validationDatasetPath validation dataset path
	 * @throws InvalidDatasetException 
	 */
	public void evaluate(String datasetPath) throws InvalidDatasetException{
		 Evaluation evaluation;
		try {
			ConverterUtils.DataSource validationDataset = new ConverterUtils.DataSource(datasetPath);
	        Instances validation = validationDataset.getDataSet();
	        if (validation.classIndex() == -1)
	        	validation.setClassIndex(validation.numAttributes() - 1);
			evaluation = new Evaluation(trainingDataset);
			evaluation.evaluateModel(logistic, validation);
			writeResults(evaluation.toSummaryString(true));
		}
		catch (IOException | NullPointerException e) {
			throw new InvalidDatasetException(datasetPath+ " invalid or not foud in file system");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	     
	}
	
	
	/**
	 * Calculates the accuracy of the predictions
	 * @param predictions list of prediction
	 * @return accuracy in percentage
	 */
	public static double calculateAccuracy(ArrayList<Prediction> predictions) {
		double correct = 0;
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.get(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
		return 100 * correct / predictions.size();
	}
	
	
	/**
	 * Writes the results to a file
	 * @param results string of results
	 */
	private void writeResults(String results) {
		try {
			DatasetManager.createDirectoryIfNotExsits();
			BufferedWriter writer = new BufferedWriter(new FileWriter(DatasetConstants.RESULTS_PATH));
			writer.write(results);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
