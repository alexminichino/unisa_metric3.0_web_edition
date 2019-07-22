package it.unisa.metric;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.unisa.metric.FileManager;
import it.unisa.metric.JarFilenameFilter;
import it.unisa.metric.Constants;
import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.table.Record;
import it.unisa.metric.struct.tree.CommentTree;
import it.unisa.metric.visitor.FieldAndVariableVisitor;
import it.unisa.metric.visitor.MethodDeclarationVisitor;
import it.unisa.metric.visitor.MethodInvocationVisitor;
import it.unisa.metric.visitor.ElementVisitor;
import it.unisa.metric.visitor.CommentVisitor;
/**
 * Parser for files with some purpose.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class Parser {

	/**
	 * Java version (default: <code>9</code>).
	 */
	private String _javaVersion = JavaCore.VERSION_9;
	/**
	 * Application classpath (system classpath + lib files in project).
	 */
	private String[] _classpath = new String[0];
	/**
	 * Classpath separator for system in use.
	 */
	private String _classpathSeparator = System.getProperty("path.separator");

	private String _jreHome;
	/**
	 * Create a parser from javaversion.
	 * @param javaVersion Java version.
	 */
	public Parser(int javaVersion) {
		Utils.print("File parsing.");
		
		String javaClassPath = System.getProperty("java.class.path");
		if (javaClassPath != null && !javaClassPath.equals("")) {
			addClasspaths(javaClassPath.split(_classpathSeparator));
		}
		_jreHome = System.getProperty("java.home");
		if (_jreHome != null && !_jreHome.equals("")) {
			addClasspaths(_jreHome + File.separator + Constants.libraryPath);
		}

		switch(javaVersion) {
		case 1: _javaVersion = JavaCore.VERSION_1_1; break;
		case 2: _javaVersion = JavaCore.VERSION_1_2; break;
		case 3: _javaVersion = JavaCore.VERSION_1_3; break;
		case 4: _javaVersion = JavaCore.VERSION_1_4; break;
		case 5: _javaVersion = JavaCore.VERSION_1_5; break;
		case 6: _javaVersion = JavaCore.VERSION_1_6; break;
		case 7: _javaVersion = JavaCore.VERSION_1_7; break;
		case 8: _javaVersion = JavaCore.VERSION_1_8; break;
		case 9: _javaVersion = JavaCore.VERSION_9; break;
		default:_javaVersion = JavaCore.VERSION_9; break;
		}
	}

	public void addClasspath(String path) {
		if (FileManager.fileExists(path) || FileManager.directoryExists(path)) {
			String[] nClasspath = new String[_classpath.length + 1];
			System.arraycopy(_classpath, 0, nClasspath, 0, _classpath.length);
			nClasspath[_classpath.length] = new File(path).getAbsolutePath();
			_classpath = new String[nClasspath.length];
			System.arraycopy(nClasspath, 0, _classpath, 0, nClasspath.length);
		}
	}

	public void addClasspaths(String[] paths) {
		for (String path : paths) {
			addClasspath(path);
		}
	}

	public void addClasspaths(String path) {
		if (FileManager.directoryExists(path)) {
			File[] dirContents = new File(path).listFiles(new JarFilenameFilter());
			String[] nClasspath = new String[_classpath.length + dirContents.length];
			System.arraycopy(_classpath, 0, nClasspath, 0, _classpath.length);

			int i = 0;
			for (File f : dirContents) {
				nClasspath[_classpath.length + (i++)] = f.getAbsolutePath();
			}

			_classpath = new String[nClasspath.length];
			System.arraycopy(nClasspath, 0, _classpath, 0, nClasspath.length);

			addClasspath(path);
		}
	}
	
	/**
	 * Gives the parser classpath.
	 * @return Parser classpath.
	 */
	public String getClasspath(boolean print) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < _classpath.length; i++) {
			if(i == (_classpath.length - 1))
				sb.append(_classpath[i]);
			else 
				sb.append(_classpath[i] + _classpathSeparator);
			if (print) {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	/**
	 * Parses a file to retrieve information about fields and variables.
	 * @param projectPath Project path.
	 * @param project Project name.
	 * @param filePath File path.
	 * @param fileName File name.
	 * @param table Data structure for store information about fields and variables.
	 * @throws LocalException If errors occurs.
	 */
	public void parseVariables(String projectPath, String project, String filePath, String fileName, Hashtable<Integer, Record> table) throws LocalException {

		Utils.print("Parsing file: " + fileName);

		try {
			String str = FileManager.readFileToString(filePath + File.separator + fileName);

			ASTParser parser = ASTParser.newParser(AST.JLS9);
			parser.setSource(str.toCharArray());

			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setUnitName(project + File.separator + fileName);
			parser.setEnvironment(_classpath, new String[] { filePath }, new String[] { "UTF8" }, true);

			Map<String, String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(_javaVersion, options);
			parser.setCompilerOptions(options);

			CompilationUnit compilation = (CompilationUnit) parser.createAST(null);
			compilation.accept(new FieldAndVariableVisitor(table));
			
		} catch(IOException ioe) {
			throw new LocalException("Error parsing file '" + fileName + "'");
		}
	}
	
	/**
	 * Parses a file to retrieve information about method declarations.
	 * @param projectPath Project path.
	 * @param project Project name.
	 * @param filePath File path.
	 * @param fileName File name.
	 * @param graph Graph for setting vertices with method declarations.
	 * @throws LocalException If errors occurs.
	 */
	public void parseMethodDeclaration(String projectPath, String project, String filePath, String fileName, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph) throws LocalException {

		Utils.print("Parsing file: " + fileName);

		try {
			String str = FileManager.readFileToString(filePath + File.separator + fileName);

			ASTParser parser = ASTParser.newParser(AST.JLS9);
			parser.setSource(str.toCharArray());

			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setUnitName(project + File.separator + fileName);
			parser.setEnvironment(_classpath, new String[] { filePath }, new String[] { "UTF8" }, true);

			Map<String, String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(_javaVersion, options);
			parser.setCompilerOptions(options);

			final CompilationUnit compilation = (CompilationUnit) parser.createAST(null);
			
			compilation.accept(new MethodDeclarationVisitor(graph));

		} catch(IOException ioe) {
			throw new LocalException("Error parsing file '" + fileName + "'");
		}
	}
	
	/**
	 * Parses a files to retrieve information about method invocations.
	 * @param projectPath Project path.
	 * @param project Project name.
	 * @param filePath File path.
	 * @param fileName File name.
	 * @param graph Graph for setting edges with method invocations.
	 * @throws LocalException If errors occurs.
	 */
	public void parseMethodInvocation(String projectPath, String project, String filePath, String fileName, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph) throws LocalException {

		Utils.print("Parsing file: " + fileName);

		try {
			String str = FileManager.readFileToString(filePath + File.separator + fileName);

			ASTParser parser = ASTParser.newParser(AST.JLS9);
			parser.setSource(str.toCharArray());

			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setUnitName(project + File.separator + fileName);
			parser.setEnvironment(_classpath, new String[] { filePath }, new String[] { "UTF8" }, true);

			Map<String, String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(_javaVersion, options);
			parser.setCompilerOptions(options);

			final CompilationUnit compilation = (CompilationUnit) parser.createAST(null);
			
			compilation.accept(new MethodInvocationVisitor(graph));

		} catch(IOException ioe) {
			throw new LocalException("Error parsing file '" + fileName + "'");
		}
	}
	
	/**
	 * Parses a file to retrieve information about comments.
	 * @param projectPath Project path.
	 * @param project Project name.
	 * @param filePath File path.
	 * @param fileName File name.
	 * @param tree Tree for setting nodes with packages and comments.
	 * @throws LocalException If errors occurs.
	 */
	public void parseComment(String projectPath, String project, String filePath, String fileName, CommentTree tree) throws LocalException {

		Utils.print("Parsing file: " + fileName);

		try {
			String str = FileManager.readFileToString(filePath + File.separator + fileName);

			ASTParser parser = ASTParser.newParser(AST.JLS9);
			parser.setSource(str.toCharArray());

			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setUnitName(project + File.separator + fileName);
			parser.setEnvironment(_classpath, new String[] { filePath }, new String[] { "UTF8" }, true);

			Map<String, String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(_javaVersion, options);
			parser.setCompilerOptions(options);

			CompilationUnit compilation = (CompilationUnit) parser.createAST(null);
			List<Comment> comments = compilation.getCommentList();
			
			Vector<ASTNode> vector = new Vector<ASTNode>();
			vector.setSize(1);
			compilation.accept(new ElementVisitor(vector));

			for(Comment comment : comments)
				comment.accept(new CommentVisitor(compilation, str, vector, tree, filePath + File.separator + fileName));
			
		} catch(IOException ioe) {
			throw new LocalException("Error parsing file '" + fileName + "'");
		}
	}
	
	/**
	 * Shows application classpath.
	 */
	public void print() {
		Utils.print("Classpath.");
		Utils.print(getClasspath(true));
	}
}
