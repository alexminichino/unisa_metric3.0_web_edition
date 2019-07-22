package it.unisa.metric.metrics;

import it.unisa.metric.struct.graph.ClassInfo;
import it.unisa.metric.struct.graph.MethodVertex;
import it.unisa.metric.struct.graph.PackageInfo;
//class complete
public class WMC {
	public int classWmc(ClassInfo classInf){
		int complexity = 0;
		for(MethodVertex v : classInf.getMethods()){
			complexity = complexity + v.getComplexity();
		}
		return complexity;
	}
	
	public int packageWmc(PackageInfo packageInf){
		int complexityPackage = 0;
		for(ClassInfo classInf : packageInf.getClasses()) {
			complexityPackage = complexityPackage + classWmc(classInf);
		}
		
		return complexityPackage;
	}
}
