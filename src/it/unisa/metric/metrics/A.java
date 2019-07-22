package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.PackageInfo;

public class A {

	public double packageA(PackageInfo packageInf){
		double absCount = 0;
		double concCount = 0;
		for(ClassInfo classInf : packageInf.getClasses()) {
			if(classInf.isAbstract()) {
				absCount++;
			}
			if(classInf.isConcrete()) {
				concCount++;
			}
		}
		
		if(absCount == 0 && concCount == 0){
			//division by zero
			return -1;
		}else{
			return absCount/(absCount+concCount);
		}
	}
}
