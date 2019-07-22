package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.PackageInfo;
//class complete
public class NM {

	public int nmClass(ClassInfo classInf){
		return classInf.getMethods().size();
	}
	
	public int nmPackage(PackageInfo packageInfo) {
		int numberOfMethodsPackage=0;
		for( ClassInfo classInf : packageInfo.getClasses()) {
			numberOfMethodsPackage = numberOfMethodsPackage + nmClass(classInf);
		}
		return numberOfMethodsPackage;
	}
}
