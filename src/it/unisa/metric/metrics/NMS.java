package it.unisa.metric.metrics;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;

//number of messages send by all the methods of the analyzed class
public class NMS {

	public NMS(){
		
	}
	//to check the number of messages send by a method i just analyze the outgoing edge of the method vertex
	public int methodNms(MethodVertex v, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph){
		return graph.outgoingEdgesOf(v).size();
	}
	
	//the nms value for a class is the sum of all the messages send by his methods
	public int classNms(ClassInfo classInf, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph){
		int nms = 0;
		for(MethodVertex v : classInf.getMethods()){
			nms = nms + methodNms(v, graph);
		}
		return nms;
	}
}
