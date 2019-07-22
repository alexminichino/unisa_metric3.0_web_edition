package it.unisa.metric.struct.tree;
/**
 * Represent the information in a {@link PackageNode}.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
class PackageNodeData implements Node.Data {

	/**
	 * Package name.
	 */
	private String packageName;
	
	/**
	 * Creates a PackageNodeData from package name.
	 * @param packageName Package name.
	 */
	protected PackageNodeData(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * Gets package name.
	 * @return Package name.
	 */
	public String getPackageName() {
		return packageName;
	}
	
}
