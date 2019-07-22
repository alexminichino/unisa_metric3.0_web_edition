package it.unisa.metric.metrics;

import javax.swing.tree.TreeNode;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.PackageInfo;
import it.unisa.metric.struct.tree.CommentNode;
import it.unisa.metric.struct.tree.CommentTree;
import it.unisa.metric.struct.tree.Node;

public class NC {
	public int ncClass(ClassInfo classInf, CommentTree tree){		
		if(classInf.getClassASTNode().getNodeType()==ASTNode.TYPE_DECLARATION){
			TypeDeclaration classNode = (TypeDeclaration) classInf.getClassASTNode();
			return visitProject((TreeNode)tree.getRoot(), classNode);
			//return visit((TreeNode)tree.getRoot(), classNode);
		}else{
			//il nodo dell'ast non è una classe
			return 0;
		}
	}
	
	public int ncPackage(PackageInfo packInf, CommentTree tree){
		int nc = 0;
		for(ClassInfo classInf : packInf.getClasses()){
			nc = nc + ncClass(classInf, tree);
		}
		return nc;
	}
	
	private int visitProject(TreeNode treeNode, TypeDeclaration classNode){
		int count = 0;
		//visit each pacakges of the project
		for(int i=0; i<treeNode.getChildCount(); i++){
			//visit each package directly linked to the project
			count = count + visitPackage(treeNode.getChildAt(i), classNode);
		}
		return count;
	}
	
	private int visitPackage(TreeNode treeNode, TypeDeclaration classNode){
		int count = 0;
		//check all class untill he finds the one he nened
		for(int i=0; i<treeNode.getChildCount(); i++){
			Node node = (Node) treeNode.getChildAt(i);
			if(node.getType() == 2){
				//we found a class node
				CommentNode commentNode = (CommentNode) node;
				TypeDeclaration objCommentTypeDec = (TypeDeclaration) commentNode.getObjectCommented();
				if(classNode.getName().toString().equals(objCommentTypeDec.getName().toString())){
					//class found here now he's gonna count all the comments in the class subtree
					count = visitClassElements(treeNode.getChildAt(i), classNode);
					break;
				}
			}else if(node.getType() == 1){
				//we found a package node
				count = count + visitPackage(treeNode.getChildAt(i), classNode);
			}
		}
		
		return count;
	}
	
	private int visitClassElements(TreeNode treeNode, TypeDeclaration classNode){
		//i want to count evrything from this node 
		CommentNode commentNode = (CommentNode) treeNode;
		int count = commentNode.getCommentList().size();
		for(int i=0; i<treeNode.getChildCount(); i++){
			//commentNode.getChildAt(i);
			count = count + visitClassElements(treeNode.getChildAt(i), classNode);
		}
		
		return count;
	}
}