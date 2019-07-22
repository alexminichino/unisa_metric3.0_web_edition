package it.unisa.metric.struct.tree;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;
/**
 * Represents a general CommentTree node.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public abstract class Node implements TreeNode {
	
	/**
	 * Node type value for project node.
	 */
	public static final int PROJECT = 0;
	/**
	 * Node type value for package nodes.
	 */
	public static final int PACKAGE = 1;
	/**
	 * Node type value for class nodes.
	 */
	public static final int CLASS = 2;
	/**
	 * Node type value for field nodes.
	 */
	public static final int FIELD = 3;
	/**
	 * Node type value for method nodes.
	 */
	public static final int METHOD = 4;
	/**
	 * Node type value for enum nodes.
	 */
	public static final int ENUM = 5;
	/**
	 * Node type values for other nodes.
	 */
	public static final int OTHER = 6;
	
	/**
	 * List of children nodes.
	 */
	protected List<Node> children;
	/**
	 * Parent node.
	 */
	protected Node parent;
	/**
	 * Node type.
	 */
	protected int type;
	
	/**
	 * Represents the information in the node.
	 * @author Andrea d'Argenio
	 * @version 1.0
	 * @since 1.0
	 *
	 */
	protected interface Data {}
	
	/**
	 * Adds a child to this node.
	 * @param node Node to add as child.
	 */
	protected void insertChild(Node node) {
		children.add(node);
	}
	
	/**
	 * Adds the parent to this node.
	 * @param parent Node to add as parent.
	 */
	protected void setParent(Node parent) {
		this.parent = parent;
	}
	
	/**
	 * Gets node type.
	 * @return Node type.
	 * @see Node
	 */
	public int getType() {
		return type;
	}
	
	@Override
	public Enumeration<Node> children() {
		return Collections.enumeration(children);
	}

	@Override
	public TreeNode getChildAt(int index) {
		try {
			return children.get(index);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		for(int index = 0; index < children.size(); index++)
			if(children.get(index).equals(node))
				return index;
		return -1;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	@Override
	public boolean getAllowsChildren() {
		if(type == FIELD || type == OTHER)
			return false;
		return true;
	}

}
