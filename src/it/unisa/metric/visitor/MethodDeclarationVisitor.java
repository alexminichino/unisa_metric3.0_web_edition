package it.unisa.metric.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.unisa.metric.metrics.Complexity;
import it.unisa.metric.metrics.LCOM;
import it.unisa.metric.metrics.LOC;
import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
/**
 * Represents an ASTVisitor that takes information about method declarations.
 * Starts from an empty graph and makes all vertices.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 *
 */
public class MethodDeclarationVisitor extends ASTVisitor {

	/**
	 * Methods graph to fill in.
	 */
	private DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph;

	/**
	 * Creates a MethodDeclarationVisitor from a DirectedWeightedMultigraph.
	 * @param graph Methods graph to fill in. 
	 */
	public MethodDeclarationVisitor(DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	/**
	 * Visits TypeDeclaration nodes.
	 */
	public boolean visit(TypeDeclaration node) {

		ITypeBinding binding = node.resolveBinding();
		IMethodBinding[] methodsBinding = binding.getDeclaredMethods();
		MethodVertex vertex;
		LOC lp = new LOC();
		Complexity comp = new Complexity();
		int complexity = 0;
		int loc = 0;
		CompilationUnit cu = (CompilationUnit) node.getRoot();
		
		//gather class info
		ClassInfo classInfo = new ClassInfo(binding, node);
		
		for(IMethodBinding method : methodsBinding){			
			//check field usage for the method
			LCOM lcom = new LCOM();
			ArrayList<IVariableBinding> fieldAccessed = lcom.checkFieldUsage(cu, method, binding.getDeclaredFields());
			
			if(method.isConstructor()){
				//not declared constructor
				if(cu.findDeclaringNode(method.getKey())== null){
					graph.addVertex(vertex = new MethodVertex((CompilationUnit) node.getRoot(), method));
					
					vertex.setComplexity(1);
					
					vertex.setLoc(0);
					
					//add vertex to the classinfo
					classInfo.addMethod(vertex);
					
					//add classinfo to the vertex
					vertex.setClassInfo(classInfo);
					
					//add accessedfields
					vertex.setMethodFieldAccessed(null);
				}
				//declared constructor
				else{
					
 					complexity = comp.visitStatements(cu, method);
					
					loc = lp.methodLocEvaluation(cu.findDeclaringNode(method).toString());
					
					graph.addVertex(vertex = new MethodVertex((CompilationUnit) node.getRoot(), method));
				
					vertex.setComplexity(complexity);
					
					vertex.setLoc(loc);
					
					//add vertex to the classinfo
					classInfo.addMethod(vertex);
					
					//add classinfo to the vertex
					vertex.setClassInfo(classInfo);
					
					//add accessedfields
					vertex.setMethodFieldAccessed(fieldAccessed);
					
				}
			}
			//generic method
			else{
				
				complexity = comp.visitStatements(cu, method);
				
				loc = lp.methodLocEvaluation(cu.findDeclaringNode(method).toString());
				
				graph.addVertex(vertex = new MethodVertex((CompilationUnit) node.getRoot(), method));
				
				vertex.setComplexity(complexity);
				
				vertex.setLoc(loc);
				
				//add vertex to the classinfo
				classInfo.addMethod(vertex);
				
				//add classinfo to the vertex
				vertex.setClassInfo(classInfo);
				
				//add accessedfields
				vertex.setMethodFieldAccessed(fieldAccessed);
			}
		}

		return true;
	}

	/**
	 * Visits AnonymousClassDeclaration nodes.
	 */
	public boolean visit(AnonymousClassDeclaration node) {
		
		ITypeBinding binding = node.resolveBinding();
		IMethodBinding[] methodsBinding = binding.getDeclaredMethods();
		MethodVertex vertex;
		LOC lp = new LOC();
		Complexity comp = new Complexity();
		int complexity = 0;
		int loc = 0;
		CompilationUnit cu = (CompilationUnit) node.getRoot();
		
		//gather class info
		ClassInfo classInfo = new ClassInfo(binding, node);
		
		for(IMethodBinding method : methodsBinding){

			//check field usage for the method
			LCOM lcom = new LCOM();
			ArrayList<IVariableBinding> fieldAccessed = lcom.checkFieldUsage(cu, method, binding.getDeclaredFields());
			
			if(method.isConstructor()){
				//not declared constructor
				if(cu.findDeclaringNode(method.getKey())== null){
					//add vertex to the graph
					graph.addVertex(vertex = new MethodVertex((CompilationUnit) node.getRoot(), method));
					
					//set default complexity for a non declared constructor
					vertex.setComplexity(1);
					
					//set default loc value for a non declared constructor
					vertex.setLoc(0);
					
					//add vertex to the classinfo
					classInfo.addMethod(vertex);
					
					//add classinfo to the vertex
					vertex.setClassInfo(classInfo);
					
					//add accessedfields
					vertex.setMethodFieldAccessed(null);
				}
				//declared constructor
				else{
					//calculate complexity of the given vertex
					complexity = comp.visitStatements(cu, method);
					
					//calculate loc of the given vertex
					loc = lp.methodLocEvaluation(cu.findDeclaringNode(method).toString());
					
					//add vertex to the graph
					graph.addVertex(vertex = new MethodVertex((CompilationUnit) node.getRoot(), method));
					
					//set complexity
					vertex.setComplexity(complexity);
					
					//set loc value
					vertex.setLoc(loc);
					
					//add vertex to the classinfo
					classInfo.addMethod(vertex);
					
					//add classinfo to the vertex
					vertex.setClassInfo(classInfo);
					
					//add accessedfields
					vertex.setMethodFieldAccessed(fieldAccessed);
				}
			//generic method
			}else{
				//calculate complexity of the given vertex
				complexity = comp.visitStatements(cu, method);
				
				//calculate loc of the given vertex
				loc = lp.methodLocEvaluation(cu.findDeclaringNode(method).toString());
				
				//add vertex to the graph
				graph.addVertex(vertex = new MethodVertex((CompilationUnit) node.getRoot(), method));
			
				//set complexity
				vertex.setComplexity(complexity);
				
				//set loc value
				vertex.setLoc(loc);
				
				//add vertex to the classinfo
				classInfo.addMethod(vertex);
				
				//add classinfo to the vertex
				vertex.setClassInfo(classInfo);
				
				//add accessedfields
				vertex.setMethodFieldAccessed(fieldAccessed);
			}
		}	
		
		return true;
	}
}
