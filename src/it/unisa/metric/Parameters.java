package it.unisa.metric;

import argparser.ArgParser;
import argparser.BooleanHolder;
import argparser.IntHolder;
import argparser.StringHolder;

/**
 * Parser for input arguments.
 * @author Andrea d'Argenio, Alexander Minichino
 * @version 2.0
 * @since 1.0
 *
 */
public class Parameters {
	/**
	 * StringHolder used to check if the class filter must be enabled.
	 */
	StringHolder inputClasses = new StringHolder();
	/**
	 * StringHolder used to check if the class filter must be enabled.
	 */
	BooleanHolder classFilter = new BooleanHolder();
	/**
	 * StringHolder for project path.
	 */
	StringHolder path = new StringHolder();
	/**
	 * IntHolder for java version.
	 */
	IntHolder java = new IntHolder();
	/**
	 * Parser for input arguments.
	 */
	ArgParser parser = new ArgParser("");
	/**
	 * Report file for analysis of affected files.
	 */
	StringHolder reportFilePath = new StringHolder();
	/**
	 * BooleanHolder used to verify whether to enable the creation of the dataset
	 */
	BooleanHolder createDataset = new BooleanHolder();
	/**
	 * BooleanHolder used to check whether to enable bug prediction
	 */
	BooleanHolder predictBugPresence = new BooleanHolder();
	/**
	 * BooleanHolder used to check whether to enable comments GUI
	 */
	BooleanHolder commentsGUI = new BooleanHolder();
	/**
	 * StringHolder for source path.
	 */
	StringHolder sourcePath = new StringHolder();

	/**
	 * Constructor for the arguments parser.
	 * @param args Input arguments from command line.
	 * @param name Name of the main class.
	 * @throws LocalException If errors occurs.
	 */
	public Parameters(String[] args, String name) throws LocalException {
		init();
		
		parser.setSynopsisString("java " + name + " -path <string> -java {[1-9]} -classfilter {true, false} -class {class1,class2,...} -reportFilePath <string>");
		
		parser.addOption("-path %s #Java project path", path);
		parser.addOption("-java %d {[1,9]} #Java compiler version", java);
		parser.addOption("-classfilter %b #Enable class filter", classFilter);
		parser.addOption("-class %s #Path of each class separed by a comma", inputClasses);
		parser.addOption("-reportFilePath %s #Report file(xlsx or xls) path ", reportFilePath);
		parser.addOption("-createDataset %b #Enable creation of the dataset", createDataset);
		parser.addOption("-predictBugPresence %b #Enable bug prediction", predictBugPresence);
		parser.addOption("-commentsGUI %b #Enable comments GUI", commentsGUI);
		parser.addOption("-sourcePath %s #Java source path (e.g. src)", sourcePath);
		parser.matchAllArgs(args);	

		if(path.value == null) {
			throw new LocalException("Project path parameter is required.");
		}
	}
	
	/**
	 * Sets java version to 9.
	 */
	private void init() {
		java.value = 9;
	}
	
	/**
	 * Gets the project path from input arguments.
	 * @return Project path.
	 */
	public String getProjectPath() {
		return path.value;
	}	
	
	/**
	 * Gets the java version from input arguments.
	 * @return Java version (default: <code>9</code>).
	 */
	public int getJavaVersion() {
		return java.value;
	}	
	
	/**
	 * Gets if the class filter must be enabled from the input arguments.
	 * @return true if set (default: <code>False</code>).
	 */
	public boolean classFilterEnabled(){
		return classFilter.value;
	}
	
	/**
	 * Gets the classes to be parsed from the input arguments.
	 * @return a String Array of the classes that must be parsed.
	 */
	public String[] getInputClasses(){
		if(classFilterEnabled()){
			String[] toReturn = inputClasses.value.split(",");
			return toReturn;
		}else
			return null;
	}
	/**
	 * Shows the usage message.
	 */
	public void usage() {
		Utils.print(parser.getHelpMessage());
	}
	
	/**
	 * Shows application parameters.
	 */
	public void print() {
		Utils.print("Parameters.");
		Utils.print("path = " + path.value);
		Utils.print("Sorce path = " + sourcePath.value);
		Utils.print("java = " + java.value);
		if(classFilterEnabled()){
			Utils.print("Class filter enabled.");
			Utils.print("Classes to be parsed:");
			for(String s : getInputClasses()){
				Utils.print(s);
			}
			Utils.print("---");
		}else{
			Utils.print("Class filter not enabled.");
		}
		if(reportFilePath.value!=null) {
			Utils.print("reportFilePath = " + reportFilePath.value);
		}
		Utils.print("createDataset is "+(!createDatasetEnabled()? "not":"") +" enabled.");
		Utils.print("predictBugPresence is "+(!predictBugPresenceEnabled()? "not ":"") +"enabled.");
		Utils.print("predictBugPresence is "+(!predictBugPresenceEnabled()? "not ":"") +"enabled.");
		Utils.print("commentsGUI is "+(!commentsGUIEnabled()? "not ":"") +"enabled.");
	}
	
	/**
	 * Gets the report file to be used for analysis of affected files.
	 * @return a String path of report file.
	 */
	public String getReportFilePath() {
		return reportFilePath.value;
	}
	
	/**
	 * Gets if report file path is present in input arguments.
	 * @return true if set (default: <code>False</code>).
	 */
	public boolean haveReportFilePath() {
		return reportFilePath.value != null;
	}
	
	/**
	 * Gets if the createDataset is enabled from the input arguments.
	 * @return true if set (default: <code>False</code>).
	 */
	public boolean createDatasetEnabled(){
		return createDataset.value;
	}
	
	/**
	 * Gets if the predictBugPresence is enabled from the input arguments.
	 * @return true if set (default: <code>False</code>).
	 */
	public boolean predictBugPresenceEnabled(){
		return predictBugPresence.value;
	}
	
	/**
	 * Gets if the commentsGUI is enabled from the input arguments.
	 * @return true if set (default: <code>False</code>).
	 */
	public boolean commentsGUIEnabled(){
		return commentsGUI.value;
	}
	
	/**
	 * Gets the source path from input arguments.
	 * @return Source path.
	 */
	public String getSourcePath() {
		return sourcePath.value;
	}
	
	/**
	 * Gets if source path is present in input arguments.
	 * @return true if set (default: <code>False</code>).
	 */
	public boolean haveSourcePath() {
		return sourcePath.value != null;
	}
	
	
	
}
