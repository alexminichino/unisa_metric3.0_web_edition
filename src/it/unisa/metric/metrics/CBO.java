package it.unisa.metric.metrics;

import java.util.Stack;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
//class complete
/* Conteggio di quante classi sono accoppiate alla classe in esame.
 * Contiamo gli archi entrati nei metodi della classe, provenienti da altre classi e gli archi uscenti dai metodi della classe, che puntano in metodi di un'altra classe.
 */
public class CBO {

	public int classCbo(ClassInfo classInf, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph){
		
		Stack<ClassInfo> bucket = new Stack<ClassInfo>();
		for(MethodVertex v : classInf.getMethods()){
			//outgoing edge parsing
			for(DefaultWeightedEdge e : graph.outgoingEdgesOf(v)){
				if(!bucket.contains(graph.getEdgeTarget(e).getClassInfo())){
					bucket.push(graph.getEdgeTarget(e).getClassInfo());
				}
			}
			//incoming edge parsing
			for(DefaultWeightedEdge e : graph.incomingEdgesOf(v)){
				if(!bucket.contains(graph.getEdgeTarget(e).getClassInfo())){
					bucket.push(graph.getEdgeTarget(e).getClassInfo());
				}
			}
			
		}
		
		return bucket.size();
	}
}
