package it.unisa.metric.struct.tree;

import java.util.ArrayList;
/**
 * Represent a project node.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class ProjectNode extends Node {

	private String projectName;
	/**
	 * Creates a ProjectNode.
	 * @param projectName project name
	 */
	public ProjectNode(String projectName) {
		type = PROJECT;
		children = new ArrayList<Node>();
		this.projectName = projectName;
	}
	
	/**
	 * Gets project name.
	 * @return project name.
	 */
	public String getProjectName() {
		return projectName;
	}
	
}
