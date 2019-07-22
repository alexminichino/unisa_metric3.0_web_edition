package it.unisa.metric.visitor;

import java.util.*;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
/**
 * Represent an ASTVisitor that takes information about code elements.
 * Maps a source file in a vector.
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
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class ElementVisitor extends ASTVisitor {

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
	 * Creates an ElementVisitor from a Vector.
	 * @param vector Vector of code elements.
	 */
	public ElementVisitor(Vector<ASTNode> vector) {
		this.vector = vector;
	}
	
	/**
	 * Visits every node and put them in the vector.
	 */
	public void preVisit(ASTNode node) {
		
		if(!(node instanceof CompilationUnit)) {
			CompilationUnit cu = (CompilationUnit) node.getRoot();
			int line = cu.getLineNumber(node.getStartPosition());
			if(vector.size() <= line)
				vector.setSize(line + 1);
			if(vector.get(line) == null)
				vector.set(line, node);
		}
	}
	
}
