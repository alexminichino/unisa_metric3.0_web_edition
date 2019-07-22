package it.unisa.metric.struct.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import it.unisa.metric.nlp.Lemmatizer;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
/**
 * Represents a node with comments in the comment tree.
 * @author Andrea d'Argenio, Alexander Minichino
 * @version 2.0
 * @since 1.0
 *
 */
public class CommentNode extends Node {

	/**
	 * Represent the data in the node.
	 */
	private CommentNodeData data;
	
	/**
	 * 
	 */
	private int vericalDistanceFromParent;
	private int horizontalDistanceFromParent;
	private int realDistanceFromParent;
	private int parentLineNumber;
	public String fileName;
	public String filePath;
	
	
	/**
	 * Creates a CommentNode from a ASTNode and a String.
	 * @param objectCommented AST node that represent the object commented.
	 * @param comment Comment text.
	 * @param realDistanceFromParent 
	 */
	public CommentNode(ASTNode objectCommented, String comment, int vericalDistanceFromParent, int horizontalDistanceFromParent, int realDistanceFromParent, int parentLineNumber, String filePath) {
		this(objectCommented);
		this.vericalDistanceFromParent = vericalDistanceFromParent;
		this.realDistanceFromParent = realDistanceFromParent;
		this.horizontalDistanceFromParent = horizontalDistanceFromParent;
		data.addComment(comment);
		this.parentLineNumber = parentLineNumber;
		this.filePath = filePath;
	}
	
	/**
	 * Creates a CommentNode from a ASTNode.
	 * @param objectCommented AST node that represent the object commented.
	 */
	public CommentNode(ASTNode objectCommented) {
		type = assignType(objectCommented);
		children = new ArrayList<Node>();
		data = new CommentNodeData(objectCommented);
	}
	
	/**
	 * Assigns the right type to {@link Node#type}.
	 * @param node AST node that represent the object commented.
	 * @return CommentNode type.
	 * @see Node
	 */
	private int assignType(ASTNode node) {
		if(node instanceof TypeDeclaration)
			return CLASS;
		if(node instanceof FieldDeclaration)
			return FIELD;
		if(node instanceof MethodDeclaration)
			return METHOD;
		if(node instanceof EnumDeclaration)
			return ENUM;
		return OTHER;
	}
	
	/**
	 * Adds a comment in {@link #data}'s comment list.
	 * @param comment Comment to add.
	 * @see CommentNodeData
	 */
	public void addComment(String comment) {
		data.addComment(comment);
	}
	
	/**
	 * Gets the object commented.
	 * @return AST node that represent the object commented.
	 */
	public ASTNode getObjectCommented() {
		return data.getObjectCommented();
	}
	
	/**
	 * Gets the comment list.
	 * @return Comment list.
	 */
	public List<String> getCommentList() {
		return data.getCommentList();
	}
	
	/**
	 * Gets vertical distance from parent node.
	 * @return distance from parent.
	 */
	public int getVerticalDistanceFromParent() {
		return vericalDistanceFromParent;
	}

	/**
	 * Gets horizontal distance from parent node.
	 * @return distance from parent.
	 */
	public int getHorizontalDistanceFromParent() {
		return horizontalDistanceFromParent;
	}

	
	/**
	 * Gets real distance from parent node.
	 * @return distance from parent.
	 */
	public int getRealDistanceFromParent() {
		return realDistanceFromParent;
	}

	/**
	 * Stamming with lemma.
	 * @return map with lemma and number of occurrences of it
	 */
	private Map<String,Integer> createLemmaMap(String comment){
		Map<String, Integer> map = new HashMap<String, Integer>();

		Lemmatizer leemmatizer = new Lemmatizer();
		for(String lemma : leemmatizer.lemmatize(comment.replaceAll("[^A-Z a-z 0-9 ]", ""))){
			if(map.containsKey(lemma)){
				map.replace(lemma, map.get(lemma)+1);
			}
			else{
				map.put(lemma, 1);
			}
		}
		for (Map.Entry<String, Integer> entry : map.entrySet()){
			System.out.println(entry.getKey()+" = "+entry.getValue());
		}
		return map;
	}

	/**
	 * Gets the number of used words
	 * @return number of used words
	 */
	public int getNumberOfWords(){
		int usedWordsMapSize = (data.getCommentList().size()>0)? createLemmaMap(data.getCommentList().get(0)).size() : 0 ;
		return usedWordsMapSize;
	}

	/**
	 * Gets the used words map.
	 * @return map with lemma and number of occurrences of it
	 */
	public Map<String, Integer> getusedWordsMap(){
		return createLemmaMap(data.getCommentList().get(0));
	}

	/**
	 * Gets the line number where the parent is defined.
	 * @return parent line number
	 */
	public int getParentLineNumber() {
		return parentLineNumber;
	}

	/**
	 * Gets the file path where it is defined.
	 * @return file path
	 */
	public String getFilePath() {
		return filePath;
	}

	@Override
	public String toString() {
		return "CommentNode [data=" + data + ", vericalDistanceFromParent=" + vericalDistanceFromParent
				+ ", horizontalDistanceFromParent=" + horizontalDistanceFromParent + ", realDistanceFromParent="
				+ realDistanceFromParent + ", filePath=" + filePath + "]";
	}
	
}
