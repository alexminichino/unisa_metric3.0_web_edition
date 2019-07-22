package it.unisa.metric.web;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.websocket.RemoteEndpoint.Basic;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.unisa.metric.Constants;
import it.unisa.metric.LocalException;
import it.unisa.metric.Parameters;
import it.unisa.metric.Parser;
import it.unisa.metric.Project;
import it.unisa.metric.Utils;
import it.unisa.metric.gui.DynamicTreeWindow;
import it.unisa.metric.metrics.Complexity;
import it.unisa.metric.ml.DatasetConstants;
import it.unisa.metric.ml.DatasetManager;
import it.unisa.metric.ml.InvalidDatasetException;
import it.unisa.metric.ml.PredictionManager;
import it.unisa.metric.report.Report;
import it.unisa.metric.report.ReportManager;
import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.graph.PackageInfo;
import it.unisa.metric.struct.table.Record;
import it.unisa.metric.struct.tree.CommentNode;
import it.unisa.metric.struct.tree.CommentTree;
import it.unisa.metric.struct.tree.Node;
import it.unisa.metric.struct.tree.PackageNode;
import it.unisa.metric.struct.tree.ProjectNode;
import weka.core.Instances;

/**
 * Main class of Metric Java.
 * @author Andrea d'Argenio, Alexander Minichino
 * @version 2.0
 * @since 1.0
 *
 */
public class WebCore {

	/**
	 * Parameters in input command line.
	 */
	private Parameters params;
	/**
	 * Project in input.
	 */
	private Project project;
	/**
	 * The parser.
	 */
	private Parser parser;
	/**
	 * Te bug report (if present)
	 */
	private HashMap<String, ArrayList<Report>> reportsMap;

	/**
	 * The JSON averages result.
	 */
	private String JSONAveragesResult;
	
	/**
	 * The JSON comment tree result
	 */
	private String JSONCommentTree;
	/**
	 * Constructor that calls all modules.
	 * @param args Input command line args.
	 * @param remote 
	 * @throws LocalException If an error occurs. 
	 */
	public WebCore(String[] args) throws LocalException {

		info(true);

		// check input params
		params = new Parameters(args, this.getClass().getName());
		params.print();

		// check project
		project = new Project(params);
		project.print();
		List<String> files = project.getSourceFiles();

		for(String s: files) {
			Utils.print("Source file:" + s);
		}

		// set parser
		parser = new Parser(params.getJavaVersion());
		parser.addClasspaths(project.getLibraryPath());
		parser.addClasspaths(project.getBinaryPath());
		parser.print();
		Scanner scanner = new Scanner(System.in);

		// set the data structures
		Hashtable<Integer, Record> table = new Hashtable<Integer, Record>();
		DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		CommentTree tree = CommentTree.createTree(project.getProjectName());

		StringBuilder sb = new StringBuilder();


		// module 1: fields and variables info.
		/*
		Utils.print("Parsing fields and variables.");
		if(params.classFilterEnabled()){
			for (String s : files) {
				for(String sf : params.getInputClasses()){
					if(s.equals(sf)){
						parser.parseVariables(project.getProjectPath(), project.getProjectName(), project.getSourcePath(), s, table);	
					}
				}		
			}	
		}else{
			for (String s : files) {
				parser.parseVariables(project.getProjectPath(), project.getProjectName(), project.getSourcePath(), s, table);			
			}	
		}


		int vars = 0, fields = 0, primitive = 0, referralType = 0;
		int singleLetter = 0, oneWord = 0, moreWords = 0, other = 0;
		for(Record record : table.values()) {
			if(record.isField())
				fields++;
			else
				vars++;
			if(record.isPrimitive())
				primitive++;
			else
				referralType++;
			if(record.getLexical() == Record.SINGLE_LETTER)
				singleLetter++;
			else if(record.getLexical() == Record.ONE_WORD)
				oneWord++;
			else if(record.getLexical() == Record.MORE_WORDS)
				moreWords++;
			else
				other++;
			sb.append(record.toString() + "\n");
		}

		Utils.print(table.size() + " fields and variables found.");
		Utils.print(fields + " fields found.");
		Utils.print(vars + " variables found.");
		Utils.print(primitive + " primitive types found.");
		Utils.print(referralType + " referral types found.");
		Utils.print(singleLetter + " single letter names found.");
		Utils.print(oneWord + " one word names found.");
		Utils.print(moreWords + " two or more words names found.");
		Utils.print(other + " other names found.");
		Utils.print(sb.toString());
		 */
		// module 2: methods invocation chain
		if(params.classFilterEnabled()){
			Utils.print("Parsing methods declarations.");
			for (String s : files){
				for(String sf : params.getInputClasses()){
					if(s.equals(sf)){
						parser.parseMethodDeclaration(project.getProjectPath(), project.getProjectName(), project.getSourcePath(), s, graph);
					}
				}
			}

			Utils.print("Parsing methods invocations.");
			for(String s : files){
				for(String sf : params.getInputClasses()){
					if(s.equals(sf)){
						parser.parseMethodInvocation(project.getProjectPath(), project.getProjectName(), project.getSourcePath(), s, graph);
					}
				}
			}		
		}else{
			Utils.print("Parsing methods declarations.");
			for (String s : files)
				parser.parseMethodDeclaration(project.getProjectPath(), project.getProjectName(), project.getSourcePath(), s, graph);

			Utils.print("Parsing methods invocations.");
			for(String s : files)
				parser.parseMethodInvocation(project.getProjectPath(), project.getProjectName(), project.getSourcePath(), s, graph);
		}

		int vertices = graph.vertexSet().size();
		int edges = graph.edgeSet().size();
		int oddVertices = 0;
		sb = new StringBuilder();

		for(MethodVertex vertex : graph.vertexSet()) {
			if(graph.inDegreeOf(vertex) == 0 && graph.outDegreeOf(vertex) == 0) {
				sb.append(vertex + "\n");
				oddVertices++;
			}
		}
		for(DefaultWeightedEdge edge : graph.edgeSet())
			sb.append(graph.getEdgeSource(edge) + " ---" + (int)graph.getEdgeWeight(edge) + "---> " + graph.getEdgeTarget(edge) + "\n");

		Utils.print(vertices + " methods found.");
		Utils.print(edges + " method invocations found.");
		Utils.print(oddVertices + " not invoked methods found.");
		Utils.print(sb.toString());

		//update methods complexity

		Complexity complexity = new Complexity();
		Utils.print("Updating complexity of each methods.");
		complexity.update(graph);
		Utils.print("Complexity update complete.");

		//update class info

		ClassInfo classInfo = new ClassInfo();
		Utils.print("Updating class info.");
		classInfo.update();
		Utils.print("Class info update complete.");

		//update package info

		PackageInfo packInfo = new PackageInfo();
		packInfo.update();

		// module 3: comment tree

		Utils.print("Parsing comments.");
		if(params.classFilterEnabled()){
			for(String s: files) {
				for(String sf : params.getInputClasses()){
					if(s.equals(sf)){
						parser.parseComment(project.getProjectPath(), project.getProjectName(), project.getSourcePath(), s, tree);
					}
				}
			}
		}else{
			for(String s: files) {
				parser.parseComment(project.getProjectPath(), project.getProjectName(), project.getSourcePath(), s, tree);
			}
		}

		Node root = (Node)tree.getRoot();
		int comments = countComments(root);
		int objects = countObjectsCommented(root);
		Utils.print(comments + " comments found.");
		Utils.print(objects + " lines of code commented found.");
		Utils.print(tree.toString());		

		//calcolo metriche
		MetricEvaluationWeb testing = new MetricEvaluationWeb(graph, tree);
		testing.metrics();
		//Salvo le medie
		JSONAveragesResult=testing.getJSONResult();
		//Salvo l'albero
		JSONCommentTree= createJSONTree(root);

		

		//Defect prediction module
		if(params.haveReportFilePath()) {
			Utils.print("Reading the reports...");
			reportsMap = ReportManager.getReportsMap(params.getReportFilePath(), project.getProjectDir());
		}

		if(params.createDatasetEnabled()) {
			//Create training dataset
			Utils.print("Creating dataset...");
			DatasetManager.createDatasetFromResults(tree, reportsMap);
		}
		if(params.predictBugPresenceEnabled()) {
			//Create Predictions Dataset
			PredictionManager pm;
			try {
				Utils.print("Making prediction manager...");
				Instances trainingDataset = DatasetManager.getDatasetFromFile(DatasetConstants.TRAINING_DATASET_PATH);
				pm = new PredictionManager(trainingDataset);
				pm.train();
				//Predict a bug presence for every comment nodes
				DatasetManager.createPredictionDataset(tree, pm);
				try {
					Utils.print("Evaluate model...");
					pm.evaluate(DatasetConstants.VALIDATION_DATASET_PATH);
				}
				catch (InvalidDatasetException e){
					Utils.print("Validation dataset not found");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		//GUI module
		if(params.commentsGUIEnabled()){
			//draw a tree
			new DynamicTreeWindow(tree, reportsMap);
		}
		info(false);
	}

	/**
	 * Counts lines of code that have comment/s.
	 * @param root Root of comment tree.
	 * @return Number of lines of code that have comment/s.
	 */
	private int countObjectsCommented(Node root) {
		int objects = 0;
		if(root.getType() != Node.PROJECT && root.getType() != Node.PACKAGE)
			if(((CommentNode)root).getCommentList().size() > 0)
				objects++;
		Enumeration<Node> children = root.children();
		while(children.hasMoreElements()) {
			Node child = children.nextElement();
			objects += countObjectsCommented(child);
		}
		return objects;
	}

	/**
	 * Counts comments in the comments tree.
	 * @param root Root of comments tree.
	 * @return Number of comments.
	 */
	private int countComments(Node root) {
		int comments = 0;
		if(root.getType() != Node.PROJECT && root.getType() != Node.PACKAGE)
			comments = ((CommentNode)root).getCommentList().size();
		Enumeration<Node> children = root.children();
		while(children.hasMoreElements()) {
			Node child = children.nextElement();
			comments += countComments(child);
		}
		return comments;
	}

	/**
	 */
	private ArrayList<CommentNode> getCommentNodeList(Node root, ArrayList<CommentNode> commentNodeList) {
		if(commentNodeList == null)
			commentNodeList = new ArrayList<>();
		if(root.getType() != Node.PROJECT && root.getType() != Node.PACKAGE)
			commentNodeList.add(((CommentNode)root));
		Enumeration<Node> children = root.children();
		while(children.hasMoreElements()) {
			Node child = children.nextElement();
			getCommentNodeList(child, commentNodeList);
		}
		return commentNodeList;
	}


	private SimpleCommentNode getSimpleTree(Node root, SimpleCommentNode simpleRoot) {
		if(root.getType() != Node.PROJECT) {

		}
		String type="";
		String name="";
		ASTNode n;
		int mod=0;
		List<String> comments;
		switch (root.getType()) {
		case Node.PROJECT:
			type="PROJECT";
			name = (((ProjectNode)root).getProjectName());
			break;
		case Node.PACKAGE:
			type="PACKAGE";
			name = ((PackageNode)root).getPackageName();
			break;
		case Node.ENUM:
			type="ENUM";
			n = ((CommentNode)root).getObjectCommented();
			name= ((EnumDeclaration)n).getName().toString();
			break;
		case Node.CLASS:
			type="CLASS";
			n = ((CommentNode)root).getObjectCommented();
			name= ((TypeDeclaration)n).getName().toString();
			mod = ((TypeDeclaration)n).getModifiers();
			if(((TypeDeclaration)n).isInterface()){

			}
			else if(Modifier.isPublic(mod)) {

			}
			else if(Modifier.isProtected(mod)) {

			}
			else if(Modifier.isPrivate(mod)) {

			}
			else {

			}

			break;
		case Node.METHOD:
			type="METHOD";
			n = ((CommentNode)root).getObjectCommented();

			List<SingleVariableDeclaration>  params = ((MethodDeclaration)n).parameters();
			name= ((MethodDeclaration)n).getName().toString() + params.stream().map(param -> param.toString()).collect(Collectors.joining(":", "(", ")"));		    	  
			mod = ((MethodDeclaration)n).getModifiers();
			if(Modifier.isPublic(mod)) {
				//
			}
			else if(Modifier.isProtected(mod)) {
				//	    		  
			}
			else if(Modifier.isPrivate(mod)) {
				//
			}
			else {
				//
			}
			break;
		case Node.FIELD:
			type="FIELD";
			n = ((CommentNode)root).getObjectCommented();
			mod = ((FieldDeclaration)n).getModifiers();
			if(Modifier.isPublic(mod)) {
				//
			}
			else if(Modifier.isProtected(mod)) {
				//	    		  
			}
			else if(Modifier.isPrivate(mod)) {
				//
			}
			else {
				//
			}

			Object f = ((FieldDeclaration)n).fragments().get(0);
			if(f instanceof VariableDeclarationFragment) {
				name = ((VariableDeclarationFragment)f).getName().toString();
			}
			else {
				name = ((FieldDeclaration)n).toString();
			}
			break;
		case Node.OTHER:
			type="OTHER";
			n = ((CommentNode)root).getObjectCommented(); 
			
			if(n instanceof ExpressionStatement) {
				name= ((ExpressionStatement)n).getExpression().toString();
			}
			else if(n instanceof PackageDeclaration) {
				name= n.toString();
			}
			else {
				name= n.toString();
			}
			break;

		default:
			break;
		}
		try {
			comments = ((CommentNode)root).getCommentList();
		} catch (ClassCastException e) {
			comments = new ArrayList<>();
		}
		
		if(simpleRoot == null)
			simpleRoot = new SimpleCommentNode(name, type, null, comments);
		else {
			SimpleCommentNode newNode = new SimpleCommentNode(name, type, simpleRoot, comments);
			simpleRoot.addChild(newNode );
			simpleRoot = newNode;
		}
		
		Enumeration<Node> children = root.children();
		while(children.hasMoreElements()) {
			Node child = children.nextElement();
			getSimpleTree(child, simpleRoot);
		}
		return simpleRoot;
	}


	//	public JSONObject toJSON(Node node, List<Node> others) {
	//	    JsonObject g = new JsonObject();
	//	    g.add("id", (CommentNode)root).);
	//	    json.put("id", node.id); // and so on
	//	    JsonElement gt = new JsonElement() {
	//			
	//			@Override
	//			public JsonElement deepCopy() {
	//				// TODO Auto-generated method stub
	//				return null;
	//			}
	//		};
	//	    
	//	    List children = new ArrayList<JSONObject>();
	//	    for(Node subnode : others) {
	//	        if(isChildOf(subnode, node)) {
	//	            others.remove(subnode);
	//	            children.add(toJSON(subnode, others));
	//	        }
	//	    }
	//	    json.put("children", children);
	//	    return json;
	//	}



	/**
	 * Shows information about the application.
	 * @param start <code>True</code> shows the starting information of application; <code>false</code> the ending one.
	 */
	private void info(boolean start) {
		if(start) {
			Utils.print("*** " + Constants.appName + " ***");
			Utils.print("*** " + Constants.appAcro + " " + Constants.version + " ***");
			Utils.print("*** " + Constants.authors + " ***");
		} else {
			Utils.print("*** End " + Constants.appAcro + " ***");
		}
	}

	public String GetJSONResult() {
		return JSONAveragesResult;
	}
	
	public String getJSONTree() {
		return JSONCommentTree;
	}
	
	private String createJSONTree(Node root) {
		SimpleCommentNode simpleTree = getSimpleTree(root, null);
		Gson g = new Gson();
		return g.toJson(simpleTree);
	}
}
