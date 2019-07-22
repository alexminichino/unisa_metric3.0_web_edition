package it.unisa.metric.metrics;

import java.util.Stack;

import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.visitor.StatementVisitor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
//completo
public class Complexity {
	
	public int methodComplexity(MethodVertex v){
		return v.getComplexity();
	}
	
	public int visitStatements(CompilationUnit cu, IMethodBinding method){
		//start
		MethodDeclaration methodNode = (MethodDeclaration) cu.findDeclaringNode(method);
		
		ASTParser parser = ASTParser.newParser(AST.JLS9);
		
		parser.setSource(("public class A {"+methodNode.toString()+"}").toCharArray());
		
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		CompilationUnit compilation = (CompilationUnit) parser.createAST(null);	
		
		StatementVisitor sv = new StatementVisitor();
		
		compilation.accept(sv);
		//end 
		
		return sv.getComplexity();
	}
	
	//update procedure with the usage of a stack to find cycle in the graph
	public void update(DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph){
			Stack<MethodVertex> bucket = null;
			
			for(MethodVertex v : graph.vertexSet()){
				if(!v.isUpdated()){
					bucket = new Stack<MethodVertex>();
					bucket.push(v);
					update(graph, v, bucket);
				}
			}
			
			return;
		}
		
	private void update(DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph, MethodVertex v, Stack<MethodVertex> bucket){
		if(v.isUpdated()){				//node already updated
			return;
		}
		else if(graph.outgoingEdgesOf(v).isEmpty()){
			//set updated because it's the last node of a chain of methods
			v.setUpdated();
			return;
		}
		else{
			//updating complexity based on linked graph nodes
			for(DefaultWeightedEdge e : graph.outgoingEdgesOf(v)){
				if(!graph.getEdgeTarget(e).isUpdated()){
					if(!bucket.contains(graph.getEdgeTarget(e))){
						//cycle not detected
						bucket.push(graph.getEdgeTarget(e));
						update(graph, graph.getEdgeTarget(e), bucket);
						//update
						v.setComplexity(v.getComplexity() +  graph.getEdgeTarget(e).getComplexity() -1);
					}else{
						//found a cycle
						//when i find a cycle I consider it as an end of invocation chain							
						//System.out.println("CYCLE FOUND on this method: "+graph.getEdgeTarget(e).getName());
					}
				}
			}
				
			//v is now updated
			v.setUpdated();
			return;
			}
		}
}
