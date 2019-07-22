package it.unisa.metric.metrics;

import java.util.Stack;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.graph.PackageInfo;
//Indica il numero di package che dipendono da classi del package P.
public class Ca {

	public int packageCa(PackageInfo packageInf, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph) {
		Stack<PackageInfo> bucket = new Stack<PackageInfo>();
		for(ClassInfo classInf : packageInf.getClasses()) {
			for(MethodVertex classMethod : classInf.getMethods()) {
				for(DefaultWeightedEdge e : graph.incomingEdgesOf(classMethod)){
					if(!graph.getEdgeSource(e).getClassInfo().getPackage().equals(packageInf)){
						if(!bucket.contains(graph.getEdgeSource(e).getClassInfo().getPackage())){
							bucket.push(graph.getEdgeSource(e).getClassInfo().getPackage());
						}
					}
				}
			}
		}
	
		return bucket.size();
	}
}
