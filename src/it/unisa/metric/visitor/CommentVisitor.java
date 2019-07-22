package it.unisa.metric.visitor;


import java.util.Vector;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;

import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

import it.unisa.metric.struct.tree.CommentNode;
import it.unisa.metric.struct.tree.CommentTree;
/**
 * Represents an ASTVisitor that takes information about comments.
 * Checks LineComment, BlockComment and Javadoc, assign them to the right code element
 * and put all in a comment tree.
 * @author Andrea d'Argenio, Alexander Minichino
 * @version 2.0
 * @since 1.0
 *
 */
public class CommentVisitor extends ASTVisitor {

	/**
	 * A vector of code elements.
	 * Every vector index represent a line of code.
	 * Example:
	 * 1: package it.unisa.metric; 
	 * 2: import java.util.Vector; 
	 * 3: 
	 * 4: public class MyClass { 
	 * 5:    public static void main(String[] args) { 
	 * 6:       // do something 
	 * 7:    } 
	 * 8: }
	 * 
	  * Resulting Vector:
	 * Vector.get(0) = null
	 * Vector.get(1) = PackageDeclaration
	 * Vector.get(2) = ImportDeclaration
	 * Vector.get(3) = null
	 * Vector.get(4) = TypeDeclaration
	 * Vector.get(5) = MethodDeclaration
	 * Vector.get(6) = null
	 * Vector.get(7) = null
	 * Vector.get(8) = null
	 */
	Vector<ASTNode> vector;
	/**
	 * Comment tree to fill in.
	 */
	CommentTree tree;
	/**
	 * AST root.
	 */
	CompilationUnit cu;
	/**
	 * Source file as string.
	 */
	String file;
	/**
	 * File path as string.
	 */
	String filePath;

	/**
	 * Creates a CommentVisitor from a CompilationUnit, a String, a Vector and a CommentTree.
	 * @param cu CompilationUnit (AST root).
	 * @param file Source file as string.
	 * @param vector Code element vector.
	 * @param tree Comment tree.
	 */
	public CommentVisitor(CompilationUnit cu, String file, Vector<ASTNode> vector, CommentTree tree, String filePath) {
		this.cu = cu;
		this.vector = vector;
		this.tree = tree;
		this.file = file;
		this.filePath = filePath;
	}

	/**
	 * Visits LineComment nodes.
	 */
	public boolean visit(LineComment node) {

		int line = cu.getLineNumber(node.getStartPosition());
		int i;
		String comment = file.substring(node.getStartPosition(), node.getStartPosition() + node.getLength());
		for(i = line; i < vector.size() && vector.get(i) == null; i++);
		ASTNode object;
		if(i < vector.size())
			object = vector.get(i);
		else
			object = vector.get(vector.size() - 1);
		int distanceFromParent = getDistanceFromParent(object, node);
		tree.insertNode(new CommentNode(object, comment, Math.abs(distanceFromParent), getHorizontalDistanceFromParent(object, node), getRealDistanceFromParent(object, node, distanceFromParent), cu.getLineNumber(object.getStartPosition()), filePath));

		return true;
	}

	/**
	 * Visits BlockComment nodes.
	 */
	public boolean visit(BlockComment node) {

		int line = cu.getLineNumber(node.getStartPosition());
		int i;
		String comment = file.substring(node.getStartPosition(), node.getStartPosition() + node.getLength());
		for(i = line; i < vector.size() && vector.get(i) == null; i++);
		ASTNode object;
		if(i < vector.size())
			object = vector.get(i);
		else
			object = vector.get(vector.size() - 1);
		int distanceFromParent = getDistanceFromParent(object, node);
		tree.insertNode(new CommentNode(object, comment,  Math.abs(distanceFromParent), getHorizontalDistanceFromParent(object, node), getRealDistanceFromParent(object, node, distanceFromParent), cu.getLineNumber(object.getStartPosition()), filePath));

		return true;
	}

	/**
	 * Visits Javadoc nodes.
	 */
	public boolean visit(Javadoc node) {

		int line = cu.getLineNumber(node.getStartPosition());
		int i;
		String comment = file.substring(node.getStartPosition(), node.getStartPosition() + node.getLength());
		for(i = line; i < vector.size() && vector.get(i) == null; i++);
		ASTNode object;
		if(i < vector.size())
			object = vector.get(i);
		else
			object = vector.get(vector.size() - 1);
		
		fixNodePosition(object);
		int distanceFromParent = getDistanceFromParent(object, node);
		tree.insertNode(new CommentNode(object, comment,  Math.abs(distanceFromParent), getHorizontalDistanceFromParent(object, node),	getRealDistanceFromParent(object, node, distanceFromParent), cu.getLineNumber(object.getStartPosition()), filePath));
		return true;
	}

	/**
	 * Gets difference between row of parent and row of comment
	 * 
	 * @param node    parent of comment
	 * @param comment the comment (superclass) node
	 */
	private int getDistanceFromParent(ASTNode node, Comment comment) {
		int endLineNumber = cu.getLineNumber(comment.getStartPosition() + comment.getLength());
		int c= cu.getLineNumber(node.getStartPosition()) - endLineNumber;
		if( c==-1) 
			System.err.println();
		return c;
	}

	/**
	 * Gets difference between row of parent and row of comment
	 * 
	 * @param node    parent of comment
	 * @param comment the comment (superclass) node
	 */
	private int getDistanceFromParent(ASTNode node, Javadoc comment) {
		String n = file.substring(node.getStartPosition(), node.getStartPosition() + node.getLength());
		int endLineNumber = cu.getLineNumber(comment.getStartPosition() + comment.getLength());
		return cu.getLineNumber(node.getStartPosition()) - endLineNumber;
	}
	
	/**
	 * Gets difference between column of parent and column of comment
	 * 
	 * @param node    parent of comment
	 * @param comment the comment (superclass) node
	 */
	private int getHorizontalDistanceFromParent(ASTNode node, Comment comment) {
		int startColumn = cu.getColumnNumber(comment.getStartPosition());
		return Math.abs(cu.getColumnNumber(node.getStartPosition()) - startColumn);
	}

	/**
	 * Gets number of chars (blank) from comment and parent
	 * 
	 * @param node               parent of comment
	 * @param comment            the comment (superclass) node
	 * @param distanceFromParent difference between row of parent and row of comment
	 */
	private int getRealDistanceFromParent(ASTNode node, Comment comment, int distanceFromParent) {
		if (distanceFromParent <= 0)
			return getInlineOrInternalDistanceFromParent(node, comment);
		String stringComment = file.substring(comment.getStartPosition(),
				comment.getStartPosition() + comment.getLength());
		int l = file.substring(comment.getStartPosition() + comment.getLength() - rightWiteSpaces(stringComment),
				node.getStartPosition()).length();
		return l;
	}

	/**
	 * Gets the number of characters (empty) from comment and parent on the same line or from comment within parent
	 * 
	 * @param node               parent of comment
	 * @param comment            the comment (superclass) node
	 * @param distanceFromParent difference between row of parent and row of comment
	 */
	private int getInlineOrInternalDistanceFromParent(ASTNode node, Comment comment) {
		String nodeString = file.substring(node.getStartPosition() ,node.getStartPosition()+node.getLength());
		String commentString = file.substring(comment.getStartPosition(), comment.getStartPosition() + comment.getLength());
		int startPosition = node.getStartPosition() + node.getLength();
		int endPosition = comment.getStartPosition();
		if (startPosition <= endPosition){
			return file.substring(startPosition, endPosition).length();
		}
		else if(nodeString.contains(commentString)) {
			int i = nodeString.indexOf(commentString);
			while (i >= 0 && Character.isWhitespace(nodeString.charAt(i-1))) {
				i--;
			}
			return nodeString.substring(i, nodeString.indexOf(commentString)).length();
		}
		else
			return 0;
		
	}

	/**
	 * Gets a string without withespace on te right
	 * @param s string to trim
	 */
	private String rTrim(String s) {
		int i = s.length() - 1;
		while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
			i--;
		}

		return s.substring(0, i + 1);
	}

	/**
	 * Gets a number of right withespace
	 * @param s string
	 */
	private int rightWiteSpaces(String s) {
		return s.length() - rTrim(s).length();
	}
	

	/**
	 * Shift node start position to after Javadoc
	 * @param node node to fix
	 */
	protected void fixNodePosition(final ASTNode node) {
		Javadoc javaDoc = null ;
		final int startPosition = node.getStartPosition();
		final int endPosition = startPosition + node.getLength();
		String nodeString = file.substring(startPosition, endPosition);
		
		if(node instanceof BodyDeclaration) {
			javaDoc = ((BodyDeclaration) node).getJavadoc();
			((BodyDeclaration) node).setJavadoc(null);
		}
		else if(node instanceof PackageDeclaration) {
			javaDoc = ((PackageDeclaration) node).getJavadoc();
			((PackageDeclaration) node).setJavadoc(null);
		}
		else if(node instanceof TypeDeclarationStatement) {
			javaDoc = ((TypeDeclarationStatement) node).getDeclaration().getJavadoc();
			((TypeDeclarationStatement) node).getDeclaration().setJavadoc(null);
		}
		// Check if javadoc present
		if ( javaDoc != null) {
			// Find location of Javadoc end */
			//Search the substring starting at the end of the comment and add the offset 
			int javaDocEndIndex = nodeString.substring(javaDoc.getLength(), node.getLength()).indexOf(node.toString().split(" ")[0]) + javaDoc.getLength();
			// Shift node start to next line
			node.setSourceRange(startPosition + javaDocEndIndex, endPosition - startPosition - javaDocEndIndex);
		}
	}
}
