package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.graph.PackageInfo;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
/*
 * Riguardante le classi, utilizziamo il grafo di invocazione di metodi per conteggiare la fan in ovvero tutti i metodi richiamati esternamente alla classe,
 * definiti come gli archi uscenti, mentre per la fan out conteggiamo le invocazioni di metodi all’interno della stessa classe. Riguardante i package, 
 * utilizziamo il grafo di invocazione dei metodi per conteggiare la fan in ovvero tutti i metodi richiamati esternamente al package, definiti come gli archi uscenti, 
 * mentre per la fan out conteggiamo le invocazioni di metodi entranti al package.
 */
public class FI {

	public double classFi(ClassInfo classInf, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph){
		double fanIn = 0;
		double fanOut = 0;
		double loc = classInf.getLoc();
		
		//conteggio fan in -> numero di metodi che sono chiamati esternamente alla classe
		for(MethodVertex method : classInf.getMethods()){
			for(DefaultWeightedEdge e : graph.incomingEdgesOf(method)){
				if(!graph.getEdgeSource(e).getClassInfo().equals(method.getClassInfo())){
					//this method is called in another class
					fanIn++;
					break;
				}
			}
		}
		
		//conteggio fan out -> metodi chiamati localmente nella classe
		for(MethodVertex method : classInf.getMethods()){
			for(DefaultWeightedEdge e : graph.incomingEdgesOf(method)){
				if(graph.getEdgeSource(e).getClassInfo().equals(method.getClassInfo())){
					//this method is called internally to this class
					fanOut++;
					break;
				}
			}
		}
		
		
		return loc*(fanIn * fanOut);
	}
	
	public double packageFi(PackageInfo packageInf, DirectedWeightedMultigraph<MethodVertex, DefaultWeightedEdge> graph){
		double loc = packageInf.getLoc();
		
		//fan in package -> numero di metodi chiamati esternamente al package
		double fi = 0;
		for(ClassInfo classInf : packageInf.getClasses()){
			for(MethodVertex method : classInf.getMethods()){
				for(DefaultWeightedEdge e : graph.incomingEdgesOf(method)){
					if(!graph.getEdgeSource(e).getClassInfo().getPackage().equals(packageInf)){
						//this method is called externally to the package
						fi++;
						break;
					}
				}
			}
		}
		
		//fan out package -> numero di metodi che fanno riferimento al package(metodi interni)
		double fo = 0;
		for(ClassInfo classInf : packageInf.getClasses()){
			for(MethodVertex method : classInf.getMethods()){
				for(DefaultWeightedEdge e : graph.incomingEdgesOf(method)){
					if(graph.getEdgeSource(e).getClassInfo().getPackage().equals(packageInf)){
						//this method is called externally to the package
						fo++;
						break;
					}
				}
			}
		}
		
		return loc*fi*fo;
	}
}
