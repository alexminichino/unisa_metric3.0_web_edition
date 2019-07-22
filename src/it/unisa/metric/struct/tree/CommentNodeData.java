package it.unisa.metric.struct.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
/**
 * Represent the information in a {@link CommentNode}.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
class CommentNodeData implements Node.Data {

	/**
	 * AST node that represent the object commented.
	 */
	private ASTNode objectCommented;
	/**
	 * Comment list.
	 */
	private List<String> comments;
	
	/**
	 * Creates a CommentNodeData from an ASTNode and a String.
	 * @param objectCommented AST node that represent the object commented.
	 * @param comment Comment text.
	 */
	protected CommentNodeData(ASTNode objectCommented, String comment) {
		this(objectCommented);
		comments.add(comment);
	}
	
	/**
	 * Creates a CommentNodeData from an ASTNode.
	 * @param objectCommented AST node that represent the object commented.
	 */
	protected CommentNodeData(ASTNode objectCommented) {
		this.objectCommented = objectCommented;
		comments = new ArrayList<String>();
	}
	
	/**
	 * Gets the object commented.
	 * @return AST node that represent the object commented.
	 */
	public ASTNode getObjectCommented() {
		return objectCommented;
	}
	
	/**
	 * Gets the comment list.
	 * @return Comment list.
	 */
	public List<String> getCommentList() {
		return comments;
	}
	
	/**
	 * Adds a comment in the comment list.
	 * @param comment Comment to add.
	 */
	public void addComment(String comment) {
		comments.add(comment);
	}
}
