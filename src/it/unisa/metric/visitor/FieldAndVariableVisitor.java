package it.unisa.metric.visitor;

import it.unisa.metric.struct.table.FieldRecord;
import it.unisa.metric.struct.table.Record;
import it.unisa.metric.struct.table.VariableRecord;

import java.util.Hashtable;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Represents an ASTVisitor that takes information about fields and variables.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class FieldAndVariableVisitor extends ASTVisitor {

	/**
	 * Table of fields and variables to fill in.
	 */
	private Hashtable<Integer, Record> table;
	
	/**
	 * Creates a FieldAndVariableVisitor from an Hashtable.
	 * @param table Table of fields and variables to fill in.
	 */
	public FieldAndVariableVisitor(Hashtable<Integer, Record> table) {
		this.table = table;
	}
	
	/**
	 * Visits FieldDeclaration nodes.
	 */
	public boolean visit(FieldDeclaration node) {
		for (VariableDeclarationFragment fragment : (Iterable<VariableDeclarationFragment>) node.fragments()) {
			IVariableBinding binding = fragment.resolveBinding();
			//if(binding != null){
				FieldRecord record = new FieldRecord((CompilationUnit) node.getRoot(), fragment, binding);
				table.put(record.hashCode(), record);
			//}
		}
		
		return true;
	}
	
	/**
	 * Visits VariableDeclarationStatement node.
	 */
	public boolean visit(VariableDeclarationStatement node) {
		for (VariableDeclarationFragment fragment : (Iterable<VariableDeclarationFragment>) node.fragments()) {
			IVariableBinding binding = fragment.resolveBinding();
			//if(binding != null){
				VariableRecord record = new VariableRecord((CompilationUnit) node.getRoot(), fragment, binding);
				table.put(record.hashCode(), record);
			//}
		}
		
		return true;
	}
}
