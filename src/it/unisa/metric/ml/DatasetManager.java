package it.unisa.metric.ml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import it.unisa.metric.report.Report;
import it.unisa.metric.report.ReportManager;
import it.unisa.metric.struct.tree.CommentNode;
import it.unisa.metric.struct.tree.CommentTree;
import it.unisa.metric.struct.tree.Node;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * A class that provides methods for managing and creating datasets
 * @author Alexander Minichino
 * @version 1.0
 * @since 1.0
 */
public class DatasetManager {
	
	
	/**
	 * Creates a new dataset
	 * @param tree comments tree
	 * @param reports a bugs report map
	 */
	public static void createDatasetFromResults(CommentTree tree, HashMap<String, ArrayList<Report>> reports) {
		HashMap<CommentNode, Boolean> bugPresenceMap = new HashMap<>();
		visitTree((Node)tree.getRoot(), reports, bugPresenceMap);
		saveTrainingDataset(bugPresenceMap);
	}
	
	
	/**
	 * Creates a dataset with the predictions made
	 * @param tree comments tree
	 * @param pm prediction manager
	 */
	public static void createPredictionDataset(CommentTree tree, PredictionManager pm) {
		try {
			HashMap<CommentNode, Boolean> bugPresenceMap = new HashMap<>();
			Instances dataset = new Instances("prediction", getAttributeList(),0);
			visitTree((Node) tree.getRoot(), bugPresenceMap, dataset, pm);
			writeDatasetFile(dataset, DatasetConstants.PREDICTION_DATASET_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Visits the comment tree to determine the presence of bugs for each comment
	 * @param root the root of tree
	 * @param reports a bugs report map 
	 * @param bugPresenceMap association comment bug presence
	 */
	private static void visitTree(Node root, HashMap<String, ArrayList<Report>> reports, HashMap<CommentNode, Boolean> bugPresenceMap) {
		if(root.getType() != Node.PROJECT && root.getType() != Node.PACKAGE) {
			CommentNode cn = (CommentNode)root;
			if(cn.getFilePath() != null) {
				boolean bugPresence = false;
				if(ReportManager.hasNeighborsReports(reports, cn.getFilePath(), cn.getParentLineNumber())) {
					bugPresence = true;
				}
				if(!bugPresenceMap.containsKey(cn))
					bugPresenceMap.put(cn, bugPresence);
			}
		}
		Enumeration<Node> children = root.children();
		while(children.hasMoreElements())
			visitTree(children.nextElement(),reports, bugPresenceMap);
	}
	
	
	/**
	 * Visits the comment tree to create the prediction dataset
	 * @param root root the root of tree
	 * @param bugPresenceMap association comment bug presence
	 * @param dataset dataset to populate
	 * @param manager prediction manager
	 * @throws Exception
	 */
	private static void visitTree(Node root, HashMap<CommentNode, Boolean> bugPresenceMap, Instances dataset, PredictionManager manager) throws Exception {
		if(root.getType() != Node.PROJECT && root.getType() != Node.PACKAGE) {
			CommentNode cn = (CommentNode)root;
			if(cn.getFilePath() != null) {
				dataset.add(manager.predictBugPresence(cn));
			}
		}
		Enumeration<Node> children = root.children();
		while(children.hasMoreElements())
			visitTree(children.nextElement(), bugPresenceMap, dataset, manager);
	}
	
	
	/**
	 * Saves the map data as a dataset
	 * @param bugPresenceMap
	 */
	private static void saveTrainingDataset(HashMap<CommentNode, Boolean> bugPresenceMap) {
		
		Instances newDataset =  new Instances("Train", getAttributeList(), bugPresenceMap.size());
		for (Map.Entry<CommentNode, Boolean> entry : bugPresenceMap.entrySet() ) {
			double[] values= {
					entry.getKey().getVerticalDistanceFromParent(),
					entry.getKey().getHorizontalDistanceFromParent(),
					entry.getKey().getRealDistanceFromParent(),
					entry.getKey().getNumberOfWords(),
					(entry.getValue())? 1:0
					};
			newDataset.add(new DenseInstance(values.length, values));
		}
		writeDatasetFile(newDataset, DatasetConstants.TRAINING_DATASET_PATH);
	}
	
	
	/**
	 * Gets the attributes of the dataset
	 * @return attributes list
	 */
	public static ArrayList<Attribute> getAttributeList() {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(new Attribute("VDFP"));
		atts.add(new Attribute("HDFP")); 
		atts.add(new Attribute("RDFP")); 
		atts.add(new Attribute("NOW")); 
		atts.add(new Attribute("BUG_PRESENCE", Arrays.asList("0","1"))); 
		return atts;
	}
	
	
	/**
	 * Saves the dataset in a file
	 * @param dataset dataset to save
	 */
	private static void writeDatasetFile(Instances dataset, String pathToSave) {
		try {
			createDirectoryIfNotExsits();
			BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSave));
			writer.write(dataset.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Reads a dataset from a file
	 * @param datasetPath the path of the file
	 * @return instances of dataset
	 * @throws InvalidDatasetException
	 */
	public static Instances getDatasetFromFile(String datasetPath) throws InvalidDatasetException {
	 	ConverterUtils.DataSource trainingDataset;
		try {
			 trainingDataset = new ConverterUtils.DataSource(datasetPath);
			 return trainingDataset.getDataSet();
		} catch (Exception e) {
			throw new InvalidDatasetException(datasetPath+ " not foud in file system");
		}
	}
	
	
	/**
	 * 
	 */
	public static void createDirectoryIfNotExsits() {
	    File file = new File(DatasetConstants.DIRECTORY_NAME);
	    if (!file.exists()) {
	        if (file.mkdir()) {
	            System.out.println("Datasets directory is created!");
	        }
	        else {
		        System.out.println("Failed to create datasets directory!");
		    }
	    } 
	}
}
