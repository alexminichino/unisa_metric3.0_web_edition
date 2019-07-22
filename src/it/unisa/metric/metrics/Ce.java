package it.unisa.metric.metrics;

import java.util.Stack;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.graph.PackageInfo;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class Ce {

	public int packageCe(PackageInfo packageInf, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph) {
		Stack<PackageInfo> bucket = new Stack<PackageInfo>();
		for(ClassInfo classInf : packageInf.getClasses()) {
			for(MethodVertex classMethod : classInf.getMethods()) {
				for(DefaultWeightedEdge e : graph.outgoingEdgesOf(classMethod)){
					if(!graph.getEdgeTarget(e).getClassInfo().getPackage().equals(packageInf)){
						bucket.push(graph.getEdgeSource(e).getClassInfo().getPackage());
					}
				}
			}
		}
		
		return bucket.size();
	}
}
