package it.unisa.metric.struct.graph;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
/**
 * Defines the vertices for the application graph that contains information about method invocations chain.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class MethodVertex implements Vertex {

	/**
	 * Method name.
	 */
	private String name;
	/**
	 * Method modifiers.
	 * @see Vertex
	 */
	private int modifiers;
	/**
	 * Method declaration line.
	 */
	private int line;
	/**
	 * Method class binding.
	 */
	private ITypeBinding sourceClass;
	/**
	 * Method return type binding.
	 */
	private ITypeBinding returnType;
	/**
	 * Method parameters binding.
	 */
	private ITypeBinding[] parameters;
	/** 
	 * Method throwed exceptions binding.
	 */
	private ITypeBinding[] exceptions;
	/** 
	 * Ciclomatic complexity of the method.
	 */
	private int complexity = 0;
	/**
	 * Used to check if the method complexity is updated or not.
	 */
	private boolean updated = false;
	/**
	 * Lines of codes.
	 */
	private int loc = 0;
	/**
	 * Object that represent the info about the method class.
	 */
	private ClassInfo classInfo;
	/**
	 * Array List that contains all the field accessed/used in the method.
	 */
	private ArrayList<IVariableBinding> fieldAccessed;
	
	/**
	 * Creates a <code>MethodVertex</code> from <code>CompilationUnit</code>, <code>MethodDeclaration</code> and <code>IMethodBinding</code>.
	 * @param cu CompilationUnit (AST root).
	 * @param declaration Method declaration.
	 * @param binding Method binding.
	 */
	public MethodVertex(CompilationUnit cu, MethodDeclaration declaration, IMethodBinding binding) {
		if(binding!=null){
			name = binding.getName();
			modifiers = binding.getModifiers();
			sourceClass = binding.getDeclaringClass();
			returnType = binding.getReturnType();
			parameters = binding.getParameterTypes();
			exceptions = binding.getExceptionTypes();
			if(declaration != null)
				line = cu.getLineNumber(declaration.getStartPosition());
			else
				line = -1;
		}
	}

	/**
	 * Creates a <code>MethodVertex</code> from <code>CompilationUnit</code> and <code>IMethodBinding</code>.
	 * @param cu CompilationUnit (AST root).
	 * @param binding Method binding.
	 */
	public MethodVertex(CompilationUnit cu, IMethodBinding binding) {
		this(cu, (MethodDeclaration) cu.findDeclaringNode(binding), binding);
	}
	
	/**
	 * Gets method name.
	 * @return Method name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets method modifiers as an integer.
	 * {@link Vertex} contains bit masks for each modifier.
	 * To get method modifiers as string array, see {@link #getModifiersToString()}. 
	 * @return Method modifiers as an integer.
	 * @see Vertex
	 * @see #getModifiersToString()
	 */
	public int getModifiers() {
		return modifiers;
	}

	/**
	 * Gets method declaration line.
	 * @return Method declaration line.
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Gets method source class binding.
	 * @return Method source class binding.
	 */
	public ITypeBinding getSourceClass() {
		return sourceClass;
	}

	/**
	 * Gets method return type binding.
	 * @return Method return type binding.
	 */
	public ITypeBinding getReturnType() {
		return returnType;
	}

	/**
	 * Gets method parameters binding.
	 * @return Method parameters binding.
	 */
	public ITypeBinding[] getParameters() {
		return parameters;
	}

	/**
	 * Gets method throwed exceptions binding.
	 * @return Method throwed exceptions binding.
	 */
	public ITypeBinding[] getExceptions() {
		return exceptions;
	}

	/**
	 * Gets a string array that contains method modifiers.
	 * @return Method modifiers.
	 * @see Vertex
	 * @see #getModifiers()
	 */
	public String[] getModifiersToString() {
		StringBuilder sb = new StringBuilder();

		if(Modifier.isPublic(modifiers))
			sb.append("public ");
		else if(Modifier.isProtected(modifiers))
			sb.append("protected ");
		else if(Modifier.isPrivate(modifiers))
			sb.append("private ");

		if(Modifier.isStatic(modifiers))
			sb.append("static ");

		if(Modifier.isFinal(modifiers))
			sb.append("final ");

		if(Modifier.isAbstract(modifiers))
			sb.append("abstract ");

		if(Modifier.isSynchronized(modifiers))
			sb.append("synchronized ");

		if(Modifier.isNative(modifiers))
			sb.append("native ");

		if(Modifier.isStrictfp(modifiers))
			sb.append("strictfp ");

		if(sb.length() > 0)
			sb.substring(0, sb.length() - 1);

		return sb.toString().split(" ");
	}

	/**
	 * Gets a string representation of a vertex.
	 * @return Vertex as string.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String[] modifiersString = getModifiersToString();
		for(String s : modifiersString)
			if(s.compareTo("") != 0)
				sb.append(s + " ");
		sb.append(returnType.getQualifiedName() + " ");
		if(!sourceClass.isAnonymous())
			sb.append(sourceClass.getQualifiedName() + "." + name + "(");
		else {
			sb.append(sourceClass.getBinaryName());
			if(name.length() == 0)
				sb.append("(");
			else
				sb.append("." + name + "(");
		}
		for(int i = 0; i < parameters.length; i++) {
			sb.append(parameters[i].getQualifiedName());
			if(i < parameters.length - 1)
				sb.append(", ");
		}
		sb.append(")");
		if(exceptions.length > 0)
			sb.append(" throws ");
		for(int i = 0; i < exceptions.length; i++) {
			sb.append(exceptions[i].getQualifiedName());
			if(i < exceptions.length - 1)
				sb.append(", ");
		}
		return sb.toString();
	}

	/**
	 * Compares two MethodVertex.
	 * @param obj Object to compare with.
	 * @return <code>True</code> if <code>this</code> and <code>obj</code> are semantically equal; <code>false</code> otherwise.
	 */
	public boolean equals(Object obj) {
		MethodVertex node = (MethodVertex) obj;
		if(node.name.compareTo(name) == 0)
			if(node.modifiers == modifiers)
				if(node.sourceClass.getQualifiedName().compareTo(sourceClass.getQualifiedName()) == 0)
					if(node.returnType.getQualifiedName().compareTo(returnType.getQualifiedName()) == 0) {
						boolean flag = true;
						if(node.parameters.length == parameters.length) {
							for(int i = 0; i < parameters.length && flag; i++)
								if(node.parameters[i].getQualifiedName().compareTo(parameters[i].getQualifiedName()) != 0)
									flag = false;
						} else
							flag = false;
						return flag;
					}			
		return false;
	}
	
	/**
	 * Gives an hashcode for this MethodVertex object. To make MethodVertex unique in the graph, they have to be {@link #equals(Object)}<code> = true</code> and have the same hashcode.
	 * For simplicity, this method always returns the same hashcode (<code>0</code>).
	 * @return Hashcode for <code>this</code> object.
	 */
	public int hashCode() {
		return 0;
	}
	
	public void setComplexity(int complexity){
		this.complexity = complexity;
	}
	
	public int getComplexity(){
		return complexity;
	}
	
	public boolean isUpdated(){
		return updated;
	}
	
	public void setUpdated(){
		updated = true;
	}
	
	public int getLoc(){
		return loc;
	}
	
	public void setLoc(int loc){
		this.loc=loc;
	}
	
	public void setClassInfo(ClassInfo classInfo){
		this.classInfo = classInfo;
	}
	
	public ClassInfo getClassInfo(){
		return classInfo;
	}
	
	public void setMethodFieldAccessed(ArrayList<IVariableBinding> fieldAccessed){
		this.fieldAccessed = fieldAccessed;
	}
	
	public ArrayList<IVariableBinding> getMethodFieldAccessed(){
		return fieldAccessed;
	}

}
