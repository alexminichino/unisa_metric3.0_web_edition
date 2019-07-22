package it.unisa.metric.metrics;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.graph.PackageInfo;

public class I {

	public double packageI(PackageInfo packageInf,DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph){
		Ce ce = new Ce();
		Ca ca = new Ca();
		double efferentCoupling = ce.packageCe(packageInf, graph);
		double afferentCoupling = ca.packageCa(packageInf, graph);
		
		if(efferentCoupling == 0 && afferentCoupling == 0){
			//division by zero
			return -1;
		}else{
			return efferentCoupling/(afferentCoupling + efferentCoupling);
		}
	}
}
