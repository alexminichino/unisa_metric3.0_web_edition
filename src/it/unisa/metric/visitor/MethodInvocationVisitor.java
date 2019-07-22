package it.unisa.metric.visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.unisa.metric.struct.graph.MethodVertex;
/**
 * Represents an ASTVisitor that takes information about method invocations.
 * Starts from a graph that had all vertices and add all edges.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class MethodInvocationVisitor extends ASTVisitor {

	/**
	 * Methods graph to fill in.
	 */
	private DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph;

	/**
	 * Creates a MethodInvocationVisitor from a DirectedWeightedMultigraph.
	 * @param graph Methods graph to fill in.
	 */
	public MethodInvocationVisitor(DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	/**
	 * Visits SuperMethodInvocation nodes.
	 */
	public boolean visit(SuperMethodInvocation node) {

		IMethodBinding binding = node.resolveMethodBinding();
		if(binding != null){
			CompilationUnit cu = (CompilationUnit) node.getRoot();
			MethodVertex invocationMethodNode = new MethodVertex(cu, binding);
			try {
				// if graph not contains this vertex, the method of this invocation is not in the input project (means: don't care about it).
				if(graph.containsVertex(invocationMethodNode)) {
					ASTNode tmp = node;
					try {
						// search for the method in which this invocation is.
						while(!((tmp = tmp.getParent()) instanceof MethodDeclaration));
					} catch(NullPointerException npe) {
						// if no method found means that the invocation is not in a method body.
						tmp = node;
						// search for the class in which this is invocation is, instead.
						while(!((tmp = tmp.getParent()) instanceof TypeDeclaration));
						TypeDeclaration sourceClass = (TypeDeclaration) tmp;
						ITypeBinding sourceBinding = sourceClass.resolveBinding();
						IMethodBinding[] sourceMethods = sourceBinding.getDeclaredMethods();
						// operations out of a method body will be referenced to all constructors.
						for(IMethodBinding method : sourceMethods){
							if(method.isConstructor()) {
								DefaultWeightedEdge edge = new DefaultWeightedEdge();
								graph.addEdge(findGraphVertex(new MethodVertex(cu, method)), findGraphVertex(invocationMethodNode), edge);
								graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
							}
						}
						return true;
					}
					MethodDeclaration sourceMethod = (MethodDeclaration) tmp;
					IMethodBinding sourceBinding = sourceMethod.resolveBinding();
					MethodVertex sourceMethodNode = new MethodVertex(cu, sourceMethod, sourceBinding);
					DefaultWeightedEdge edge = new DefaultWeightedEdge();

					graph.addEdge(findGraphVertex(sourceMethodNode), findGraphVertex(invocationMethodNode), edge);
					graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
					
				}
			} catch(IllegalArgumentException iae) {
				// this exception occurs when you try to add a loop in a vertex, then don't care about loops.
				return true;
			}
		}
		return true;
	}

	/**
	 * Visits SuperConstructorInvocation nodes.
	 */
	public boolean visit(SuperConstructorInvocation node) {
		
		IMethodBinding binding = node.resolveConstructorBinding();
		if(binding != null){
		
			CompilationUnit cu = (CompilationUnit) node.getRoot();
			MethodVertex invocationMethodNode = new MethodVertex(cu, binding);
			try {
				if(graph.containsVertex(invocationMethodNode)) {
					ASTNode tmp = node;
					try {
						while(!((tmp = tmp.getParent()) instanceof MethodDeclaration));
					} catch(NullPointerException npe) {
						tmp = node;
						while(!((tmp = tmp.getParent()) instanceof TypeDeclaration));
						TypeDeclaration sourceClass = (TypeDeclaration) tmp;
						ITypeBinding sourceBinding = sourceClass.resolveBinding();
						IMethodBinding[] sourceMethods = sourceBinding.getDeclaredMethods();
						for(IMethodBinding method : sourceMethods){
							if(method.isConstructor()) {
								DefaultWeightedEdge edge = new DefaultWeightedEdge();
								graph.addEdge(findGraphVertex(new MethodVertex(cu, method)), findGraphVertex(invocationMethodNode), edge);
								graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
							}
						}
						return true;
					}
					MethodDeclaration sourceMethod = (MethodDeclaration) tmp;
					IMethodBinding sourceBinding = sourceMethod.resolveBinding();
					MethodVertex sourceMethodNode = new MethodVertex(cu, sourceMethod, sourceBinding);
					DefaultWeightedEdge edge = new DefaultWeightedEdge();
	
					graph.addEdge(findGraphVertex(sourceMethodNode), findGraphVertex(invocationMethodNode), edge);
					graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
					
				}
			} catch(IllegalArgumentException iae) {
				return true;
			}
		}
		return true;
	}

	/**
	 * Visits ConstructorInvocation nodes.
	 */
	public boolean visit(ConstructorInvocation node) {

		IMethodBinding binding = node.resolveConstructorBinding();
		if(binding != null){
			CompilationUnit cu = (CompilationUnit) node.getRoot();
			MethodVertex invocationMethodNode = new MethodVertex(cu, binding);
			try {
				if(graph.containsVertex(invocationMethodNode)) {
					ASTNode tmp = node;
					try {
						while(!((tmp = tmp.getParent()) instanceof MethodDeclaration));
					} catch(NullPointerException npe) {
						tmp = node;
						while(!((tmp = tmp.getParent()) instanceof TypeDeclaration));
						TypeDeclaration sourceClass = (TypeDeclaration) tmp;
						ITypeBinding sourceBinding = sourceClass.resolveBinding();
						IMethodBinding[] sourceMethods = sourceBinding.getDeclaredMethods();
						for(IMethodBinding method : sourceMethods){
							if(method.isConstructor()) {
								DefaultWeightedEdge edge = new DefaultWeightedEdge();
								graph.addEdge(findGraphVertex(new MethodVertex(cu, method)), findGraphVertex(invocationMethodNode), edge);
								graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
							}
						}
						return true;
					}
					MethodDeclaration sourceMethod = (MethodDeclaration) tmp;
					IMethodBinding sourceBinding = sourceMethod.resolveBinding();
					MethodVertex sourceMethodNode = new MethodVertex(cu, sourceMethod, sourceBinding);
					DefaultWeightedEdge edge = new DefaultWeightedEdge();
	
					graph.addEdge(findGraphVertex(sourceMethodNode), findGraphVertex(invocationMethodNode), edge);
					graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
					
				}
			} catch(IllegalArgumentException iae) {
				return true;
			}
		}
		return true;
	}

	/**
	 * Visits ClassInstanceCreation nodes.
	 */
	public boolean visit(ClassInstanceCreation node) {
		
		IMethodBinding binding = node.resolveConstructorBinding();
		if(binding != null){
			CompilationUnit cu = (CompilationUnit) node.getRoot();
			MethodVertex invocationMethodNode = new MethodVertex(cu, binding);
			try {
				if(graph.containsVertex(invocationMethodNode)) {
					ASTNode tmp = node;
					try {
						while(!((tmp = tmp.getParent()) instanceof MethodDeclaration));
					} catch(NullPointerException npe) {
						tmp = node;
						while(!((tmp = tmp.getParent()) instanceof TypeDeclaration));
						TypeDeclaration sourceClass = (TypeDeclaration) tmp;
						ITypeBinding sourceBinding = sourceClass.resolveBinding();
						IMethodBinding[] sourceMethods = sourceBinding.getDeclaredMethods();
						for(IMethodBinding method : sourceMethods){
							if(method.isConstructor()) {
								DefaultWeightedEdge edge = new DefaultWeightedEdge();
								graph.addEdge(findGraphVertex(new MethodVertex(cu, method)), findGraphVertex(invocationMethodNode), edge);
								graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
							}
						}
						return true;
					}
					MethodDeclaration sourceMethod = (MethodDeclaration) tmp;
					IMethodBinding sourceBinding = sourceMethod.resolveBinding();
					MethodVertex sourceMethodNode = new MethodVertex(cu, sourceMethod, sourceBinding);
					DefaultWeightedEdge edge = new DefaultWeightedEdge();
	
					graph.addEdge(findGraphVertex(sourceMethodNode), findGraphVertex(invocationMethodNode), edge);
					graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
					
				}
			} catch(IllegalArgumentException iae) {
				return true;
			}
		}
		return true;
	}

	/**
	 * Visits MethodInvocation nodes.
	 */
	public boolean visit(MethodInvocation node) {

		IMethodBinding binding = node.resolveMethodBinding();
		if(binding != null){
			CompilationUnit cu = (CompilationUnit) node.getRoot();
			MethodVertex invocationMethodNode = new MethodVertex(cu, binding);
			try {
				if(graph.containsVertex(invocationMethodNode)) {
					ASTNode tmp = node;
					try {
						while(!((tmp = tmp.getParent()) instanceof MethodDeclaration));
					} catch(NullPointerException npe) {
						tmp = node;
						while(!((tmp = tmp.getParent()) instanceof TypeDeclaration));
						TypeDeclaration sourceClass = (TypeDeclaration) tmp;
						ITypeBinding sourceBinding = sourceClass.resolveBinding();
						IMethodBinding[] sourceMethods = sourceBinding.getDeclaredMethods();
						for(IMethodBinding method : sourceMethods){
							if(method.isConstructor()) {
								DefaultWeightedEdge edge = new DefaultWeightedEdge();
								graph.addEdge(findGraphVertex(new MethodVertex(cu, method)), findGraphVertex(invocationMethodNode), edge);
								graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
							}
						}
						return true;
					}
					MethodDeclaration sourceMethod = (MethodDeclaration) tmp;
					IMethodBinding sourceBinding = sourceMethod.resolveBinding();
					MethodVertex sourceMethodNode = new MethodVertex(cu, sourceMethod, sourceBinding);
					
					DefaultWeightedEdge edge = new DefaultWeightedEdge();
	
					graph.addEdge(findGraphVertex(sourceMethodNode), findGraphVertex(invocationMethodNode), edge);
					graph.setEdgeWeight(edge, cu.getLineNumber(node.getStartPosition()));
					
				}
			} catch(IllegalArgumentException iae) {
				return true;
			}
		}
			
		return true;
	}
	
	public MethodVertex findGraphVertex(MethodVertex fakeVertex){
		
		if(graph.containsVertex(fakeVertex)){
			for(MethodVertex v : graph.vertexSet()){
				if(v.equals(fakeVertex)){
					//returning true graph vertex
					return v;
				}
			}
			//returning fake graph vertex
			return fakeVertex;
		}else{
			//returning fake graph vertex
			return fakeVertex;
		}
	}

}