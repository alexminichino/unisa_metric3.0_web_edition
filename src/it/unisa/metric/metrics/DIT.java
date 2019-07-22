package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.PackageInfo;
//class complete
public class DIT {
	public int classDit(ClassInfo classInf){
		int depth = 0;
		while(classInf.getParent() != null){
			depth++;
			classInf = classInf.getParent();
		}
		return depth;
	}
	
	public int packageDit(PackageInfo packageInf) {
		int maxDepth=0;
		for(ClassInfo classInf : packageInf.getClasses()) {
			if(maxDepth < classDit(classInf)){
				maxDepth = classDit(classInf);
			}
		}
		return maxDepth;
	}
}
