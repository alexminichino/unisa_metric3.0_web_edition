package it.unisa.metric.struct.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultTreeModel;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
/**
 * Represent a comment tree.
 * @author Andrea d'Argenio, Alexander Minichino
 * @version 2.0
 * @since 1.0
 *
 */
public class CommentTree extends DefaultTreeModel {

	private static final long serialVersionUID = -5408824147062550725L;

	/**
	 * Creates a CommentTree from a Node.
	 * @param root Node that represent the project.
	 * @see ProjectNode
	 */
	public CommentTree(Node root) {
		super(root, true);
	}
	
	/**
	 * Invoked to correctly create e CommentTree.
	 * @return A CommentTree.
	 * @param ProjectName a project name
	 */
	public static CommentTree createTree(String ProjectName) {
		return new CommentTree(new ProjectNode(ProjectName));
	}
	
	/**
	 * Insert a Node in the right place in the tree.
	 * If dependences are missing, this method creates the entire node-path for the correct insert.
	 * Is not possible to insert project node or package node directly. If you insert a node that should be go
	 * under a package node, this method automatically creates package node for you.
	 * @param node Node to insert.
	 * @return <code>True</code> if insert goes well; <code>false</code> otherwise.
	 */
	public boolean insertNode(Node node) {
		// project node and package node are not directly inserted in the tree.
		if(node.getType() == Node.PROJECT || node.getType() == Node.PACKAGE)
			return false;
		
		CommentNode commentNode = (CommentNode) node;
		// if the object commented is already in the tree, we have to add the comment to an existing node.
		
		CompilationUnit cu = (CompilationUnit) commentNode.getObjectCommented().getRoot();
		PackageDeclaration pd = cu.getPackage();
		String[] packagePath = new String[0];
		Node pn = (Node)root;
		if(pd != null ) {
			// creates a node-path for package.
			packagePath = pd.getName().toString().split("\\.");
			checkPackage((Node) root, packagePath, 0);
			pn = getPackage((Node) root, packagePath, 0);
		}
		// if it is a class node, insert the node under the right package node.
		if(commentNode.getType() == Node.CLASS) {
			//if it is already present
			Node oldNode = findNode(commentNode.getObjectCommented());
			if(oldNode != null && ((CommentNode) oldNode).getCommentList().size() == 0) {
				replaceNode(oldNode, node);
			}
			else {
				pn.insertChild(node);
				node.setParent(pn);
			}
		// if it is a field node or a method node, insert the node under the right class node.
		} 
		else if(commentNode.getType() == Node.FIELD || commentNode.getType() == Node.METHOD  || commentNode.getType() == Node.ENUM) {
			int typeParent = commentNode.getObjectCommented().getParent().getNodeType();
			
			if(commentNode.getType() == Node.ENUM && typeParent == ASTNode.COMPILATION_UNIT) {
				pn.insertChild(node);
				node.setParent(pn);
			}
			else {
				ASTNode tmp = commentNode.getObjectCommented();
				while(!((tmp = tmp.getParent()) instanceof AbstractTypeDeclaration));
				AbstractTypeDeclaration td = (AbstractTypeDeclaration) tmp;
				if(containsNode(td)) {
					CommentNode tdNode = (CommentNode) findNode(td);
					tdNode.insertChild(node);
					node.setParent(tdNode);
				} else {
					CommentNode tdNode = new CommentNode(td);
					pn.insertChild(tdNode);
					tdNode.setParent(pn);
					tdNode.insertChild(node);
					node.setParent(tdNode);
				}
			}
		// if it is another type of node, insert the node under the right class/method node.
		} 
		else {
			ASTNode tmp = commentNode.getObjectCommented();
			while((tmp = tmp.getParent()) != null && !(tmp instanceof MethodDeclaration));
			// if tmp==null -> insert the node under the right class node.
			if(tmp == null){
				AbstractTypeDeclaration td = null;
				List<TypeDeclaration> tmpList = new ArrayList<TypeDeclaration>();
				cu.accept(new ASTVisitor() {
					public boolean visit(TypeDeclaration node) {
						if(tmpList.isEmpty()) {
							tmpList.add(node);
							return false;
						}
						return true;
					}
				});
				if(!tmpList.isEmpty()) {
					td = tmpList.get(0);
				}
				//Enum visit
				else {
					List<EnumDeclaration> tmpList2 = new ArrayList<EnumDeclaration>();
					cu.accept(new ASTVisitor() {
						public boolean visit(EnumDeclaration node) {
							if(tmpList.isEmpty()) {
								tmpList2.add(node);
								return false;
							}
							return true;
						}
					});
					td = tmpList2.get(0);
				}				
				if(containsNode(td)) {
					CommentNode tdNode = (CommentNode) findNode(td);
					tdNode.insertChild(node);
					node.setParent(tdNode);
				} else {
					CommentNode tdNode = new CommentNode(td);
					pn.insertChild(tdNode);
					tdNode.setParent(pn);

					tdNode.insertChild(node);
					node.setParent(tdNode);
				}
				return true;
			}
			// if tmp!=null -> insert the node under the right method node.
			MethodDeclaration md = (MethodDeclaration) tmp;
			while(!((tmp = tmp.getParent()) instanceof TypeDeclaration));
			TypeDeclaration td = (TypeDeclaration) tmp;
			if(containsNode(td)) {
				if(containsNode(md)) {
					CommentNode mdNode = (CommentNode) findNode(md);
					mdNode.insertChild(node);
					node.setParent(mdNode);
				} else {
					CommentNode tdNode = (CommentNode) findNode(td);
					CommentNode mdNode = new CommentNode(md);
					tdNode.insertChild(mdNode);
					mdNode.setParent(tdNode);
					
					mdNode.insertChild(node);
					node.setParent(mdNode);
				}
			} else {
				CommentNode tdNode = new CommentNode(td);
				pn.insertChild(tdNode);
				tdNode.setParent(pn);
				
				CommentNode mdNode = new CommentNode(md);
				tdNode.insertChild(mdNode);
				mdNode.setParent(tdNode);
				
				mdNode.insertChild(node);
				node.setParent(mdNode);
			}
		}
		return true;
	}
	
	/**
	 * Creates the node-package-path if package nodes are missing.
	 * @param root Root of the tree.
	 * @param path Package path.
	 * @param index Recursive index (always <code>0</code> as input).
	 */
	private void checkPackage(Node root, String[] path, int index) {
		if(index < path.length && (root.getType() == Node.PROJECT || root.getType() == Node.PACKAGE)) {
			Enumeration<Node> children = root.children();
			boolean found = false;
			while(!found && children.hasMoreElements()) {
				Node child = children.nextElement();
				if(child.getType() == Node.PACKAGE && ((PackageNode)child).getPackageName().compareTo(path[index]) == 0) {
					checkPackage(child, path, index + 1);
					found = true;
				}
			}
			if(!found) {
				PackageNode newNode = new PackageNode(path[index]);
				root.insertChild(newNode);
				newNode.setParent(root);
				checkPackage(newNode, path, index + 1);
			}
		}
	}
	
	/**
	 * Gets the right package node.
	 * @param root Root of the tree.
	 * @param path Package path.
	 * @param index Recursive index (always <code>0</code> as input).
	 * @return Package node.
	 */
	private PackageNode getPackage(Node root, String[] path, int index) {
		if((root.getType() == Node.PACKAGE) && (index == path.length))
			return (PackageNode) root;
		Enumeration<Node> children = root.children();
		while(children.hasMoreElements()) {
			Node child = children.nextElement();
			if(child.getType() == Node.PACKAGE && ((PackageNode)child).getPackageName().compareTo(path[index]) == 0)
				return getPackage(child, path, index + 1);
		}
		return null;
	}
	
	/**
	 * Finds a CommentNode from its object commented.
	 * @param node AST node that represent the object commented.
	 * @return CommentNode within this object commented; <code>null</code> if this object commented is not in any node.
	 * @see #getNodeByASTNode(Node, ASTNode)
	 */
	public Node findNode(ASTNode node) {
		return getNodeByASTNode((Node)root, node);
	}
	
	/**
	 * Checks if a node within this AST node is there.
	 * @param node AST node that represent the object commented.
	 * @return <code>True</code> if there is a node within this object commented; <code>false</code> otherwise.
	 */
	public boolean containsNode(ASTNode node) {
		if(getNodeByASTNode((Node)root, node) == null)
			return false;
		return true;
	}
	
	/**
	 * Checks if a node within this AST node is there.
	 * @param root Root of the tree.
	 * @param object AST node that represent the object commented.
	 * @return <code>True</code> if there is a node within this object commented; <code>false</code> otherwise.
	 * @see #findNode(ASTNode)
	 */
	private CommentNode getNodeByASTNode(Node root, ASTNode object) {
		CommentNode match = null;
		if((root.getType() != Node.PROJECT && root.getType() != Node.PACKAGE) && ((CommentNode)root).getObjectCommented().equals(object))
			return (CommentNode)root;
		if(!root.isLeaf()) {
			Enumeration<Node> e = root.children();
			while(e.hasMoreElements() && match == null)
				match = getNodeByASTNode(e.nextElement(), object);
		}
		return match;
	}
	
	/**
	 * Replaces a node with a new one
	 * @param oldNode node to replace
	 * @param newNode substitute node
	 */
	private void replaceNode(Node oldNode, Node newNode) {
		Node parent = (Node) oldNode.getParent();
		Enumeration<Node> e = oldNode.children();
		while(e.hasMoreElements()) {
			Node child = e.nextElement();
			newNode.insertChild(child);
			child.setParent(newNode);
		}
		parent.children.remove(oldNode);
		parent.insertChild(newNode);
		newNode.setParent(parent);
	}
	
	/**
	 * Gets a string representation of a CommentTree.
	 * @return Tree as string.
	 * @see #treeToString(Node, int, StringBuilder)
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		treeToString((Node) root, 0, sb);
		
		return sb.toString();
	}
	
	/**
	 * Fill in the StringBuilder with a string representation of a CommentTree.
	 * @param root Root of the tree.
	 * @param depth Recursive index (always <code>0</code> as input).
	 * @param sb StringBuilder to fill in.
	 */
	private void treeToString(Node root, int depth, StringBuilder sb) {
		sb.append(parentToString(root, depth));
		if(root.getType() == Node.PROJECT)
			sb.append("[0] ROOT");
		else {
			sb.append("---[" + depth + "]NODE-----\n");
			if(root.getType() == Node.PACKAGE)
				sb.append(((PackageNode) root).getPackageName());
			else {
				List<String> comments = ((CommentNode) root).getCommentList();
				if(!comments.isEmpty()) {
					sb.append("Comments:\n");
					for(String c : comments)
						sb.append(c + "\n");
				}
				sb.append("Node:\n\t" + nodeToString((CommentNode) root));
			}
		}
		sb.append("\n-----------------------------------\n");
		Enumeration<Node> children = root.children();
		while(children.hasMoreElements())
			treeToString(children.nextElement(), depth + 1, sb);
	}
	
	/**
	 * Gets a string representation of a parent of a node.
	 * @param node Node of the tree.
	 * @param depth Depth of node.
	 * @return Parent of this node as string.
	 * @see #treeToString(Node, int, StringBuilder)
	 */
	private String parentToString(Node node, int depth) {
		if(depth == 0)
			return "";
		String s = "---[" + (depth - 1) + "]PARENT---\n";
		Node parent = (Node) node.getParent();
		if(parent.getType() == Node.PROJECT)
			s += "ROOT";
		else if(parent.getType() == Node.PACKAGE)
			s += ((PackageNode) parent).getPackageName();
		else {
			List<String> comments = ((CommentNode) parent).getCommentList();
			if(!comments.isEmpty()) {
				s += "Comments:\n";
				for(String c : comments)
					s += c + "\n";
			}
			s += "Node:\n\t" + nodeToString((CommentNode)parent);
		}
		return s + "\n";
	}
	
	/**
	 * Gets a string representation of a node.
	 * @param node Node of the tree.
	 * @return Node as string.
	 * @see #treeToString(Node, int, StringBuilder)
	 */
	private String nodeToString(CommentNode node) {
		ASTNode object = node.getObjectCommented();
		if(node.getType() == Node.CLASS || node.getType() == Node.FIELD || node.getType() == Node.METHOD) {
			if(object instanceof TypeDeclaration) {
				TypeDeclaration c = (TypeDeclaration) object;
				if(c.getJavadoc() != null)
					c.setJavadoc(null);
			} else if(object instanceof FieldDeclaration) {
				FieldDeclaration c = (FieldDeclaration) object;
				if(c.getJavadoc() != null)
					c.setJavadoc(null);
			} else if(object instanceof MethodDeclaration) {
				MethodDeclaration c = (MethodDeclaration) object;
				if(c.getJavadoc() != null)
					c.setJavadoc(null);
			}
		}
		if(object.toString().indexOf('\n') != -1)
			return object.toString().substring(0, object.toString().indexOf('\n'));
		else
			return object.toString();
	}
}
