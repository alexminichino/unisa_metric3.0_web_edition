package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.PackageInfo;

public class AC {

	public int packageAc(PackageInfo packageInf) {
		int abstractClassCount=0;
		for(ClassInfo classInf: packageInf.getClasses()) {
			if(classInf.isAbstract()) {
				abstractClassCount++;
			}
		}
		return abstractClassCount;
	}
}
