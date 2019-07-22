package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.ClassInfo;
//class complete
public class NOC {
	
	public int classNoc(ClassInfo info){
		return info.getChildren().size();
	}
	
	public int packageNoc(){
		return 0;
	}
}
