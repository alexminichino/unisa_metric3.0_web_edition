package it.unisa.metric.web;

import java.util.ArrayList;
import java.util.List;

public class SimpleCommentNode {
	private String name;
	private String nodeType;
	private ArrayList<String> comments;
	private ArrayList<SimpleCommentNode> children;
	private transient SimpleCommentNode parent;
	
	
	public SimpleCommentNode(String name, String nodeType, SimpleCommentNode parent, List<String> comments) {
		this.name = name;
		this.nodeType = nodeType;
		this.parent = parent;
		this.children = new ArrayList<>();
		this.comments= new ArrayList<>(comments);
	}
	
	public void addChild(SimpleCommentNode node) {
		children.add(node);
	}
	
}
