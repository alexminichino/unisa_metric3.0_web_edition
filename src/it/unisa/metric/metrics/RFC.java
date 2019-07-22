package it.unisa.metric.metrics;

import java.util.Stack;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
//The RFC is the "Number of Distinct Methods and Constructors invoked by a Class".
public class RFC {
	public int classRfc(ClassInfo classInf, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph){
		Stack<MethodVertex> bucket = new Stack<MethodVertex>();
		for(MethodVertex invoker : classInf.getMethods()){
			for(DefaultWeightedEdge outgoingedge : graph.outgoingEdgesOf(invoker)){
				if(!bucket.contains(graph.getEdgeTarget(outgoingedge))){
					bucket.add(graph.getEdgeTarget(outgoingedge));
				}
			}
		}
		return bucket.size();
	}
}
