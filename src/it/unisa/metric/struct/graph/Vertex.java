package it.unisa.metric.struct.graph;

import org.eclipse.jdt.core.dom.Modifier;
/**
 * Interface for graph vertices.
 * Contains bit masks for java modifiers for methods.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public interface Vertex {
	
	/**
	 * Bit mask for <code>public</code> modifier.
	 */
	public static final int PUBLIC = Modifier.PUBLIC;
	/**
	 * Bit mask for <code>private</code> modifier.
	 */
	public static final int PRIVATE = Modifier.PRIVATE;
	/**
	 * Bit mask for <code>protected</code> modifier.
	 */
	public static final int PROTECTED = Modifier.PROTECTED;
	/**
	 * Bit mask for <code>static</code> modifier.
	 */
	public static final int STATIC = Modifier.STATIC;
	/**
	 * Bit mask for <code>final</code> modifier.
	 */
	public static final int FINAL = Modifier.FINAL;
	/**
	 * Bit mask for <code>abstract</code> modifier.
	 */
	public static final int ABSTRACT = Modifier.ABSTRACT;
	/**
	 * Bit mask for <code>synchronized</code> modifier.
	 */
	public static final int SYNCHRONIZED = Modifier.SYNCHRONIZED;
	/**
	 * Bit mask for <code>native</code> modifier.
	 */
	public static final int NATIVE = Modifier.NATIVE;
	/**
	 * Bit mask for <code>strictfp</code> modifier.
	 */
	public static final int STRICTFP = Modifier.STRICTFP;
	
}
