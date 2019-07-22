package it.unisa.metric.struct.tree;

import java.util.ArrayList;
/**
 * Represent a package node.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class PackageNode extends Node {

	/**
	 * Data in package nodes.
	 */
	private PackageNodeData data;
	
	/**
	 * Creates a PackageNode from a package name.
	 * @param packageName Package name.
	 */
	public PackageNode(String packageName) {
		children = new ArrayList<Node>();
		type = PACKAGE;
		data = new PackageNodeData(packageName);
	}
	
	/**
	 * Gets package name.
	 * @return Package name.
	 */
	public String getPackageName() {
		return data.getPackageName();
	}
	
}